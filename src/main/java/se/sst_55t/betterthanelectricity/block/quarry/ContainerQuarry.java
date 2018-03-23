package se.sst_55t.betterthanelectricity.block.quarry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.block.inventory.SlotBattery;
import se.sst_55t.betterthanelectricity.item.IBattery;

public class ContainerQuarry extends Container
{
    private final IInventory tileQuarry;
    private int cookTime;
    private int totalCookTime;
    private int furnaceBurnTime;
    private int currentItemBurnTime;

    public ContainerQuarry(InventoryPlayer playerInventory, IInventory quarryInventory)
    {
        this.tileQuarry = quarryInventory;

        this.addSlotToContainer(new SlotBattery(quarryInventory, 0, 26, 53));
        for (int j = 1; j < 6; ++j)
        {
            this.addSlotToContainer(new Slot(quarryInventory, j, 62 + (j-1) * 18, 35));
        }

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileQuarry);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = (IContainerListener)this.listeners.get(i);

            if (this.cookTime != this.tileQuarry.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileQuarry.getField(2));
            }

            if (this.furnaceBurnTime != this.tileQuarry.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileQuarry.getField(0));
            }

            if (this.currentItemBurnTime != this.tileQuarry.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileQuarry.getField(1));
            }

            if (this.totalCookTime != this.tileQuarry.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileQuarry.getField(3));
            }
        }

        this.cookTime = this.tileQuarry.getField(2);
        this.furnaceBurnTime = this.tileQuarry.getField(0);
        this.currentItemBurnTime = this.tileQuarry.getField(1);
        this.totalCookTime = this.tileQuarry.getField(3);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileQuarry.setField(id, data);
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileQuarry.isUsableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 6)
            {
                if (!this.mergeItemStack(itemstack1, 6, 42, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index >= 6)
            {
                if (TileEntityQuarry.isItemFuel(itemstack1) || itemstack1.getItem() instanceof IBattery)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 6 && index < 33)
                {
                    if (!this.mergeItemStack(itemstack1, 33, 42, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 33 && index < 42 && !this.mergeItemStack(itemstack1, 6, 33, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 6, 42, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}