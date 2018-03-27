package se.sst_55t.betterthanelectricity.block.quarry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
//import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.block.*;
import se.sst_55t.betterthanelectricity.block.cable.TileEntityCable;
import se.sst_55t.betterthanelectricity.block.inventory.SlotBattery;
import se.sst_55t.betterthanelectricity.block.multiSocket.TileEntityMultiSocketOut;
import se.sst_55t.betterthanelectricity.item.IBattery;

import javax.annotation.Nullable;

import java.util.List;

import static net.minecraft.tileentity.TileEntityHopper.putStackInInventoryAllSlots;

/**
 * Created by Timmy on 2016-11-27.
 */
public class TileEntityQuarry extends TileEntityLockable implements ITickable, ISidedInventory, IConsumer
{
    public static final int BASE_CONSUME_RATE = 1;

    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {1,2,3,4,5};
    private static final int[] SLOTS_SIDES = new int[] {0};
    /** The ItemStacks that hold the items currently being used in the furnace */
    private NonNullList<ItemStack> quarryItemStacks = NonNullList.<ItemStack>withSize(6, ItemStack.EMPTY);
    /** The number of ticks that the quarry will keep working */
    private int workTimeLeft;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for */
    private int currentConsumeRate;
    private int currentBlockMineTime;
    private int totalBlockMineTime = getTimeToMineBlock();
    private String furnaceCustomName;
    private int transferCooldown = -1;

    private BlockPos minerPosition;

    /** Gets minerPosition */
    public BlockPos getMinerPosition() {
        return minerPosition;
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.quarryItemStacks.size();
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.quarryItemStacks)
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
        return this.quarryItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nullable
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.quarryItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nullable
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.quarryItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        ItemStack itemstack = this.quarryItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.quarryItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        /*
        if (index == 0 && !flag)
        {
            this.totalBlockMineTime = this.getTimeToMineBlock();
            this.currentBlockMineTime = 0;
            this.markDirty();
        }
        */
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.hasCustomName() ? this.furnaceCustomName : "container.quarry";
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
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityQuarry.class, new String[] {"Items"}));
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        // getUpdateTag() is called whenever the chunkdata is sent to the
        // client. In contrast getUpdatePacket() is called when the tile entity
        // itself wants to sync to the client. In many cases you want to send
        // over the same information in getUpdateTag() as in getUpdatePacket().
        return writeToNBT(new NBTTagCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        // Prepare a packet for syncing our TE to the client. Since we only have to sync the stack
        // and that's all we have we just write our entire NBT here. If you have a complex
        // tile entity that doesn't need to have all information on the client you can write
        // a more optimal NBT here.
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        // Here we get the packet from the server and read it into our client side tile entity
        this.readFromNBT(packet.getNbtCompound());
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.quarryItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.quarryItemStacks);
        this.workTimeLeft = compound.getInteger("BurnTime");
        this.currentBlockMineTime = compound.getInteger("CookTime");
        this.totalBlockMineTime = compound.getInteger("CookTimeTotal");
        this.currentConsumeRate = getItemBurnTime(this.quarryItemStacks.get(0));
        this.transferCooldown = compound.getInteger("TransferCooldown");
        this.minerPosition = new BlockPos(compound.getInteger("PosX"),compound.getInteger("PosY"),compound.getInteger("PosZ"));

        if (compound.hasKey("CustomName", 8))
        {
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("BurnTime", this.workTimeLeft);
        compound.setInteger("CookTime", this.currentBlockMineTime);
        compound.setInteger("CookTimeTotal", this.totalBlockMineTime);
        ItemStackHelper.saveAllItems(compound, this.quarryItemStacks);
        compound.setInteger("TransferCooldown", this.transferCooldown);
        if(minerPosition == null)
        {
            minerPosition = new BlockPos(getMinX(),this.pos.getY(),getMinZ());
        }
        compound.setInteger("PosX", this.minerPosition.getX());
        compound.setInteger("PosY", this.minerPosition.getY());
        compound.setInteger("PosZ", this.minerPosition.getZ());

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
     * Quarry isWorking
     */
    public boolean isWorking()
    {
        return this.workTimeLeft > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isWorking(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    public boolean canStartBurn(ItemStack batteryStack, TileEntity generatorTE)
    {
        if(!batteryStack.isEmpty() && batteryStack.getItem() instanceof IBattery)
        {
            if(((IBattery)batteryStack.getItem()).getCharge(batteryStack) > 0)
            {
                return true;
            }
        }

        if(generatorTE != null && generatorTE instanceof IGenerator && ((IGenerator)generatorTE).isConnected())
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

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        if(minerPosition == null)
        {
            minerPosition = new BlockPos(getMinX(),this.pos.getY(),getMinZ());
        }

        boolean flag = this.isWorking();
        boolean isDirty = false;

        if (this.isWorking())
        {
            --this.workTimeLeft;
            placeScaffold();
        }

        if (!this.world.isRemote)
        {
            ItemStack batteryStack = this.quarryItemStacks.get(0);
            TileEntity te = getGeneratorTE();
            if (this.isWorking() || canStartBurn(batteryStack, te))
            {
                if (!this.isWorking())
                {
                    this.workTimeLeft = BASE_CONSUME_RATE;
                    this.currentConsumeRate = this.workTimeLeft;

                    if (this.isWorking())
                    {
                        isDirty = true;

                        if (!batteryStack.isEmpty() && ((IBattery)batteryStack.getItem()).getCharge(batteryStack) > 0)
                        {
                            ((IBattery)batteryStack.getItem()).decreaseCharge(batteryStack);
                        }
                        else if(te instanceof IElectricityStorage  && ((IElectricityStorage)te).getCharge() > 0)
                        {
                            ((IElectricityStorage) getGeneratorTE()).decreaseCharge();
                        }
                    }
                }

                if (this.isWorking() && this.hasBlockToMine())
                {
                    //System.out.println("Block breaking");
                    ++this.currentBlockMineTime;
                    world.sendBlockBreakProgress(-1,minerPosition,(currentBlockMineTime/14));

                    if (this.currentBlockMineTime == this.totalBlockMineTime)
                    {
                        //System.out.println("Block Broken");
                        this.currentBlockMineTime = 0;
                        this.totalBlockMineTime = this.getTimeToMineBlock();
                        this.quarryBlock();
                        world.sendBlockBreakProgress(-1,minerPosition,0);
                        isDirty = true;
                    }
                }
                else if (this.isWorking() && !this.hasBlockToMine() && minerPosition.getY() > 1)
                {

                    while(!this.hasBlockToMine() && minerPosition.getY() > 1)
                    {
                        if (minerPosition.getX() < getMaxX())
                        {
                            minerPosition = minerPosition.add(1, 0, 0);
                        }
                        else if (minerPosition.getX() == getMaxX() && minerPosition.getZ() < getMaxZ())
                        {
                            minerPosition = minerPosition.add(-8, 0, 1);
                        }
                        else if (minerPosition.getZ() == getMaxZ())
                        {
                            minerPosition = minerPosition.add(-8, -1, -8);
                        }
                    }
                    world.sendBlockBreakProgress(-1, minerPosition, 0);
                    this.currentBlockMineTime = 0;
                    world.notifyBlockUpdate(this.pos, world.getBlockState(this.pos), world.getBlockState(this.pos), 0);
                }
                else
                {
                    world.sendBlockBreakProgress(-1,minerPosition,0);
                    this.currentBlockMineTime = 0;
                }
            }
            if (flag != this.isWorking())
            {
                isDirty = true;
                BlockQuarry.setState(this.isWorking(), this.world, this.pos);
            }
        }

        if (isDirty)
        {
            this.markDirty();
        }

        if (this.world != null && !this.world.isRemote)
        {
            --this.transferCooldown;

            if (!this.isOnTransferCooldown())
            {
                this.setTransferCooldown(0);
                this.updateItemTransfer();
            }
        }
    }

    protected void placeScaffold()
    {
        for(int x = getMinX()-1; x <= getMaxX()+1; x++)
        {
            if (world.getBlockState(new BlockPos(x, this.pos.getY(), getMinZ()).north()).getBlock() != ModBlocks.quarry_scaffold &&
                    !(world.getBlockState(new BlockPos(x, this.pos.getY(), getMinZ()).north()).getBlock() instanceof BlockQuarry))
            {
                world.setBlockState(new BlockPos(x, this.pos.getY(), getMinZ()).north(), ModBlocks.quarry_scaffold.getDefaultState());
            }
            if (world.getBlockState(new BlockPos(x, this.pos.getY(), getMaxZ()).south()).getBlock() != ModBlocks.quarry_scaffold &&
                    !(world.getBlockState(new BlockPos(x, this.pos.getY(), getMaxZ()).south()).getBlock() instanceof BlockQuarry))
            {
                world.setBlockState(new BlockPos(x, this.pos.getY(), getMaxZ()).south(), ModBlocks.quarry_scaffold.getDefaultState());
            }
        }
        for(int z = getMinZ()-1; z <= getMaxZ()+1; z++)
        {
            if (world.getBlockState(new BlockPos(getMinX(), this.pos.getY(), z).west()).getBlock() != ModBlocks.quarry_scaffold &&
                    !(world.getBlockState(new BlockPos(getMinX(), this.pos.getY(), z).west()).getBlock() instanceof BlockQuarry))
            {
                world.setBlockState(new BlockPos(getMinX(), this.pos.getY(), z).west(), ModBlocks.quarry_scaffold.getDefaultState());
            }
            if (world.getBlockState(new BlockPos(getMaxX(), this.pos.getY(), z).east()).getBlock() != ModBlocks.quarry_scaffold &&
                    !(world.getBlockState(new BlockPos(getMaxX(), this.pos.getY(), z).east()).getBlock() instanceof BlockQuarry))
            {
                world.setBlockState(new BlockPos(getMaxX(), this.pos.getY(), z).east(), ModBlocks.quarry_scaffold.getDefaultState());
            }
        }
    }

    public void removeScaffold()
    {
        for(int x = getMinX()-1; x <= getMaxX()+1; x++)
        {
            if (world.getBlockState(new BlockPos(x, this.pos.getY(), getMinZ()).north()).getBlock() == ModBlocks.quarry_scaffold)
            {
                world.setBlockState(new BlockPos(x, this.pos.getY(), getMinZ()).north(), Blocks.AIR.getDefaultState());
            }
            if (world.getBlockState(new BlockPos(x, this.pos.getY(), getMaxZ()).south()).getBlock() == ModBlocks.quarry_scaffold)
            {
                world.setBlockState(new BlockPos(x, this.pos.getY(), getMaxZ()).south(), Blocks.AIR.getDefaultState());
            }
        }
        for(int z = getMinZ()-1; z <= getMaxZ()+1; z++)
        {
            if (world.getBlockState(new BlockPos(getMinX(), this.pos.getY(), z).west()).getBlock() == ModBlocks.quarry_scaffold)
            {
                world.setBlockState(new BlockPos(getMinX(), this.pos.getY(), z).west(), Blocks.AIR.getDefaultState());
            }
            if (world.getBlockState(new BlockPos(getMaxX(), this.pos.getY(), z).east()).getBlock() == ModBlocks.quarry_scaffold)
            {
                world.setBlockState(new BlockPos(getMaxX(), this.pos.getY(), z).east(), Blocks.AIR.getDefaultState());
            }
        }
    }

    protected boolean updateItemTransfer()
    {
        if (this.world != null && !this.world.isRemote)
        {
            if (!this.isOnTransferCooldown())
            {
                boolean transferOutFlag = false;

                if (    !quarryItemStacks.get(1).isEmpty() ||
                        !quarryItemStacks.get(2).isEmpty() ||
                        !quarryItemStacks.get(3).isEmpty() ||
                        !quarryItemStacks.get(4).isEmpty() ||
                        !quarryItemStacks.get(5).isEmpty()  )
                {
                    transferOutFlag = this.transferItemsOut();
                }

                if (transferOutFlag)
                {
                    this.setTransferCooldown(8);
                    this.markDirty();
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    /**
     * Amount of ticks required to destroy a block.
     * Lower number = faster. 20 ticks = 1 sec
     */
    public int getTimeToMineBlock()
    {
        return 10;
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean hasBlockToMine()
    {
        if(world.getBlockState(minerPosition).getBlock() == Blocks.AIR)
        {
            return false;
        }
        else if(world.getBlockState(minerPosition).getBlock() == Blocks.WATER){
            return false;
        }
        else if(world.getBlockState(minerPosition).getBlock() == Blocks.FLOWING_WATER){
            return false;
        }
        else if(world.getBlockState(minerPosition).getBlock() == Blocks.LAVA){
            return false;
        }
        else if(world.getBlockState(minerPosition).getBlock() == Blocks.FLOWING_LAVA){
            return false;
        }
        else if(world.getBlockState(minerPosition).getBlock() == Blocks.BEDROCK){
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void quarryBlock()
    {

        if (this.hasBlockToMine())
        {
            NonNullList<ItemStack> drops = NonNullList.create();
            world.getBlockState(minerPosition).getBlock().getDrops(drops, world, minerPosition,world.getBlockState(minerPosition),0);
            if(!isFull()){
                for (ItemStack drop : drops)
                {
                    if(!insertStack(drop))
                    {
                        drop.setCount(0);
                    }
                }
            }
            world.destroyBlock(minerPosition,false);
        }
    }

    public boolean insertStack(ItemStack mineStack)
    {
        for (int j = 1; j < quarryItemStacks.size(); ++j)
        {
            ItemStack quarrySlotStack = this.quarryItemStacks.get(j);

            if (!quarrySlotStack.isEmpty() && quarrySlotStack.getItem() == mineStack.getItem() && (!mineStack.getHasSubtypes() || mineStack.getMetadata() == quarrySlotStack.getMetadata()) && ItemStack.areItemStackTagsEqual(mineStack, quarrySlotStack))
            {
                int k = quarrySlotStack.getCount() + mineStack.getCount();
                int maxSize = Math.min(getInventoryStackLimit(), mineStack.getMaxStackSize());

                if (k <= maxSize)
                {
                    mineStack.setCount(0);
                    quarrySlotStack.setCount(k);
                }
                else if (quarrySlotStack.getCount() < maxSize)
                {
                    mineStack.shrink(maxSize - quarrySlotStack.getCount());
                    quarrySlotStack.setCount(maxSize);
                }
            }
            else if (quarrySlotStack.isEmpty())
            {
                this.quarryItemStacks.set(j, mineStack.copy());
                mineStack.setCount(0);
                return true;
            }
        }
        if(mineStack.isEmpty())
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
        if (stack == null)
        {
            return 0;
        }
        else
        {
            Item item = stack.getItem();

            if (item instanceof IBattery)
            {
                if(((IBattery)stack.getItem()).getCharge(stack) > 0)
                {
                    return BASE_CONSUME_RATE;
                }
            }
            return 0;
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
        if (index != 0)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = this.quarryItemStacks.get(0);
            return isItemFuel(stack) || SlotBattery.isBattery(stack) && (itemstack.isEmpty() || !(itemstack.getItem() instanceof IBattery));
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
            Item item = stack.getItem();

            if (!(item instanceof IBattery))
            {
                return false;
            }
        }

        return true;
    }

    public String getGuiID()
    {
        return "betterthanelectricity:quarry_block";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerQuarry(playerInventory, this);
    }

    public int getField(int id)
    {
        switch (id)
        {
            case 0:
                return this.workTimeLeft;
            case 1:
                return this.currentConsumeRate;
            case 2:
                return this.currentBlockMineTime;
            case 3:
                return this.totalBlockMineTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value)
    {
        switch (id)
        {
            case 0:
                this.workTimeLeft = value;
                break;
            case 1:
                this.currentConsumeRate = value;
                break;
            case 2:
                this.currentBlockMineTime = value;
                break;
            case 3:
                this.totalBlockMineTime = value;
                break;
        }
    }

    public int getFieldCount()
    {
        return 4;
    }

    public void clear()
    {
        this.quarryItemStacks.clear();
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
    public TileEntity getGeneratorTE()
    {
        TileEntity outputTE;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            outputTE = getConnectedBlockTE(facing);

            if (outputTE != null)
            {
                if(outputTE instanceof TileEntityCable)
                {
                    return ((TileEntityCable) outputTE).getGeneratorTE(facing.getOpposite());
                }
            }
        }
        return null;
    }

    /**
     * Returns the charge given to this block from its generator.
     */
    public float getChargeRate()
    {
        ItemStack batteryStack = this.quarryItemStacks.get(0);
        if(!batteryStack.isEmpty() && batteryStack.getItem() instanceof IBattery && ((IBattery)batteryStack.getItem()).getCharge(batteryStack) > 0)
        {
            return getConsumeRate();
        }

        TileEntity te = getGeneratorTE();
        if(te != null && te instanceof IGenerator)
        {
            return ((IGenerator)te).getChargeRate();
        }

        return 0;
    }

    @Override
    public float getConsumeRate() {
        return (1.0F / (BASE_CONSUME_RATE / 20.0F));
    }

    public float getGUIChargeRatio()
    {
        ItemStack batteryStack = this.quarryItemStacks.get(0);
        if(!batteryStack.isEmpty() && batteryStack.getItem() instanceof IBattery && ((IBattery)batteryStack.getItem()).getCharge(batteryStack) > 0)
        {
            return 1.0F;
        }

        TileEntity te = getGeneratorTE();
        if(te != null && te instanceof IGenerator)
        {
            float chargeRatio = ((IGenerator)te).getChargeRate() / getConsumeRate();
            if(chargeRatio < 0.0F) return 0.0F;
            if(chargeRatio > 1.0F) return 1.0F;
            return chargeRatio;
        }

        return 0.0F;
    }

    /** Determines work area 9x9*/
    public int getMinX(){

        if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.EAST)
        {
            return this.pos.offset(EnumFacing.WEST,9).getX();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.WEST)
        {
            return this.pos.offset(EnumFacing.EAST).getX();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == (EnumFacing.NORTH))
        {
            return this.pos.offset(EnumFacing.WEST, 4).getX();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.SOUTH)
        {
            return this.pos.offset(EnumFacing.WEST, 4).getX();
        }
        return 0;
    }
    public int getMaxX(){

        if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.EAST)
        {
            return this.pos.offset(EnumFacing.WEST).getX();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.WEST)
        {
            return this.pos.offset(EnumFacing.EAST, 9).getX();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == (EnumFacing.NORTH))
        {
            return this.pos.offset(EnumFacing.EAST, 4).getX();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.SOUTH)
        {
            return this.pos.offset(EnumFacing.EAST, 4).getX();
        }
        return 0;
    }
    public int getMinZ(){

        if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.EAST)
        {
            return this.pos.offset(EnumFacing.NORTH, 4).getZ();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.WEST)
        {
            return this.pos.offset(EnumFacing.NORTH, 4).getZ();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.NORTH)
        {
            return this.pos.offset(EnumFacing.SOUTH).getZ();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.SOUTH)
        {
            return this.pos.offset(EnumFacing.NORTH,9).getZ();
        }
        return 0;
    }
    public int getMaxZ(){

        if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.EAST)
        {
            return this.pos.offset(EnumFacing.SOUTH, 4).getZ();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.WEST)
        {
            return this.pos.offset(EnumFacing.SOUTH, 4).getZ();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.NORTH)
        {
            return this.pos.offset(EnumFacing.SOUTH, 9).getZ();
        }
        else if(world.getBlockState(this.pos).getValue(BlockHorizontal.FACING) == EnumFacing.SOUTH)
        {
            return this.pos.offset(EnumFacing.NORTH).getZ();
        }
        return 0;
    }


    /** Copied code from Hopper */


    private boolean transferItemsOut()
    {
        IInventory iinventory = this.getInventoryForHopperTransfer();

        if (iinventory == null)
        {
            return false;
        }
        else
        {
            //EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();
            EnumFacing enumfacing = world.getBlockState(this.pos).getValue(BlockQuarry.FACING).getOpposite();

            if (this.isInventoryFull(iinventory, enumfacing))
            {
                return false;
            }
            else
            {
                for (int i = 1; i < this.getSizeInventory(); ++i)
                {
                    if (!this.getStackInSlot(i).isEmpty())
                    {
                        ItemStack itemstack = this.getStackInSlot(i).copy();
                        ItemStack itemstack1 = putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(i, 1), enumfacing);

                        if (itemstack1.isEmpty())
                        {
                            iinventory.markDirty();
                            return true;
                        }

                        this.setInventorySlotContents(i, itemstack);
                    }
                }

                return false;
            }
        }
    }

    /**
     * Returns the IInventory that this hopper is pointing into
     */
    private IInventory getInventoryForHopperTransfer()
    {
        //EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata());
        EnumFacing enumfacing = world.getBlockState(this.pos).getValue(BlockQuarry.FACING);
        return getInventoryAtPosition(this.getWorld(), this.getXPos() + (double)enumfacing.getFrontOffsetX(), this.getYPos() + (double)enumfacing.getFrontOffsetY(), this.getZPos() + (double)enumfacing.getFrontOffsetZ());
    }

    /**
     * Returns the IInventory (if applicable) of the TileEntity at the specified position
     */
    public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z)
    {
        IInventory iinventory = null;
        int i = MathHelper.floor(x);
        int j = MathHelper.floor(y);
        int k = MathHelper.floor(z);
        BlockPos blockpos = new BlockPos(i, j, k);
        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(blockpos);
        Block block = state.getBlock();

        if (block.hasTileEntity(state))
        {
            TileEntity tileentity = worldIn.getTileEntity(blockpos);

            if (tileentity instanceof IInventory)
            {
                iinventory = (IInventory)tileentity;

                if (iinventory instanceof TileEntityChest && block instanceof BlockChest)
                {
                    iinventory = ((BlockChest)block).getContainer(worldIn, blockpos, true);
                }
            }
        }

        if (iinventory == null)
        {
            List<Entity> list = worldIn.getEntitiesInAABBexcluding((Entity)null, new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), EntitySelectors.HAS_INVENTORY);

            if (!list.isEmpty())
            {
                iinventory = (IInventory)list.get(worldIn.rand.nextInt(list.size()));
            }
        }

        return iinventory;
    }

    private static boolean canCombine(ItemStack stack1, ItemStack stack2)
    {
        if (stack1.getItem() != stack2.getItem())
        {
            return false;
        }
        else if (stack1.getMetadata() != stack2.getMetadata())
        {
            return false;
        }
        else if (stack1.getCount() > stack1.getMaxStackSize())
        {
            return false;
        }
        else
        {
            return ItemStack.areItemStackTagsEqual(stack1, stack2);
        }
    }

    /**
     * Gets the world X position for this hopper entity.
     */
    public double getXPos()
    {
        return (double)this.pos.getX() + 0.5D;
    }

    /**
     * Gets the world Y position for this hopper entity.
     */
    public double getYPos()
    {
        return (double)this.pos.getY() + 0.5D;
    }

    /**
     * Gets the world Z position for this hopper entity.
     */
    public double getZPos()
    {
        return (double)this.pos.getZ() + 0.5D;
    }

    /**
     * Returns false if the inventory has any room to place items in
     */
    private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side)
    {
        if (inventoryIn instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int k : aint)
            {
                ItemStack itemstack1 = isidedinventory.getStackInSlot(k);

                if (itemstack1.isEmpty() || itemstack1.getCount() != itemstack1.getMaxStackSize())
                {
                    return false;
                }
            }
        }
        else
        {
            int i = inventoryIn.getSizeInventory();

            for (int j = 0; j < i; ++j)
            {
                ItemStack itemstack = inventoryIn.getStackInSlot(j);

                if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
                {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean isFull()
    {
        for (ItemStack itemstack : this.quarryItemStacks)
        {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
            {
                return false;
            }
        }

        return true;
    }

    public void setTransferCooldown(int ticks)
    {
        this.transferCooldown = ticks;
    }

    private boolean isOnTransferCooldown()
    {
        return this.transferCooldown > 0;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        AxisAlignedBB bb = INFINITE_EXTENT_AABB;
        //Block type = getBlockType();
        //BlockPos pos = getPos();

        //bb = new AxisAlignedBB(pos.add(-10,-255,-10),pos.add(10,255,10));

        return bb;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return super.shouldRenderInPass(pass);
    }

    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }

}