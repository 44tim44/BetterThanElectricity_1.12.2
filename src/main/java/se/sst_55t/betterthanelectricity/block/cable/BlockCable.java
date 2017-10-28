package se.sst_55t.betterthanelectricity.block.cable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.block.BlockBase;
import net.minecraft.block.material.Material;
import se.sst_55t.betterthanelectricity.block.BlockTileEntity;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2017-08-22.
 */
public class BlockCable extends BlockTileEntity<TileEntityCable> {
    public BlockCable()
    {
        super(Material.CIRCUITS, "block_cable");
    }

    @Override
    public Class<TileEntityCable> getTileEntityClass() {
        return TileEntityCable.class;
    }

    @Nullable
    @Override
    public TileEntityCable createTileEntity(World world, IBlockState state) {
        return new TileEntityCable();
    }
}
