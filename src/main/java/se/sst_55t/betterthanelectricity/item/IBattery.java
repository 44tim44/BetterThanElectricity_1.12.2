package se.sst_55t.betterthanelectricity.item;

import net.minecraft.item.ItemStack;

/**
 * Created by Timeout on 2017-10-01.
 */
public interface IBattery {
    public void decreaseCharge(ItemStack stack);

    public void increaseCharge(ItemStack stack);

    public void setCharge(int value, ItemStack stack);

    public int getCharge(ItemStack stack);

    public int getMaxCharge(ItemStack stack);
}
