package se.sst_55t.betterthanelectricity.block.compactor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import se.sst_55t.betterthanelectricity.block.ICable;
import se.sst_55t.betterthanelectricity.block.IConsumer;
import se.sst_55t.betterthanelectricity.block.IElectricityStorage;
import se.sst_55t.betterthanelectricity.block.IGenerator;
import se.sst_55t.betterthanelectricity.block.cable.TileEntityCable;
import se.sst_55t.betterthanelectricity.block.inventory.SlotBattery;
import se.sst_55t.betterthanelectricity.item.IBattery;
import se.sst_55t.betterthanelectricity.item.ItemBattery;
import se.sst_55t.betterthanelectricity.item.ModItems;
import se.sst_55t.betterthanelectricity.recipe.CompactorRecipes;

import javax.annotation.Nullable;

/**
 * Created by Timmy on 2016-11-27.
 */
public class TileEntityCompactor extends TileEntityLockable implements ITickable, ISidedInventory, IConsumer
{
    public static final int BASE_CONSUME_RATE = 20;

    private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
    /** The ItemStacks that hold the items currently being used in the furnace */
    private NonNullList<ItemStack> compactorItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
    /** The number of ticks that the furnace will keep burning */
    private int furnaceBurnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for */
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
    private String furnaceCustomName;

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory()
    {
        return this.compactorItemStacks.size();
    }

    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.compactorItemStacks)
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
        return this.compactorItemStacks.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nullable
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.compactorItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nullable
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.compactorItemStacks, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, @Nullable ItemStack stack)
    {
        ItemStack itemstack = this.compactorItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.compactorItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.totalCookTime = this.getCookTime(stack);
            this.cookTime = 0;
            this.markDirty();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.hasCustomName() ? this.furnaceCustomName : "container.compactor";
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
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityCompactor.class, new String[] {"Items"}));
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.compactorItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.compactorItemStacks);
        this.furnaceBurnTime = compound.getInteger("BurnTime");
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.compactorItemStacks.get(1));

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
        ItemStackHelper.saveAllItems(compound, this.compactorItemStacks);

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

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning())
        {
            --this.furnaceBurnTime;
        }

        if (!this.world.isRemote)
        {
            ItemStack batteryStack = this.compactorItemStacks.get(1);
            TileEntity te = getOutputTE();

            if (this.isBurning() || !batteryStack.isEmpty() && !this.compactorItemStacks.get(0).isEmpty() || (te != null && (te instanceof IGenerator && ((IGenerator)te).isConnected() && ((IGenerator)te).getChargeRate() >= getConsumeRate() && ((IGenerator)te).getChargeRate() != 0)))
            {
                if (!this.isBurning() && this.canCompact())
                {
                    this.furnaceBurnTime = BASE_CONSUME_RATE;
                    this.currentItemBurnTime = this.furnaceBurnTime;

                    if (this.isBurning())
                    {
                        flag1 = true;

                        if (!batteryStack.isEmpty() && ((IBattery)batteryStack.getItem()).getCharge(batteryStack) > 0)
                        {
                            ((IBattery)batteryStack.getItem()).decreaseCharge(batteryStack);
                        }
                        else if( te instanceof IElectricityStorage)
                        {
                            ((IElectricityStorage) getOutputTE()).decreaseCharge();
                        }
                    }
                }

                if (this.isBurning() && this.canCompact())
                {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime(this.compactorItemStacks.get(0));
                        this.smeltItem();
                        flag1 = true;
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
                flag1 = true;
                BlockCompactor.setState(this.isBurning(), this.world, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }

    /**
     * Time to cook:
     * Lower number = faster.
     */
    public int getCookTime(@Nullable ItemStack stack)
    {
        return 200;
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canCompact()
    {
        if (((ItemStack)this.compactorItemStacks.get(0)).isEmpty())
        {
            return false;
        }
        else
        {
            ItemStack itemstack = CompactorRecipes.instance().getSmeltingResult(this.compactorItemStacks.get(0));

            if (itemstack.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack itemstack1 = this.compactorItemStacks.get(2);
                if (itemstack1.isEmpty())
                {
                    return true;
                }
                else if (!itemstack1.isItemEqual(itemstack))
                {
                    return false;
                }
                else if (itemstack1.getCount() + itemstack.getCount() <= this.getInventoryStackLimit() && itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize())  // Forge fix: make furnace respect stack sizes in furnace recipes
                {
                    return true;
                }
                else
                {
                    return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canCompact())
        {
            ItemStack itemstack = this.compactorItemStacks.get(0);
            ItemStack itemstack1 = CompactorRecipes.instance().getSmeltingResult(itemstack);
            ItemStack itemstack2 = this.compactorItemStacks.get(2);

            if (itemstack2.isEmpty())
            {
                this.compactorItemStacks.set(2, itemstack1.copy());
            }
            else if (itemstack2.getItem() == itemstack1.getItem())
            {
                itemstack2.grow(itemstack1.getCount());
            }

            itemstack.shrink(1);
        }
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
                    return 20;
                }
            }
            else
            {
                return 0;
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
        if (index == 2)
        {
            return false;
        }
        else if (index != 1)
        {
            return true;
        }
        else
        {
            ItemStack itemstack = this.compactorItemStacks.get(1);
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
        return "betterthanelectricity:compactor_block";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new ContainerCompactor(playerInventory, this);
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
        this.compactorItemStacks.clear();
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
    public TileEntity getOutputTE() {
        TileEntity outputTE;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            outputTE = getConnectedBlockTE(facing);

            if (outputTE != null)
            {
                if(outputTE instanceof TileEntityCable)
                {
                    return ((TileEntityCable) outputTE).getOutputTE(facing.getOpposite());
                }
            }
        }
        return null;
    }

    @Override
    public float getConsumeRate() {
        return (1.0F / (BASE_CONSUME_RATE / 20.0F));
    }

    public float getGUIChargeRatio()
    {
        ItemStack batteryStack = this.compactorItemStacks.get(1);
        if(!batteryStack.isEmpty() && batteryStack.getItem() instanceof IBattery && ((IBattery)batteryStack.getItem()).getCharge(batteryStack) > 0)
        {
            return 1.0F;
        }

        TileEntity te = getOutputTE();
        if(te != null && te instanceof IGenerator)
        {
            float chargeRatio = ((IGenerator)te).getChargeRate() / getConsumeRate();
            if(chargeRatio < 0.0F) return 0.0F;
            if(chargeRatio > 1.0F) return 1.0F;
            return chargeRatio;
        }

        return 0.0F;
    }
}