package se.sst_55t.betterthanelectricity.block.multiSocket;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.block.BlockTileEntity;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2017-08-22.
 */
public class BlockMultiSocketOut extends BlockTileEntity<TileEntityMultiSocketOut> {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockMultiSocketOut()
    {
        super(Material.CIRCUITS, "block_multi_socket_out");
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    @Override
    public Class<TileEntityMultiSocketOut> getTileEntityClass() {
        return TileEntityMultiSocketOut.class;
    }

    @Nullable
    @Override
    public TileEntityMultiSocketOut createTileEntity(World world, IBlockState state) {
        return new TileEntityMultiSocketOut();
    }
}
