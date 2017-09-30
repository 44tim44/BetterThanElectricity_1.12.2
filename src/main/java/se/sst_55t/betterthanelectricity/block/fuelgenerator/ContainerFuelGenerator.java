package se.sst_55t.betterthanelectricity.block.fuelgenerator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.block.inventory.SlotBattery;
import se.sst_55t.betterthanelectricity.item.ModItems;
import se.sst_55t.betterthanelectricity.recipe.PulverizerRecipes;

public class ContainerFuelGenerator extends Container
{
    private final IInventory tileFurnace;
    private int cookTime;
    private int totalCookTime;
    private int furnaceBurnTime;
    private int currentItemBurnTime;

    public ContainerFuelGenerator(InventoryPlayer playerInventory, IInventory furnaceInventory)
    {
        this.tileFurnace = furnaceInventory;
        this.addSlotToContainer(new Slot(furnaceInventory, 0, 107, 35));
        this.addSlotToContainer(new SlotFurnaceFuel(furnaceInventory, 1, 53, 35));
        //this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, furnaceInventory, 2, 116, 35));

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
        listener.sendAllWindowProperties(this, this.tileFurnace);
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

            if (this.cookTime != this.tileFurnace.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileFurnace.getField(2));
            }

            if (this.furnaceBurnTime != this.tileFurnace.getField(0))
            {
                icontainerlistener.sendWindowProperty(this, 0, this.tileFurnace.getField(0));
            }

            if (this.currentItemBurnTime != this.tileFurnace.getField(1))
            {
                icontainerlistener.sendWindowProperty(this, 1, this.tileFurnace.getField(1));
            }

            if (this.totalCookTime != this.tileFurnace.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileFurnace.getField(3));
            }
        }

        this.cookTime = this.tileFurnace.getField(2);
        this.furnaceBurnTime = this.tileFurnace.getField(0);
        this.currentItemBurnTime = this.tileFurnace.getField(1);
        this.totalCookTime = this.tileFurnace.getField(3);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileFurnace.setField(id, data);
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileFurnace.isUsableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstackHand = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstackSlot = slot.getStack();
            itemstackHand = itemstackSlot.copy();

            if (index != 1 && index != 0)
            {
                if (itemstackSlot.getItem() == ModItems.battery || itemstackSlot.getItem() == ModItems.miningDrill)
                {
                    if (!this.mergeItemStack(itemstackSlot, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (TileEntityFuelGenerator.isItemFuel(itemstackSlot))
                {
                    if (!this.mergeItemStack(itemstackSlot, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 2 && index < 29)
                {
                    if (!this.mergeItemStack(itemstackSlot, 29, 38, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 29 && index < 38 && !this.mergeItemStack(itemstackSlot, 2, 29, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstackSlot, 2, 38, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstackSlot.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstackSlot.getCount() == itemstackHand.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstackSlot);
        }

        return itemstackHand;
    }
}