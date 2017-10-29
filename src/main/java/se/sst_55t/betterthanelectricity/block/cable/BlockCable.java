package se.sst_55t.betterthanelectricity.block.cable;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import se.sst_55t.betterthanelectricity.block.BlockTileEntity;
import se.sst_55t.betterthanelectricity.block.multiSocket.BlockMultiSocketIn;
import se.sst_55t.betterthanelectricity.block.multiSocket.BlockMultiSocketOut;
import se.sst_55t.betterthanelectricity.block.pulverizer.BlockPulverizer;
import se.sst_55t.betterthanelectricity.block.solarpanel.BlockSolarPanel;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2017-08-22.
 */
public class BlockCable extends BlockTileEntity<TileEntityCable>
{

    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    public int color;

    public BlockCable(String name, int color)
    {
        super(Material.CIRCUITS, name);
        this.color = color;
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)).withProperty(UP, Boolean.valueOf(false)).withProperty(DOWN, Boolean.valueOf(false)));
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state
                .withProperty(NORTH, Boolean.valueOf(this.attachesTo(worldIn, worldIn.getBlockState(pos.north()), pos.north(), EnumFacing.SOUTH)))
                .withProperty(SOUTH, Boolean.valueOf(this.attachesTo(worldIn, worldIn.getBlockState(pos.south()), pos.south(), EnumFacing.NORTH)))
                .withProperty(WEST, Boolean.valueOf(this.attachesTo(worldIn, worldIn.getBlockState(pos.west()), pos.west(), EnumFacing.EAST)))
                .withProperty(EAST, Boolean.valueOf(this.attachesTo(worldIn, worldIn.getBlockState(pos.east()), pos.east(), EnumFacing.WEST)))
                .withProperty(UP, Boolean.valueOf(this.attachesTo(worldIn, worldIn.getBlockState(pos.up()), pos.up(), EnumFacing.DOWN)))
                .withProperty(DOWN, Boolean.valueOf(this.attachesTo(worldIn, worldIn.getBlockState(pos.down()), pos.down(), EnumFacing.UP)));
    }

    public final boolean attachesTo(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing)
    {
        Block block = state.getBlock();
        if(block instanceof BlockCable && ((BlockCable) block).color == this.color) return true;
        if(block instanceof BlockSolarPanel) return true;
        if(block instanceof BlockPulverizer) return true;
        if(block instanceof BlockMultiSocketOut) return true;
        if(block instanceof BlockMultiSocketIn) return true;
        return false;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {NORTH, EAST, WEST, SOUTH, UP, DOWN});
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return 0;
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
