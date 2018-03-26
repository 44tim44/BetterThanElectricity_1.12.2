package se.sst_55t.betterthanelectricity.block.windmill;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.block.BlockTileEntity;
import se.sst_55t.betterthanelectricity.block.quarry.BlockQuarry;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2018-03-25.
 */
public class BlockWindWheel extends BlockTileEntity
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockWindWheel(Material material, String name)
    {
        super(material, name);
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for(EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            Block block = worldIn.getBlockState(pos.offset(enumfacing)).getBlock();

            if (block instanceof BlockWindMill)
            {
                EnumFacing windMillFacing = worldIn.getBlockState(pos.offset(enumfacing)).getValue(BlockWindMill.FACING);
                return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos, windMillFacing);
            }
        }
        return false;
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!this.canBlockStay(worldIn, pos, worldIn.getBlockState(pos).getValue(BlockWindWheel.FACING) ))
        {
            worldIn.destroyBlock(pos, true);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, EnumFacing facing)
    {
        //EnumFacing facing = worldIn.getBlockState(pos).getValue(BlockWindWheel.FACING);
        switch(facing) {
            case NORTH:
                if (    worldIn.getBlockState(pos.east()        ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.west()        ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.up()          ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.down()        ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.east().up()   ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.east().down() ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.west().up()   ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.west().down() ).getBlock() != Blocks.AIR)
                {
                    return false;
                }
                if(!(worldIn.getBlockState(pos.south()).getBlock() instanceof BlockWindMill))
                {
                    return false;
                }
                break;
            case SOUTH:
                if (    worldIn.getBlockState(pos.east()        ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.west()        ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.up()          ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.down()        ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.east().up()   ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.east().down() ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.west().up()   ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.west().down() ).getBlock() != Blocks.AIR)
                {
                    System.out.println("Surrounding blocks != Air");
                    return false;
                }
                if(!(worldIn.getBlockState(pos.north()).getBlock() instanceof BlockWindMill))
                {
                    System.out.println("Block North is not Windmill");
                    return false;
                }
                break;
            case EAST:
                if (    worldIn.getBlockState(pos.north()       ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.south()       ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.up()          ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.down()        ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.north().up()  ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.north().down()).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.south().up()  ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.south().down()).getBlock() != Blocks.AIR)
                {
                    return false;
                }
                if(!(worldIn.getBlockState(pos.west()).getBlock() instanceof BlockWindMill))
                {
                    return false;
                }
                break;
            case WEST:
                if (    worldIn.getBlockState(pos.north()       ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.south()       ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.up()          ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.down()        ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.north().up()  ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.north().down()).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.south().up()  ).getBlock() != Blocks.AIR ||
                        worldIn.getBlockState(pos.south().down()).getBlock() != Blocks.AIR)
                {
                    return false;
                }
                if(!(worldIn.getBlockState(pos.east()).getBlock() instanceof BlockWindMill))
                {
                    return false;
                }
                break;
        }
        return true;
    }

    @Override
    public Class<TileEntityWindWheel> getTileEntityClass()
    {
        return TileEntityWindWheel.class;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityWindWheel();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        EnumFacing enumfacing = facing.getOpposite();
        return this.getDefaultState().withProperty(FACING, facing);
        /*
        switch (placer.getHorizontalFacing()){
            case NORTH:
            default:
                return iblockstate.withProperty(FACING, EnumFacing.SOUTH);
            case SOUTH:
                return iblockstate.withProperty(FACING, EnumFacing.NORTH);
            case EAST:
                return iblockstate.withProperty(FACING, EnumFacing.WEST);
            case WEST:
                return iblockstate.withProperty(FACING, EnumFacing.EAST);
        }
        */
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();
        switch (meta){
            case 0:
                return iblockstate.withProperty(FACING, EnumFacing.DOWN);
            case 1:
                return iblockstate.withProperty(FACING, EnumFacing.UP);
            case 2:
                return iblockstate.withProperty(FACING, EnumFacing.NORTH);
            case 3:
                return iblockstate.withProperty(FACING, EnumFacing.SOUTH);
            case 4:
                return iblockstate.withProperty(FACING, EnumFacing.WEST);
            case 5:
                return iblockstate.withProperty(FACING, EnumFacing.EAST);
        }
        return iblockstate.withProperty(FACING, EnumFacing.NORTH);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        switch ((EnumFacing)state.getValue(FACING)){
            case DOWN:
                return 0;
            case UP:
                return 1;
            case NORTH:
                return 2;
            case SOUTH:
                return 3;
            case WEST:
                return 4;
            case EAST:
                return 5;
        }
        return 2;
    }
}
