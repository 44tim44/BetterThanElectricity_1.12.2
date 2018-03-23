package se.sst_55t.betterthanelectricity.block.cable;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import se.sst_55t.betterthanelectricity.block.BlockTileEntity;
import se.sst_55t.betterthanelectricity.block.chargingstation.BlockChargingStation;
import se.sst_55t.betterthanelectricity.block.compactor.BlockCompactor;
import se.sst_55t.betterthanelectricity.block.electricfurnace.BlockElectricFurnace;
import se.sst_55t.betterthanelectricity.block.fuelgenerator.BlockFuelGenerator;
import se.sst_55t.betterthanelectricity.block.multiSocket.BlockMultiSocketIn;
import se.sst_55t.betterthanelectricity.block.multiSocket.BlockMultiSocketOut;
import se.sst_55t.betterthanelectricity.block.pulverizer.BlockPulverizer;
import se.sst_55t.betterthanelectricity.block.quarry.BlockQuarry;
import se.sst_55t.betterthanelectricity.block.solarpanel.BlockSolarPanel;
import se.sst_55t.betterthanelectricity.block.windmill.BlockWindMill;

import javax.annotation.Nullable;
import java.util.List;

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

    //                                                                      X0           Y0          Z0         X1          Y2          Z2
    protected static final AxisAlignedBB AABB_CORE =    new AxisAlignedBB(  0.3125D,     0.3125D,    0.3125D,   0.6875D,    0.6875D,    0.6875D );
    protected static final AxisAlignedBB AABB_SOUTH =   new AxisAlignedBB(  0.3125D,     0.3125D,    0.6875D,   0.6875D,    0.6875D,    1.0D    );
    protected static final AxisAlignedBB AABB_WEST =    new AxisAlignedBB(  0.0D,        0.3125D,    0.3125D,   0.3125D,    0.6875D,    0.6875D );
    protected static final AxisAlignedBB AABB_NORTH =   new AxisAlignedBB(  0.3125D,     0.3125D,    0.0D,      0.6875D,    0.6875D,    0.3125D );
    protected static final AxisAlignedBB AABB_EAST =    new AxisAlignedBB(  0.6875D,     0.3125D,    0.3125D,   1.0D,       0.6875D,    0.6875D );
    protected static final AxisAlignedBB AABB_UP =      new AxisAlignedBB(  0.3125D,     0.6875D,    0.3125D,   0.6875D,    1.0D,       0.6875D );
    protected static final AxisAlignedBB AABB_DOWN =    new AxisAlignedBB(  0.3125D,     0.0D,       0.3125D,   0.6875D,    0.3125D,    0.6875D );

    public int color;

    public BlockCable(String name, int color)
    {
        super(Material.CIRCUITS, name);
        this.setHardness(1.0F);
        this.color = color;
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)).withProperty(UP, Boolean.valueOf(false)).withProperty(DOWN, Boolean.valueOf(false)));
    }

    @Override
    public void addCollisionBoxToList(IBlockState iBlockState, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_)
    {
        IBlockState state = this.getActualState(iBlockState, worldIn, pos);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_CORE);
        if(state.getValue(NORTH))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NORTH);
        }
        if(state.getValue(SOUTH))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SOUTH);
        }
        if(state.getValue(WEST))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WEST);
        }
        if(state.getValue(EAST))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EAST);
        }
        if(state.getValue(UP))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_UP);
        }
        if(state.getValue(DOWN))
        {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_DOWN);
        }
    }

    private static List<AxisAlignedBB> getCollisionBoxList(IBlockState state)
    {
        List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();
        list.add(AABB_CORE);
        if(state.getValue(NORTH))
        {
            list.add(AABB_NORTH);
        }
        if(state.getValue(SOUTH))
        {
            list.add(AABB_SOUTH);
        }
        if(state.getValue(WEST))
        {
            list.add(AABB_WEST);
        }
        if(state.getValue(EAST))
        {
            list.add(AABB_EAST);
        }
        if(state.getValue(UP))
        {
            list.add(AABB_UP);
        }
        if(state.getValue(DOWN))
        {
            list.add(AABB_DOWN);
        }
        return list;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {

        return FULL_BLOCK_AABB.offset(pos);
        /**
        // all sides (NWSEUD)
        if(state.getValue(NORTH) && state.getValue(SOUTH) && state.getValue(WEST) && state.getValue(EAST) && state.getValue(UP) && state.getValue(DOWN) )
        {
            return FULL_BLOCK_AABB.offset(pos);
        }

        // all except NORTH (WSEUD)
        if(!state.getValue(NORTH) && state.getValue(SOUTH) && state.getValue(WEST) && state.getValue(EAST) && state.getValue(UP) && state.getValue(DOWN) )
        {
            return new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 1.0D, 1.0D).offset(pos);
        }
        // all except SOUTH (NWEUD)
        if(state.getValue(NORTH) && !state.getValue(SOUTH) && state.getValue(WEST) && state.getValue(EAST) && state.getValue(UP) && state.getValue(DOWN) )
        {
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.6875D).offset(pos);
        }
        // all except WEST (NSEUD)
        if(state.getValue(NORTH) && state.getValue(SOUTH) && !state.getValue(WEST) && state.getValue(EAST) && state.getValue(UP) && state.getValue(DOWN) )
        {
            return new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D).offset(pos);
        }
        // all except EAST (NWSUD)
        if(state.getValue(NORTH) && state.getValue(SOUTH) && state.getValue(WEST) && !state.getValue(EAST) && state.getValue(UP) && state.getValue(DOWN) )
        {
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.6875D, 1.0D, 1.0D).offset(pos);
        }
        // all except UP (NWSED)
        if(state.getValue(NORTH) && state.getValue(SOUTH) && state.getValue(WEST) && state.getValue(EAST) && !state.getValue(UP) && state.getValue(DOWN) )
        {
            return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6875D, 1.0D).offset(pos);
        }
        // all except DOWN (NWSEU)
        if(state.getValue(NORTH) && state.getValue(SOUTH) && state.getValue(WEST) && state.getValue(EAST) && !state.getValue(UP) && state.getValue(DOWN) )
        {
            return new AxisAlignedBB(0.0D, 0.3125D, 0.0D, 1.0D, 1.0D, 1.0D).offset(pos);
        }

        // all except NORTH & SOUTH (WEUD)
        if(!state.getValue(NORTH) && !state.getValue(SOUTH) && state.getValue(WEST) && state.getValue(EAST) && state.getValue(UP) && state.getValue(DOWN) )
        {
            return new AxisAlignedBB(0.0D, 0.0D, 0.3125D, 1.0D, 1.0D, 0.6875D).offset(pos);
        }
        // all except WEST & EAST (NSUD)
        if(state.getValue(NORTH) && state.getValue(SOUTH) && !state.getValue(WEST) && !state.getValue(EAST) && state.getValue(UP) && state.getValue(DOWN) )
        {
            return new AxisAlignedBB(0.3125D, 0.0D, 0.0D, 0.6875D, 1.0D, 1.0D).offset(pos);
        }
        // all except UP & DOWN (NWSE)
        if(state.getValue(NORTH) && state.getValue(SOUTH) && state.getValue(WEST) && state.getValue(EAST) && !state.getValue(UP) && !state.getValue(DOWN) )
        {
            return new AxisAlignedBB(0.0D, 0.3125D, 0.0D, 1.0D, 0.6875D, 1.0D).offset(pos);
        }


        // only NORTH
        if(state.getValue(NORTH))
        {
            return new AxisAlignedBB(0.3125D,     0.3125D,    0.0D,      0.6875D,    0.6875D,    0.6875D).offset(pos);
        }
        // only SOUTH
        if(state.getValue(SOUTH))
        {
            return new AxisAlignedBB(0.3125D,     0.3125D,    0.3125D,      0.6875D,    0.6875D,    1.0D).offset(pos);
        }
        // only WEST
        if(state.getValue(WEST))
        {
            return new AxisAlignedBB(0.0D, 0.3125D, 0.3125D, 0.6875D, 0.6875D, 0.6875D).offset(pos);
        }
        // only EAST
        if(state.getValue(EAST))
        {
            return new AxisAlignedBB(0.3125D, 0.3125D, 0.3125D, 1.0D, 0.6875D, 0.6875D).offset(pos);
        }
        // only UP
        if(state.getValue(UP))
        {
            return new AxisAlignedBB(0.3125D, 0.3125D, 0.3125D, 0.6875D, 1.0D, 0.6875D).offset(pos);
        }
        // only DOWN
        if(state.getValue(DOWN))
        {
            return new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 0.6875D, 0.6875D).offset(pos);
        }

        // only core
        return AABB_CORE.offset(pos);
    */
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     */
    @Override
    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end)
    {
        List<RayTraceResult> list = Lists.<RayTraceResult>newArrayList();

        for (AxisAlignedBB axisalignedbb : getCollisionBoxList(this.getActualState(blockState, worldIn, pos)))
        {
            list.add(this.rayTrace(pos, start, end, axisalignedbb));
        }

        RayTraceResult raytraceresult1 = null;
        double d1 = 0.0D;

        for (RayTraceResult raytraceresult : list)
        {
            if (raytraceresult != null)
            {
                double d0 = raytraceresult.hitVec.squareDistanceTo(end);

                if (d0 > d1)
                {
                    raytraceresult1 = raytraceresult;
                    d1 = d0;
                }
            }
        }

        return raytraceresult1;
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

        // Cables
        if(block instanceof BlockCable && ((BlockCable) block).color == this.color) return true;

        // Generator Machines
        if(block instanceof BlockSolarPanel) return true;
        if(block instanceof BlockWindMill) return true;
        if(block instanceof BlockFuelGenerator) return true;

        // Consumer Machines
        if(block instanceof BlockPulverizer) return true;
        if(block instanceof BlockChargingStation) return true;
        if(block instanceof BlockElectricFurnace) return true;
        if(block instanceof BlockCompactor) return true;
        if(block instanceof BlockQuarry) return true;

        // Sockets
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
