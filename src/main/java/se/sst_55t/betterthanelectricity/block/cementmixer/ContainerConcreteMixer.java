package se.sst_55t.betterthanelectricity.block.cementmixer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.block.inventory.SlotWaterBucket;
import se.sst_55t.betterthanelectricity.recipe.ConcreteMixerRecipes;

public class ContainerConcreteMixer extends Container
{
    private final IInventory tileConcreteMixer;
    private int cookTime;
    private int totalCookTime;
    private int furnaceBurnTime;
    private int currentItemBurnTime;

    public ContainerConcreteMixer(InventoryPlayer playerInventory, IInventory concreteMixerInventory)
    {
        this.tileConcreteMixer = concreteMixerInventory;
        this.addSlotToContainer(new Slot(concreteMixerInventory, 0, 56, 17));
        this.addSlotToContainer(new SlotWaterBucket(concreteMixerInventory, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, concreteMixerInventory, 2, 116, 35));

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
        listener.sendAllWindowProperties(this, this.tileConcreteMixer);
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

            if (this.cookTime != this.tileConcreteMixer.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileConcreteMixer.getField(2));
            }

            if (this.furnaceBurnTime != this.tileConcreteMixer.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileConcreteMixer.getField(0));
            }

            if (this.currentItemBurnTime != this.tileConcreteMixer.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileConcreteMixer.getField(1));
            }

            if (this.totalCookTime != this.tileConcreteMixer.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileConcreteMixer.getField(3));
            }
        }

        this.cookTime = this.tileConcreteMixer.getField(2);
        this.furnaceBurnTime = this.tileConcreteMixer.getField(0);
        this.currentItemBurnTime = this.tileConcreteMixer.getField(1);
        this.totalCookTime = this.tileConcreteMixer.getField(3);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileConcreteMixer.setField(id, data);
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileConcreteMixer.isUsableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack clickedStack = slot.getStack();
            itemstack = clickedStack.copy();

            if (index == 2)
            {
                if (!this.mergeItemStack(clickedStack, 3, 39, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(clickedStack, itemstack);
            }
            else if (index != 1 && index != 0)
            {
                if (!ConcreteMixerRecipes.instance().getSmeltingResult(clickedStack).isEmpty())
                {
                    if (!this.mergeItemStack(clickedStack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (TileEntityConcreteMixer.isItemFuel(clickedStack))
                {
                    if (!this.mergeItemStack(clickedStack, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(clickedStack, 30, 39, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(clickedStack, 3, 30, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(clickedStack, 3, 39, false))
            {
                return ItemStack.EMPTY;
            }

            if (clickedStack.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (clickedStack.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, clickedStack);
        }

        return itemstack;
    }
}