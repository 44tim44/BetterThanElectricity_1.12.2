package se.sst_55t.betterthanelectricity.block.fuelgenerator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.block.*;
import se.sst_55t.betterthanelectricity.block.cable.TileEntityCable;
import se.sst_55t.betterthanelectricity.block.multiSocket.TileEntityMultiSocketIn;
import se.sst_55t.betterthanelectricity.block.multiSocket.TileEntityMultiSocketOut;
import se.sst_55t.betterthanelectricity.item.IBattery;
import se.sst_55t.betterthanelectricity.item.IChargeable;

import javax.annotation.Nullable;

/**
 * Created by Timmy on 2016-11-27.
 */
public class TileEntityFuelGenerator extends TileEntityLockable implements ITickable, ISidedInventory, IGenerator, IElectricityStorage
{
    private static final int BASE_CHARGE_RATE = 8; // Amount of ticks required to charge 1 energy.
    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    /** The ItemStacks that hold the items currently being used in the furnace */
    private NonNullList<ItemStack> fuelGeneratorItemStacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
    /** The number of ticks that the furnace will keep burning */
    private int furnaceBurnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for */
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String furnaceCustomName;
    private static final int maxCharge = 1;
    private int currentCharge;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.fuelGeneratorItemStacks.size();
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.fuelGeneratorItemStacks)
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
        return this.fuelGeneratorItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nullable
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.fuelGeneratorItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nullable
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.fuelGeneratorItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        ItemStack itemstack = this.fuelGeneratorItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.fuelGeneratorItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.totalCookTime = this.getItemChargeTime();
            this.cookTime = 0;
            this.markDirty();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.hasCustomName() ? this.furnaceCustomName : "container.fuel_generator";
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
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityFuelGenerator.class, new String[] {"Items"}));
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.fuelGeneratorItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.fuelGeneratorItemStacks);
        this.furnaceBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.fuelGeneratorItemStacks.get(1)) / 2;

        if (compound.hasKey("CustomName", 8))
        {
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", this.furnaceBurnTime);
        compound.setInteger("CookTime", this.cookTime);
        compound.setInteger("CookTimeTotal", this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.fuelGeneratorItemStacks);

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
     * Furnace isBurning
     */
    public boolean isBurning()
    {
        return this.furnaceBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    public boolean canStartBurn(ItemStack batteryStack, TileEntity consumerTE)
    {
        if(!batteryStack.isEmpty())
        {
            if(batteryStack.getItem() instanceof IBattery)
            {
                if (((IBattery) batteryStack.getItem()).getCharge(batteryStack) < ((IBattery) batteryStack.getItem()).getMaxCharge(batteryStack)) {
                    return true;
                }
            }
            else if(batteryStack.getItem() instanceof IBattery)
            {
                if (((IChargeable) batteryStack.getItem()).getCharge(batteryStack) < ((IChargeable) batteryStack.getItem()).getMaxCharge(batteryStack)) {
                    return true;
                }
            }
        }

        if(consumerTE != null && consumerTE instanceof IConsumer && ((IConsumer)consumerTE).isConnected())
        {
            if(consumerTE instanceof TileEntityMultiSocketIn)
            {
                TileEntity[] generatorTEList = ((TileEntityMultiSocketIn) consumerTE).getGeneratorTEList(null);
                for(TileEntity generatorTE : generatorTEList)
                {
                    if(generatorTE == this)
                    {
                        return true;
                    }
                }
            }
            else if(((IConsumer) consumerTE).getGeneratorTE() == this)
            {
                return true;
            }

        }

        return false;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        this.totalCookTime = this.getItemChargeTime();

        boolean flag = this.isBurning();
        boolean isDirty = false;

        if (this.isBurning())
        {
            --this.furnaceBurnTime;
        }

        if (!this.world.isRemote)
        {
            ItemStack batteryStack = fuelGeneratorItemStacks.get(0);
            ItemStack itemstackFuel = this.fuelGeneratorItemStacks.get(1);
            TileEntity te = getConsumerTE();

            if (this.isBurning() || !itemstackFuel.isEmpty() && canStartBurn(batteryStack, te))
            {
                if (!this.isBurning() && this.canGenerate())
                {
                    this.furnaceBurnTime = getItemBurnTime(itemstackFuel)/2;
                    this.currentItemBurnTime = this.furnaceBurnTime;

                    if (this.isBurning())
                    {
                        isDirty = true;

                        if (!itemstackFuel.isEmpty())
                        {
                            Item item = itemstackFuel.getItem();
                            itemstackFuel.shrink(1);

                            if (itemstackFuel.isEmpty())
                            {
                                ItemStack item1 = item.getContainerItem(itemstackFuel);
                                this.fuelGeneratorItemStacks.set(1, item1);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canGenerate())
                {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.getItemChargeTime();


                        if(batteryStack.getItem() instanceof IBattery && ((IBattery) batteryStack.getItem()).getCharge(batteryStack) < ((IBattery) batteryStack.getItem()).getMaxCharge(batteryStack))
                        {
                            ((IBattery)batteryStack.getItem()).increaseCharge(batteryStack);
                        }
                        else if(batteryStack.getItem() instanceof IChargeable && ((IChargeable) batteryStack.getItem()).getCharge(batteryStack) < ((IChargeable) batteryStack.getItem()).getMaxCharge(batteryStack))
                        {
                            ((IChargeable)batteryStack.getItem()).increaseCharge(batteryStack);
                        }
                        else
                        {
                            if(!this.world.isRemote) this.increaseCharge();
                        }

                        isDirty = true;
                    }
                }
                else
                {
                    this.cookTime = 0;
                }
            }
            else if (!this.isBurning() && this.cookTime > 0)
            {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }

            if (flag != this.isBurning())
            {
                isDirty = true;
                BlockFuelGenerator.setState(this.isBurning(), this.world, this.pos);
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
    public int getItemChargeTime()
    {
        return BASE_CHARGE_RATE;
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canGenerate()
    {
        if(this.currentCharge < maxCharge)
        {
            return true;
        }
        return false;
    }

    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    public static int getItemBurnTime(ItemStack stack)
    {
        if (stack.isEmpty())
        {
            return 0;
        }
        else
        {
            int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
            if (burnTime >= 0) return burnTime;
            Item item = stack.getItem();

            if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB))
            {
                return 150;
            }
            else if (item == Item.getItemFromBlock(Blocks.WOOL))
            {
                return 100;
            }
            else if (item == Item.getItemFromBlock(Blocks.CARPET))
            {
                return 67;
            }
            else if (item == Item.getItemFromBlock(Blocks.LADDER))
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON))
            {
                return 100;
            }
            else if (Block.getBlockFromItem(item).getDefaultState().getMaterial() == Material.WOOD)
            {
                return 300;
            }
            else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK))
            {
                return 16000;
            }
            else if (item instanceof ItemTool && "WOOD".equals(((ItemTool)item).getToolMaterialName()))
            {
                return 200;
            }
            else if (item instanceof ItemSword && "WOOD".equals(((ItemSword)item).getToolMaterialName()))
            {
                return 200;
            }
            else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe)item).getMaterialName()))
            {
                return 200;
            }
            else if (item == Items.STICK)
            {
                return 100;
            }
            else if (item != Items.BOW && item != Items.FISHING_ROD)
            {
                if (item == Items.SIGN)
                {
                    return 200;
                }
                else if (item == Items.COAL)
                {
                    return 1600;
                }
                else if (item == Items.LAVA_BUCKET)
                {
                    return 20000;
                }
                else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL)
                {
                    if (item == Items.BLAZE_ROD)
                    {
                        return 2400;
                    }
                    else if (item instanceof ItemDoor && item != Items.IRON_DOOR)
                    {
                        return 200;
                    }
                    else
                    {
                        return item instanceof ItemBoat ? 400 : 0;
                    }
                }
                else
                {
                    return 100;
                }
            }
            else
            {
                return 300;
            }
        }
    }

    public static boolean isItemFuel(ItemStack stack)
    {
        /**
         * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
         * fuel
         */
        return getItemBurnTime(stack) > 0;
    }

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
            ItemStack itemstack = this.fuelGeneratorItemStacks.get(1);
            return isItemFuel(stack);
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
        return new ContainerFuelGenerator(playerInventory, this);
    }

    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.furnaceBurnTime;
            case 1:
                return this.currentItemBurnTime;
            case 2:
                return this.cookTime;
            case 3:
                return this.totalCookTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.furnaceBurnTime = value;
                break;
            case 1:
                this.currentItemBurnTime = value;
                break;
            case 2:
                this.cookTime = value;
                break;
            case 3:
                this.totalCookTime = value;
        }
    }

    public int getFieldCount()
    {
        return 4;
    }

    public void clear()
    {
        this.fuelGeneratorItemStacks.clear();
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
                amountOfConnections++;
            }
        }
        return amountOfConnections == 1;
    }

    @Override
    public TileEntity getConsumerTE() {
        TileEntity inputTE;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            inputTE = getConnectedBlockTE(facing);

            if (inputTE != null)
            {
                if(inputTE instanceof TileEntityCable)
                {
                    return ((TileEntityCable) inputTE).getConsumerTE(facing.getOpposite());
                }
            }
        }
        return null;
    }

    @Override
    public float getChargeRate()
    {
        //if (isBurning())
        if(world.getBlockState(this.pos).getBlock() == ModBlocks.fuelGenerator_on)
        {
            return (1.0F / (getItemChargeTime() / 20.0F));
        }
        return 0;
    }

    /**
     * Returns true if a battery is currently being charged, or if a consumer is given energy by this block.
     * Used for GUI.
     *
     */
    public boolean isGivingCharge()
    {
        if(!isBurning())
        {
            return false;
        }

        ItemStack batteryStack = fuelGeneratorItemStacks.get(0);
        if (batteryStack.getItem() instanceof IChargeable)
        {
            if(((IChargeable)batteryStack.getItem()).getCharge(batteryStack) < ((IChargeable)batteryStack.getItem()).getMaxCharge(batteryStack))
            {
                return true;
            }
        }
        else if (batteryStack.getItem() instanceof IBattery)
        {
            if(((IBattery)batteryStack.getItem()).getCharge(batteryStack) < ((IBattery)batteryStack.getItem()).getMaxCharge(batteryStack))
            {
                return true;
            }
        }

        TileEntity consumerTE = getConsumerTE();
        if(consumerTE != null)
        {
            if (consumerTE instanceof TileEntityMultiSocketIn)
            {
                TileEntity[] generatorTEList = ((TileEntityMultiSocketIn) consumerTE).getGeneratorTEList(null);
                for (TileEntity generatorTE : generatorTEList)
                {
                    if (generatorTE == this) {
                        return true;
                    }
                }
            }
            else if (((IConsumer) consumerTE).getGeneratorTE() == this)
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void increaseCharge() {
        if(this.currentCharge < maxCharge) this.currentCharge++;
        this.markDirty();
    }

    @Override
    public void decreaseCharge() {
        if(this.currentCharge > 0) this.currentCharge--;
        this.markDirty();
    }

    @Override
    public void setCharge(int value) {
        if(value > maxCharge)
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

    public int getCharge()
    {
        return this.currentCharge;
    }

    @Override
    public int getMaxCharge() {
        return maxCharge;
    }
}