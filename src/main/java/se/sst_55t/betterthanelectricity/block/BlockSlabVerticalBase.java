package se.sst_55t.betterthanelectricity.block;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.ModEnums;
import se.sst_55t.betterthanelectricity.item.ItemVerticalSlab;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Timeout on 2017-09-24.
 */
public class BlockSlabVerticalBase extends Block {

    public static final PropertyEnum<BlockSlabVerticalBase.EnumPosition> POSITION = PropertyEnum.<BlockSlabVerticalBase.EnumPosition>create("position", BlockSlabVerticalBase.EnumPosition.class);
    public static final PropertyEnum<BlockSlabVerticalBase.EnumShape> SHAPE = PropertyEnum.<BlockSlabVerticalBase.EnumShape>create("shape", BlockSlabVerticalBase.EnumShape.class);
    protected static final AxisAlignedBB AABB_NORTH_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_SOUTH_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_EAST_HALF = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WEST_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 1.0D);

    protected static final AxisAlignedBB AABB_NORTHWEST_OUTER_CORNER = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.5D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_NORTHEAST_OUTER_CORNER = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_SOUTHWEST_OUTER_CORNER = new AxisAlignedBB(0.0D, 0.0D, 0.5D, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTHEAST_OUTER_CORNER = new AxisAlignedBB(0.5D, 0.0D, 0.5D, 1.0D, 1.0D, 1.0D);

    protected String name;
    protected final ModEnums.BlockType type;

    public BlockSlabVerticalBase(ModEnums.BlockType type, String name) {
        super(type.getMaterialType().getMaterial());
        this.setSoundType(type.getMaterialType().getSound());

        this.fullBlock = this.isDouble();
        this.name = name;
        this.type = type;
        this.useNeighborBrightness = true;
        this.setResistance(5.0F);

        setUnlocalizedName(name);
        setRegistryName(name);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? new BlockStateContainer(this) : new BlockStateContainer(this, new IProperty[] {POSITION, SHAPE}) ;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();

        switch (meta){
            case 0:
                return iblockstate;
            case 1:
                return iblockstate.withProperty(POSITION,EnumPosition.NORTH);
            case 2:
                return iblockstate.withProperty(POSITION,EnumPosition.SOUTH);
            case 3:
                return iblockstate.withProperty(POSITION,EnumPosition.EAST);
            case 4:
                return iblockstate.withProperty(POSITION,EnumPosition.WEST);
        }
        return iblockstate;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        if(!isDouble()) {
            switch (state.getValue(POSITION)) {
                case NORTH:
                    return 1;
                case SOUTH:
                    return 2;
                case EAST:
                    return 3;
                case WEST:
                    return 4;
                default:
                    return 1;
            }
        }
        else
        {
            return 0;
        }
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if(this.isDouble())
        {
            return state;
        }
        else
        {
            return state.withProperty(SHAPE, getPaneShape(state, worldIn, pos));
        }
    }

    private static BlockSlabVerticalBase.EnumShape getPaneShape(IBlockState state, IBlockAccess access, BlockPos pos)
    {
        EnumPosition enumPosition = state.getValue(POSITION);

        IBlockState iblockstate = access.getBlockState(pos.offset(enumPosition.getFacing()));
        if(isBlockPane(iblockstate))
        {
            EnumPosition enumPosition1 = iblockstate.getValue(POSITION);
            if (enumPosition1 != (state.getValue(POSITION)) && isDifferentPanes(state, access, pos, enumPosition1.getFacing().getOpposite()))
            {
                if (enumPosition1.getFacing() == enumPosition.getFacing().rotateYCCW())
                {
                    return EnumShape.OUTER_CORNER_LEFT;
                }
                return EnumShape.OUTER_CORNER_RIGHT;
            }
        }

        IBlockState iblockstate1 = access.getBlockState(pos.offset(enumPosition.getFacing().getOpposite()));
        if(isBlockPane(iblockstate1))
        {
            EnumPosition enumPosition2 = iblockstate1.getValue(POSITION);
            if (enumPosition2 != (state.getValue(POSITION)) && isDifferentPanes(state, access, pos, enumPosition2.getFacing()))
            {
                if (enumPosition2.getFacing() == enumPosition.getFacing().rotateYCCW())
                {
                    return EnumShape.INNER_CORNER_LEFT;
                }
                return EnumShape.INNER_CORNER_RIGHT;
            }
        }

        return EnumShape.STRAIGHT;
    }

    private static boolean isDifferentPanes(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing facing)
    {
        IBlockState iblockstate = access.getBlockState(pos.offset(facing));
        return !isBlockPane(iblockstate) || iblockstate.getValue(POSITION) != state.getValue(POSITION);
    }

    public static boolean isBlockPane(IBlockState state)
    {
        return state.getBlock() instanceof BlockSlabVerticalBase && !((BlockSlabVerticalBase) state.getBlock()).isDouble();
    }


    @Override
    public void addCollisionBoxToList(IBlockState iBlockState, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean flag)
    {
        IBlockState state = this.getActualState(iBlockState, worldIn, pos);
        if (((BlockSlabVerticalBase)state.getBlock()).isDouble()) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
        } else {
            EnumPosition position = state.getValue(POSITION);
            EnumShape shape = state.getValue(SHAPE);
            switch (position) {
                case NORTH:
                    switch (shape) {
                        case STRAIGHT:
                        default:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_NORTH_HALF);
                            break;
                        case OUTER_CORNER_LEFT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_NORTHWEST_OUTER_CORNER);
                            break;
                        case OUTER_CORNER_RIGHT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_NORTHEAST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_LEFT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_NORTH_HALF);
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_SOUTHWEST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_RIGHT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_NORTH_HALF);
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_SOUTHEAST_OUTER_CORNER);
                            break;
                    }
                    break;
                case SOUTH:
                    switch (shape) {
                        case STRAIGHT:
                        default:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_SOUTH_HALF);
                            break;
                        case OUTER_CORNER_LEFT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_SOUTHEAST_OUTER_CORNER);
                            break;
                        case OUTER_CORNER_RIGHT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_SOUTHWEST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_LEFT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_SOUTH_HALF);
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_NORTHEAST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_RIGHT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_SOUTH_HALF);
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_NORTHWEST_OUTER_CORNER);
                            break;
                    }
                    break;
                case EAST:
                    switch (shape) {
                        case STRAIGHT:
                        default:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_EAST_HALF);
                            break;
                        case OUTER_CORNER_LEFT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_NORTHEAST_OUTER_CORNER);
                            break;
                        case OUTER_CORNER_RIGHT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_SOUTHEAST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_LEFT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_EAST_HALF);
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_NORTHWEST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_RIGHT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_EAST_HALF);
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_SOUTHWEST_OUTER_CORNER);
                            break;
                    }
                    break;
                case WEST:
                    switch (shape) {
                        case STRAIGHT:
                        default:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_WEST_HALF);
                            break;
                        case OUTER_CORNER_LEFT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_SOUTHWEST_OUTER_CORNER);
                            break;
                        case OUTER_CORNER_RIGHT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_NORTHWEST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_LEFT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_WEST_HALF);
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_SOUTHEAST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_RIGHT:
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_WEST_HALF);
                            addCollisionBoxToList(pos, entityBox, collidingBoxes,AABB_NORTHEAST_OUTER_CORNER);
                            break;
                    }
                    break;
            }
        }
    }

    private static List<AxisAlignedBB> getCollisionBoxList(IBlockState state)
    {
        List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();
        if (((BlockSlabVerticalBase)state.getBlock()).isDouble())
        {
            list.add(FULL_BLOCK_AABB);
            return list;
        }
        else
        {
            EnumPosition position = state.getValue(POSITION);
            EnumShape shape = state.getValue(SHAPE);
            switch (position) {
                case NORTH:
                    switch (shape) {
                        case STRAIGHT:
                        default:
                            list.add(AABB_NORTH_HALF);
                            break;
                        case OUTER_CORNER_LEFT:
                            list.add(AABB_NORTHWEST_OUTER_CORNER);
                            break;
                        case OUTER_CORNER_RIGHT:
                            list.add(AABB_NORTHEAST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_LEFT:
                            list.add(AABB_NORTH_HALF);
                            list.add(AABB_SOUTHWEST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_RIGHT:
                            list.add(AABB_NORTH_HALF);
                            list.add(AABB_SOUTHEAST_OUTER_CORNER);
                            break;
                    }
                    break;
                case SOUTH:
                    switch (shape) {
                        case STRAIGHT:
                        default:
                            list.add(AABB_SOUTH_HALF);
                            break;
                        case OUTER_CORNER_LEFT:
                            list.add(AABB_SOUTHEAST_OUTER_CORNER);
                            break;
                        case OUTER_CORNER_RIGHT:
                            list.add(AABB_SOUTHWEST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_LEFT:
                            list.add(AABB_SOUTH_HALF);
                            list.add(AABB_NORTHEAST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_RIGHT:
                            list.add(AABB_SOUTH_HALF);
                            list.add(AABB_NORTHWEST_OUTER_CORNER);
                            break;
                    }
                    break;
                case EAST:
                    switch (shape) {
                        case STRAIGHT:
                        default:
                            list.add(AABB_EAST_HALF);
                            break;
                        case OUTER_CORNER_LEFT:
                            list.add(AABB_NORTHEAST_OUTER_CORNER);
                            break;
                        case OUTER_CORNER_RIGHT:
                            list.add(AABB_SOUTHEAST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_LEFT:
                            list.add(AABB_EAST_HALF);
                            list.add(AABB_NORTHWEST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_RIGHT:
                            list.add(AABB_EAST_HALF);
                            list.add(AABB_SOUTHWEST_OUTER_CORNER);
                            break;
                    }
                    break;
                case WEST:
                    switch (shape) {
                        case STRAIGHT:
                        default:
                            list.add(AABB_WEST_HALF);
                            break;
                        case OUTER_CORNER_LEFT:
                            list.add(AABB_SOUTHWEST_OUTER_CORNER);
                            break;
                        case OUTER_CORNER_RIGHT:
                            list.add(AABB_NORTHWEST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_LEFT:
                            list.add(AABB_WEST_HALF);
                            list.add(AABB_SOUTHEAST_OUTER_CORNER);
                            break;
                        case INNER_CORNER_RIGHT:
                            list.add(AABB_WEST_HALF);
                            list.add(AABB_NORTHEAST_OUTER_CORNER);
                            break;
                    }
                    break;
            }
            return list;
        }
    }


    /*
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        if (this.isDouble())
        {
            return FULL_BLOCK_AABB;
        }
        else
        {
            switch (state.getValue(POSITION)){
                case NORTH:
                    switch (state.getValue(SHAPE)) {
                        case STRAIGHT:
                            return AABB_NORTH_HALF;
                        case OUTER_CORNER_LEFT:
                            return AABB_NORTHWEST_OUTER_CORNER;
                        case OUTER_CORNER_RIGHT:
                            return AABB_NORTHEAST_OUTER_CORNER;
                        case INNER_CORNER_LEFT:
                            return FULL_BLOCK_AABB;
                        case INNER_CORNER_RIGHT:
                            return FULL_BLOCK_AABB;
                    }
                case SOUTH:
                    switch (state.getValue(SHAPE)) {
                        case STRAIGHT:
                            return AABB_SOUTH_HALF;
                        case OUTER_CORNER_LEFT:
                            return AABB_SOUTHEAST_OUTER_CORNER;
                        case OUTER_CORNER_RIGHT:
                            return AABB_SOUTHWEST_OUTER_CORNER;
                        case INNER_CORNER_LEFT:
                            return FULL_BLOCK_AABB;
                        case INNER_CORNER_RIGHT:
                            return FULL_BLOCK_AABB;
                    }
                case EAST:
                    switch (state.getValue(SHAPE)) {
                        case STRAIGHT:
                            return AABB_EAST_HALF;
                        case OUTER_CORNER_LEFT:
                            return AABB_NORTHEAST_OUTER_CORNER;
                        case OUTER_CORNER_RIGHT:
                            return AABB_SOUTHEAST_OUTER_CORNER;
                        case INNER_CORNER_LEFT:
                            return FULL_BLOCK_AABB;
                        case INNER_CORNER_RIGHT:
                            return FULL_BLOCK_AABB;
                    }
                case WEST:
                    switch (state.getValue(SHAPE)) {
                        case STRAIGHT:
                            return AABB_WEST_HALF;
                        case OUTER_CORNER_LEFT:
                            return AABB_SOUTHWEST_OUTER_CORNER;
                        case OUTER_CORNER_RIGHT:
                            return AABB_NORTHWEST_OUTER_CORNER;
                        case INNER_CORNER_LEFT:
                            return FULL_BLOCK_AABB;
                        case INNER_CORNER_RIGHT:
                            return FULL_BLOCK_AABB;
                    }
            }
        }
        return AABB_NORTH_HALF;
    }
    */

    /**
     * Return an AABB (in world coords!) that should be highlighted when the player is targeting this Block
     */
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
    {
        if (this.isDouble())
        {
            return FULL_BLOCK_AABB.offset(pos);
        }
        else
        {
            state = this.getActualState(state,worldIn,pos);
            switch (state.getValue(POSITION)){
                case NORTH:
                    switch (state.getValue(SHAPE)) {
                        case STRAIGHT:
                            return AABB_NORTH_HALF.offset(pos);
                        case OUTER_CORNER_LEFT:
                            return AABB_NORTHWEST_OUTER_CORNER.offset(pos);
                        case OUTER_CORNER_RIGHT:
                            return AABB_NORTHEAST_OUTER_CORNER.offset(pos);
                        case INNER_CORNER_LEFT:
                            return FULL_BLOCK_AABB.offset(pos);
                        case INNER_CORNER_RIGHT:
                            return FULL_BLOCK_AABB.offset(pos);
                    }
                    break;
                case SOUTH:
                    switch (state.getValue(SHAPE)) {
                        case STRAIGHT:
                            return AABB_SOUTH_HALF.offset(pos);
                        case OUTER_CORNER_LEFT:
                            return AABB_SOUTHEAST_OUTER_CORNER.offset(pos);
                        case OUTER_CORNER_RIGHT:
                            return AABB_SOUTHWEST_OUTER_CORNER.offset(pos);
                        case INNER_CORNER_LEFT:
                            return FULL_BLOCK_AABB.offset(pos);
                        case INNER_CORNER_RIGHT:
                            return FULL_BLOCK_AABB.offset(pos);
                    }
                    break;
                case EAST:
                    switch (state.getValue(SHAPE)) {
                        case STRAIGHT:
                            return AABB_EAST_HALF.offset(pos);
                        case OUTER_CORNER_LEFT:
                            return AABB_NORTHEAST_OUTER_CORNER.offset(pos);
                        case OUTER_CORNER_RIGHT:
                            return AABB_SOUTHEAST_OUTER_CORNER.offset(pos);
                        case INNER_CORNER_LEFT:
                            return FULL_BLOCK_AABB.offset(pos);
                        case INNER_CORNER_RIGHT:
                            return FULL_BLOCK_AABB.offset(pos);
                    }
                    break;
                case WEST:
                    switch (state.getValue(SHAPE)) {
                        case STRAIGHT:
                            return AABB_WEST_HALF.offset(pos);
                        case OUTER_CORNER_LEFT:
                            return AABB_SOUTHWEST_OUTER_CORNER.offset(pos);
                        case OUTER_CORNER_RIGHT:
                            return AABB_NORTHWEST_OUTER_CORNER.offset(pos);
                        case INNER_CORNER_LEFT:
                            return FULL_BLOCK_AABB.offset(pos);
                        case INNER_CORNER_RIGHT:
                            return FULL_BLOCK_AABB.offset(pos);

                    }
                    break;
            }
        }
        return AABB_NORTH_HALF.offset(pos);
    }

    /**
     * Determines if the block is solid enough on the top side to support other blocks, like redstone components.
     */
    public boolean isTopSolid(IBlockState state)
    {
        return ((BlockSlabVerticalBase)state.getBlock()).isDouble();
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess access, IBlockState state, BlockPos pos, EnumFacing facing)
    {
        if (((BlockSlabVerticalBase)state.getBlock()).isDouble())
        {
            return BlockFaceShape.SOLID;
        }
        else if (facing == EnumFacing.UP || facing == EnumFacing.DOWN)
        {
            return BlockFaceShape.UNDEFINED;
        }
        else
        {
            switch (state.getValue(POSITION))
            {
                case NORTH:
                    if(facing == EnumFacing.NORTH)
                    {
                        return BlockFaceShape.SOLID;
                    }
                case SOUTH:
                    if(facing == EnumFacing.SOUTH)
                    {
                        return BlockFaceShape.SOLID;
                    }
                case EAST:
                    if(facing == EnumFacing.EAST)
                    {
                        return BlockFaceShape.SOLID;
                    }
                case WEST:
                    if(facing == EnumFacing.WEST)
                    {
                        return BlockFaceShape.SOLID;
                    }
            }
            return BlockFaceShape.UNDEFINED;
        }
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return this.isDouble();
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return this.isDouble();
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (this.isDouble())
        {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
        else if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(blockState, blockAccess, pos, side))
        {
            return false;
        }
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        if (net.minecraftforge.common.ForgeModContainer.disableStairSlabCulling)
        {
            return super.doesSideBlockRendering(state, world, pos, face);
        }

        if (state.isOpaqueCube())
        {
            return true;
        }

        EnumPosition side = state.getValue(POSITION);
        switch (side){
            case NORTH:
                return face == EnumFacing.NORTH;
            case SOUTH:
                return face == EnumFacing.SOUTH;
            case EAST:
                return face == EnumFacing.EAST;
            case WEST:
                return face == EnumFacing.WEST;
        }
        return isDouble();
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = this.getStateFromMeta(meta);

        if (this.isDouble())
        {
            return iblockstate;
        }
        else
        {
            switch (facing) {
                case NORTH:
                    return iblockstate.withProperty(POSITION, EnumPosition.SOUTH);
                case SOUTH:
                    return iblockstate.withProperty(POSITION, EnumPosition.NORTH);
                case EAST:
                    return iblockstate.withProperty(POSITION, EnumPosition.WEST);
                case WEST:
                    return iblockstate.withProperty(POSITION, EnumPosition.EAST);
                default:
                    switch (placer.getHorizontalFacing()){
                        case NORTH:
                            return iblockstate.withProperty(POSITION, EnumPosition.NORTH);
                        case SOUTH:
                            return iblockstate.withProperty(POSITION, EnumPosition.SOUTH);
                        case EAST:
                            return iblockstate.withProperty(POSITION, EnumPosition.EAST);
                        case WEST:
                            return iblockstate.withProperty(POSITION, EnumPosition.WEST);
                    }
            }
        }
        return iblockstate.withProperty(POSITION, EnumPosition.NORTH);
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

    public Comparable<?> getTypeForItem(ItemStack stack) {
        return null;
    }

    public String getUnlocalizedName(int meta) {
        return this.getUnlocalizedName();
    }

    public IProperty<?> getVariantProperty() {
        return null;
    }

    public boolean isDouble() {
        return false;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return ModEnums.FireType.SLAB.getFireSpread();
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return ModEnums.FireType.SLAB.getFlamability();
    }

    public void registerItemModel(Item itemBlock) {
        BTEMod.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemSlab(BlockSlabVerticalBase block, BlockSlabVerticalBase singleSlab, BlockSlabVerticalBase doubleSlab) {
        return new ItemVerticalSlab(this,singleSlab,doubleSlab).setRegistryName(this.getRegistryName());
    }

    @Override
    public BlockSlabVerticalBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    /**
     * Sets how many hits it takes to break a block.
     */
    @Override
    public BlockSlabVerticalBase setHardness(float hardness)
    {
        this.blockHardness = hardness;

        if (this.blockResistance < hardness * 5.0F)
        {
            this.blockResistance = hardness * 5.0F;
        }

        return this;
    }

    public static enum EnumPosition implements IStringSerializable
    {
        NORTH("north"),
        SOUTH("south"),
        EAST("east"),
        WEST("west");

        private final String name;

        private EnumPosition(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }

        public EnumPosition rotateYCCW(){
            switch (this) {
                case NORTH: return WEST;
                case EAST: return NORTH;
                case SOUTH: return EAST;
                case WEST: return SOUTH;
                default: throw new IllegalStateException("Unable to get CCW Position of " + this);
            }
        }

        public EnumPosition rotateY()
        {
            switch (this) {
                case NORTH: return EAST;
                case EAST: return SOUTH;
                case SOUTH: return WEST;
                case WEST: return NORTH;
                default: throw new IllegalStateException("Unable to get Y-rotated Position of " + this);
            }
        }

        public EnumFacing getFacing(){
            switch(this)
            {
                case NORTH: return EnumFacing.NORTH;
                case SOUTH: return EnumFacing.SOUTH;
                case EAST: return EnumFacing.EAST;
                case WEST: return EnumFacing.WEST;
                default: throw new IllegalStateException("Unable to get facing of " + this);
            }
        }
    }

    public static enum EnumShape implements IStringSerializable
    {
        STRAIGHT("straight"),
        INNER_CORNER_LEFT("inner_corner_left"),
        INNER_CORNER_RIGHT("inner_corner_right"),
        OUTER_CORNER_LEFT("outer_corner_left"),
        OUTER_CORNER_RIGHT("outer_corner_right");

        private final String name;

        private EnumShape(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }

    }
}
