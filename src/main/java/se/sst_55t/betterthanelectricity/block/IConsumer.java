package se.sst_55t.betterthanelectricity.block;

import net.minecraft.tileentity.TileEntity;

/**
 * Created by Timeout on 2017-10-27.
 */
public interface IConsumer {

    TileEntity getOutputTE();

    float getConsumeRate();

}
