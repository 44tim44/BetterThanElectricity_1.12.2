package se.sst_55t.betterthanelectricity.block.inventory;

/**
 * Created by Timeout on 2017-08-23.
 */

import se.sst_55t.betterthanelectricity.item.ModItems;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBattery extends Slot
{
    public SlotBattery(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
        return isBattery(stack);
    }

    public int getItemStackLimit(ItemStack stack)
    {
        return isBattery(stack) ? 1 : super.getItemStackLimit(stack);
    }

    public static boolean isBattery(ItemStack stack)
    {
        return stack.getItem() == ModItems.battery;
    }
}
