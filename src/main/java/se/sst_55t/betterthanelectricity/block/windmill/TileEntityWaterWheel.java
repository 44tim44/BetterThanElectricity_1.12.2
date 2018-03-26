package se.sst_55t.betterthanelectricity.block.windmill;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Created by Timeout on 2018-03-25.
 */
public class TileEntityWaterWheel extends TileEntity implements ITickable
{
    private int counter = 0;

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos().add(-1, -1, -1), getPos().add(1, 1, 1));
    }

    @Override
    public void update()
    {
        counter++;

        if(counter % 4 == 0)
        {
            IBlockState iblockstate = world.getBlockState(getPos());
            BlockWaterWheel block = (BlockWaterWheel) iblockstate.getBlock();

            if (!block.canBlockStay(world, getPos(), world.getBlockState(getPos()).getValue(BlockWaterWheel.FACING)))
            {
                world.destroyBlock(getPos(), true);
            }
        }
    }
}
