package se.sst_55t.betterthanelectricity.block.multiSocket;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.block.BlockTileEntity;
import se.sst_55t.betterthanelectricity.block.cable.TileEntityCable;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2017-08-22.
 */
public class BlockMultiSocketIn extends BlockTileEntity<TileEntityMultiSocketIn> {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockMultiSocketIn()
    {
        super(Material.CIRCUITS, "block_multi_socket_in");
    }

    @Override
    public Class<TileEntityMultiSocketIn> getTileEntityClass() {
        return TileEntityMultiSocketIn.class;
    }

    @Nullable
    @Override
    public TileEntityMultiSocketIn createTileEntity(World world, IBlockState state) {
        return new TileEntityMultiSocketIn();
    }
}
