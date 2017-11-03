package se.sst_55t.betterthanelectricity.block.inventory;

/**
 * Created by Timeout on 2017-08-23.
 */

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import se.sst_55t.betterthanelectricity.item.IBattery;

public class SlotWaterBucket extends Slot
{
    public SlotWaterBucket(IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
        return isWaterBucket(stack);
    }

    public int getItemStackLimit(ItemStack stack)
    {
        return 1;
    }

    public static boolean isWaterBucket(ItemStack stack)
    {
        return stack.getItem() == Items.WATER_BUCKET;
    }
}
