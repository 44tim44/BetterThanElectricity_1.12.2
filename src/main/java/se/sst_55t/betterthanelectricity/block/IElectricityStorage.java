package se.sst_55t.betterthanelectricity.block;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by Timeout on 2017-10-27.
 */
public interface IElectricityStorage {

    void increaseCharge();

    void decreaseCharge();

    void setCharge(int value);

    int getCharge();

    int getMaxCharge();

}
