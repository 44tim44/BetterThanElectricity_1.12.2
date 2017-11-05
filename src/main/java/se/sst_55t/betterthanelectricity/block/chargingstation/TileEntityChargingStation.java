package se.sst_55t.betterthanelectricity.block.chargingstation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import se.sst_55t.betterthanelectricity.block.ICable;
import se.sst_55t.betterthanelectricity.block.IConsumer;
import se.sst_55t.betterthanelectricity.block.IElectricityStorage;
import se.sst_55t.betterthanelectricity.block.IGenerator;
import se.sst_55t.betterthanelectricity.block.cable.TileEntityCable;
import se.sst_55t.betterthanelectricity.block.multiSocket.TileEntityMultiSocketOut;
import se.sst_55t.betterthanelectricity.item.IBattery;
import se.sst_55t.betterthanelectricity.item.IChargeable;

import javax.annotation.Nullable;

/**
 * Created by Timmy on 2016-11-27.
 */
public class TileEntityChargingStation extends TileEntityLockable implements ITickable, ISidedInventory, IElectricityStorage, IConsumer, IGenerator
{
    private static final int BASE_CHARGE_RATE = 2; // Amount of ticks required to charge 1 energy.
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    /** The ItemStacks that hold the items currently being used in the furnace */
    private NonNullList<ItemStack> chargingStationItemStacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);

    private int outChargeTime;
    private int totalOutChargeTime;
    private int inChargeTime;
    private int totalInChargeTime = BASE_CHARGE_RATE;
    private String furnaceCustomName;
    private static final int maxCharge = 6400;
    private int currentCharge;


    public void decreaseCharge()
    {
        this.currentCharge--;
        this.markDirty();
    }

    public void increaseCharge()
    {
        this.currentCharge++;
        this.markDirty();
    }

    public void setCharge(int value)
    {
        if(value > this.maxCharge)
        {
            this.currentCharge = maxCharge;
            this.markDirty();
        }
        else if(value < 0)
        {
            this.currentCharge = 0;
            this.markDirty();
        }
        else
        {
            this.currentCharge = value;
            this.markDirty();
        }
    }

    public int getCharge(){
        return this.currentCharge;
    }

    public int getMaxCharge(){
        return maxCharge;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.chargingStationItemStacks.size();
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.chargingStationItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    @Nullable
    public ItemStack getStackInSlot(int index)
    {
        return this.chargingStationItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nullable
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.chargingStationItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nullable
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.chargingStationItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        ItemStack itemstack = this.chargingStationItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.chargingStationItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.totalOutChargeTime = this.getOutputRate();
            this.outChargeTime = 0;
            this.markDirty();
        }

        if (index == 1 && !flag)
        {
            this.totalInChargeTime = this.getOutputRate();
            this.inChargeTime = 0;
            this.markDirty();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.hasCustomName() ? this.furnaceCustomName : "container.charging_station";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return this.furnaceCustomName != null && !this.furnaceCustomName.isEmpty();
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        this.furnaceCustomName = p_145951_1_;
    }

    public static void registerFixesFurnace(DataFixer fixer)
    {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityChargingStation.class, new String[] {"Items"}));
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.chargingStationItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.chargingStationItemStacks);
        this.outChargeTime = compound.getInteger("OutChargeTime");
        this.totalOutChargeTime = compound.getInteger("OutChargeTimeTotal");
        this.inChargeTime = compound.getInteger("InChargeTime");
        this.totalInChargeTime = compound.getInteger("InChargeTimeTotal");
        this.currentCharge = compound.getInteger("CurrentCharge");

        if (compound.hasKey("CustomName", 8))
        {
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.chargingStationItemStacks);
        compound.setInteger("OutChargeTime", this.outChargeTime);
        compound.setInteger("OutChargeTimeTotal", this.totalOutChargeTime);
        compound.setInteger("InChargeTime", this.inChargeTime);
        compound.setInteger("InChargeTimeTotal", this.totalInChargeTime);
        compound.setInteger("CurrentCharge", this.currentCharge);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.furnaceCustomName);
        }

        return compound;
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        boolean isDirty = false;

        if (!this.world.isRemote)
        {
            ItemStack batteryOutputStack = chargingStationItemStacks.get(0);
            ItemStack batteryInputStack = chargingStationItemStacks.get(1);
            TileEntity generatorTE = getGeneratorTE();

            if(this.currentCharge < maxCharge && this.canTakeCharge(batteryInputStack,generatorTE))
            {
                ++this.inChargeTime;

                if (this.inChargeTime == this.totalInChargeTime)
                {
                    this.inChargeTime = 0;

                    if(batteryInputStack.getItem() instanceof IBattery)
                    {
                        ((IBattery)batteryInputStack.getItem()).decreaseCharge(batteryInputStack);
                        this.increaseCharge();
                    }
                    else if(generatorTE instanceof IElectricityStorage  && ((IElectricityStorage)generatorTE).getCharge() > 0)
                    {
                        ((IElectricityStorage) getGeneratorTE()).decreaseCharge();
                        this.increaseCharge();
                    }

                    isDirty = true;
                }
            }
            else
            {
                this.inChargeTime = 0;
            }

            if (this.currentCharge > 0 && this.canGiveCharge())
            {
                ++this.outChargeTime;

                if (this.outChargeTime == this.totalOutChargeTime)
                {
                    this.outChargeTime = 0;
                    this.totalOutChargeTime = this.getOutputRate();

                    if(batteryOutputStack.getItem() instanceof IBattery)
                    {
                        ((IBattery)batteryOutputStack.getItem()).increaseCharge(batteryOutputStack);
                        this.decreaseCharge();
                    }
                    else if(batteryOutputStack.getItem() instanceof IChargeable)
                    {
                        ((IChargeable)batteryOutputStack.getItem()).increaseCharge(batteryOutputStack);
                        this.decreaseCharge();
                    }

                    isDirty = true;
                }
            }
            else
            {
                this.outChargeTime = 0;
            }
        }

        if (isDirty)
        {
            this.markDirty();
        }
    }

    /**
     * Time to cook:
     * Lower number = faster.
     */
    public int getOutputRate()
    {
        return BASE_CHARGE_RATE;
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canGiveCharge()
    {
        ItemStack itemstack = this.chargingStationItemStacks.get(0);

        if(currentCharge > 0)
        {
            if (!itemstack.isEmpty() && itemstack.getItem() instanceof IBattery)
            {
                if (((IBattery) itemstack.getItem()).getCharge(itemstack) < ((IBattery) itemstack.getItem()).getMaxCharge(itemstack))
                {
                    return true;
                }
            }
            else if (!itemstack.isEmpty() && itemstack.getItem() instanceof IChargeable)
            {
                if (((IChargeable) itemstack.getItem()).getCharge(itemstack) < ((IChargeable) itemstack.getItem()).getMaxCharge(itemstack))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    @SideOnly(Side.CLIENT)
    public static boolean isGivingCharge(TileEntityChargingStation tecs)
    {
        return tecs.canGiveCharge();
    }
    */

    /**
     * Returns true if the Charging Station has a battery in the input slot or if it is connected to a Generator in the input-face
     */
    private boolean canTakeCharge(ItemStack batteryInputStack, TileEntity generatorTE)
    {
        //ItemStack batteryInputStack = this.chargingStationItemStacks.get(1);
        if (!batteryInputStack.isEmpty() && batteryInputStack.getItem() instanceof IBattery)
        {
            if (((IBattery) batteryInputStack.getItem()).getCharge(batteryInputStack) > 0)
            {
                return true;
            }
        }

        //TileEntity generatorTE = getGeneratorTE();
        if (generatorTE != null && generatorTE instanceof IGenerator && ((IGenerator)generatorTE).isConnected())
        {
            if(generatorTE instanceof TileEntityMultiSocketOut)
            {
                TileEntity[] consumerTEList = ((TileEntityMultiSocketOut) generatorTE).getConsumerTEList(null);
                for(TileEntity consumerTE : consumerTEList)
                {
                    if(consumerTE == this && ((IElectricityStorage)generatorTE).getCharge() > 0)
                    {
                        return true;
                    }
                }
            }
            else if(((IGenerator) generatorTE).getConsumerTE() == this && ((IElectricityStorage)generatorTE).getCharge() > 0)
            {
                return true;
            }
        }
        return false;
    }

    /*
    @SideOnly(Side.CLIENT)
    public static boolean isTakingCharge(TileEntityChargingStation tecs)
    {
        return tecs.canTakeCharge();
    }
    */

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        return this.world.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player)
    {
    }

    public void closeInventory(EntityPlayer player)
    {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        if (index != 1)
        {
            return stack.getItem() instanceof IBattery || stack.getItem() instanceof IChargeable;
        }
        else
        {
            return stack.getItem() instanceof IBattery;
        }
    }

    public int[] getSlotsForFace(EnumFacing side)
    {
        return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES);
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
    {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
    {
        if (direction == EnumFacing.DOWN && index == 1)
        {
            return false;
        }

        return true;
    }

    public String getGuiID()
    {
        return "betterthanelectricity:fuel_generator_block";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerChargingStation(playerInventory, this);
    }

    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.inChargeTime;
            case 1:
                return this.totalInChargeTime;
            case 2:
                return this.outChargeTime;
            case 3:
                return this.totalOutChargeTime;
            case 4:
                return this.currentCharge;
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.inChargeTime = value;
                break;
            case 1:
                this.totalInChargeTime = value;
                break;
            case 2:
                this.outChargeTime = value;
                break;
            case 3:
                this.totalOutChargeTime = value;
                break;
            case 4:
                this.currentCharge = value;
        }
    }

    public int getFieldCount()
    {
        return 5;
    }

    public void clear()
    {
        this.chargingStationItemStacks.clear();
    }

    net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.UP);
    net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.DOWN);
    net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, EnumFacing.WEST);

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, EnumFacing facing)
    {
        if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN)
                return (T) handlerBottom;
            else if (facing == EnumFacing.UP)
                return (T) handlerTop;
            else
                return (T) handlerSide;
        return super.getCapability(capability, facing);
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    public static boolean isBlockAboveAir(TileEntityChargingStation te){
        BlockPos blockPos = te.getPos();
        return te.getWorld().isAirBlock(blockPos.offset(EnumFacing.UP));
    }

    private EnumFacing getFrontSide(){
        EnumFacing frontSide = this.world.getBlockState(this.pos).getValue(BlockChargingStation.FACING);
        return frontSide;
    }

    private EnumFacing getBackSide(){
        EnumFacing frontSide = this.world.getBlockState(this.pos).getValue(BlockChargingStation.FACING);
        return frontSide.getOpposite();
    }

    public TileEntity getConnectedBlockTE(EnumFacing facing)
    {
        if(( world.getTileEntity(this.pos.offset(facing)) instanceof ICable) && isConnected())
        {
            return world.getTileEntity(this.pos.offset(facing));
        }
        return null;
    }

    public boolean isConnected()
    {
        int amountOfConnections = 0;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            if( world.getTileEntity(this.pos.offset(facing)) instanceof ICable )
            {
                if (facing != getBackSide() && facing != getFrontSide())
                {
                    amountOfConnections++;
                }
            }
        }
        return amountOfConnections == 0 && ((world.getTileEntity(this.pos.offset(getFrontSide())) instanceof ICable) || (world.getTileEntity(this.pos.offset(getBackSide())) instanceof ICable));
    }

    @Override
    public TileEntity getGeneratorTE()
    {
        EnumFacing facing = getBackSide();

        TileEntity outputTE = getConnectedBlockTE(facing);

        if (outputTE != null)
        {
            if(outputTE instanceof TileEntityCable)
            {
                return ((TileEntityCable) outputTE).getGeneratorTE(facing.getOpposite());
            }
        }
        return null;
    }

    @Override
    public TileEntity getConsumerTE()
    {
        EnumFacing facing = getFrontSide();

        TileEntity inputTE = getConnectedBlockTE(facing);

        if (inputTE != null)
        {
            if(inputTE instanceof TileEntityCable)
            {
                return ((TileEntityCable) inputTE).getConsumerTE(facing.getOpposite());
            }
        }
        return null;
    }

    @Override
    public float getChargeRate() {
        ItemStack batteryOutputStack = this.chargingStationItemStacks.get(0);
        if(batteryOutputStack.isEmpty() ||  ((IChargeable) batteryOutputStack.getItem()).getCharge(batteryOutputStack) == ((IChargeable) batteryOutputStack.getItem()).getMaxCharge(batteryOutputStack))
        {
            if (this.currentCharge > 0)
            {
                TileEntity generatorTE = getGeneratorTE();
                float outerChargeRate = 0;
                if(generatorTE != null && generatorTE instanceof IGenerator)
                {
                    if (generatorTE instanceof TileEntityMultiSocketOut)
                    {
                        TileEntity[] consumerTEList = ((TileEntityMultiSocketOut) generatorTE).getConsumerTEList(null);
                        for (TileEntity consumerTE : consumerTEList)
                        {
                            if (consumerTE == this)
                            {
                                outerChargeRate = ((IGenerator) generatorTE).getChargeRate();
                            }
                        }
                    }
                    else if (((IGenerator) generatorTE).getConsumerTE() == this)
                    {
                        outerChargeRate = ((IGenerator) generatorTE).getChargeRate();
                    }

                    if (this.currentCharge < outerChargeRate)
                    {
                        return outerChargeRate;
                    }
                }

                return getCharge();
            }
        }
        return 0;
    }

    @Override
    public float getConsumeRate() {
        ItemStack batteryInputStack = this.chargingStationItemStacks.get(1);
        if(batteryInputStack.isEmpty() ||
                (batteryInputStack.getItem() instanceof IChargeable && ((IChargeable) batteryInputStack.getItem()).getCharge(batteryInputStack) > 0) ||
                (batteryInputStack.getItem() instanceof IBattery && ((IBattery) batteryInputStack.getItem()).getCharge(batteryInputStack) > 0) )
        {
            if (this.currentCharge < maxCharge)
            {
                return (1.0F / (BASE_CHARGE_RATE / 20.0F));
            }
        }
        return 0;
    }

    public float getGUIChargeRatio()
    {
        ItemStack batteryStack = this.chargingStationItemStacks.get(1);
        if(!batteryStack.isEmpty() && batteryStack.getItem() instanceof IBattery && ((IBattery)batteryStack.getItem()).getCharge(batteryStack) > 0)
        {
            return 1.0F;
        }

        TileEntity te = getGeneratorTE();
        if(te != null && te instanceof IGenerator)
        {
            return 1.0F;
        }

        return 0.0F;
    }

    public float getGUIDischargeRatio()
    {
        ItemStack batteryStack = this.chargingStationItemStacks.get(0);
        if(!batteryStack.isEmpty() && batteryStack.getItem() instanceof IBattery && ((IBattery)batteryStack.getItem()).getCharge(batteryStack) < ((IBattery)batteryStack.getItem()).getMaxCharge(batteryStack))
        {
            return 1.0F;
        }
        if(!batteryStack.isEmpty() && batteryStack.getItem() instanceof IChargeable && ((IChargeable)batteryStack.getItem()).getCharge(batteryStack) < ((IChargeable)batteryStack.getItem()).getMaxCharge(batteryStack))
        {
            return 1.0F;
        }

        TileEntity te = getConsumerTE();
        if(te != null && te instanceof IConsumer)
        {
            return 1.0F;
        }

        return 0.0F;
    }
}