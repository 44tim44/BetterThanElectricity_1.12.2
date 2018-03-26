package se.sst_55t.betterthanelectricity.block.electriclamp;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.block.BlockBase;
import se.sst_55t.betterthanelectricity.block.BlockTileEntity;
import se.sst_55t.betterthanelectricity.block.ModBlocks;
import se.sst_55t.betterthanelectricity.item.IChargeable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by Timeout on 2017-10-04.
 */
public class BlockElectricLamp extends BlockTileEntity<TileEntityElectricLamp> //implements IChargeable
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    protected static final AxisAlignedBB AABB_UP    = new AxisAlignedBB(0.125D, 0.750D, 0.125D, 0.875D, 1.000D, 0.875D);
    protected static final AxisAlignedBB AABB_DOWN  = new AxisAlignedBB(0.125D, 0.000D, 0.125D, 0.875D, 0.250D, 0.875D);
    protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.125D, 0.125D, 0.000D, 0.875D, 0.875D, 0.250D);
    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.125D, 0.125D, 0.750D, 0.875D, 0.875D, 1.000D);
    protected static final AxisAlignedBB AABB_EAST  = new AxisAlignedBB(0.750D, 0.125D, 0.125D, 1.000D, 0.875D, 0.875D);
    protected static final AxisAlignedBB AABB_WEST  = new AxisAlignedBB(0.000D, 0.125D, 0.125D, 0.250D, 0.875D, 0.875D);

    public final boolean isOn;
    public int color;

    public BlockElectricLamp(Material material, String name, boolean isOn, int color) {
        super(material, name);
        this.isOn = isOn;
        this.color = color;

        if (isOn && color == 0)
        {
            this.setLightLevel(1.0F);
        }
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

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (FMLCommonHandler.instance().getEffectiveSide().isServer())
        {
            System.out.println("Light should be 15");
            return 15;
        }
        return super.getLightValue(state, world, pos);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (worldIn.isRemote && worldIn.getLight(pos) == 15)
        {
            worldIn.checkLightFor(EnumSkyBlock.BLOCK, pos);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING))
        {
            case UP:
            default:
                return AABB_UP;
            case DOWN:
                return AABB_DOWN;
            case NORTH:
                return AABB_NORTH;
            case SOUTH:
                return AABB_SOUTH;
            case EAST:
                return AABB_EAST;
            case WEST:
                return AABB_WEST;
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING}) ;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();

        switch (meta){
            case 0:
            default:
                return iblockstate.withProperty(FACING, EnumFacing.UP);
            case 1:
                return iblockstate.withProperty(FACING, EnumFacing.DOWN);
            case 2:
                return iblockstate.withProperty(FACING, EnumFacing.NORTH);
            case 3:
                return iblockstate.withProperty(FACING, EnumFacing.SOUTH);
            case 4:
                return iblockstate.withProperty(FACING, EnumFacing.EAST);
            case 5:
                return iblockstate.withProperty(FACING, EnumFacing.WEST);
        }

    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        switch (state.getValue(FACING)) {
            case UP:
            default:
                return 0;
            case DOWN:
                return 1;
            case NORTH:
                return 2;
            case SOUTH:
                return 3;
            case EAST:
                return 4;
            case WEST:
                return 5;
        }
    }

    public static void setState(boolean active,World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            EnumFacing facing = worldIn.getBlockState(pos).getValue(FACING);

            switch (((BlockElectricLamp)state.getBlock()).color)
            {
                case 0: // white
                    if (active)
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLamp.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    else
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLamp_on.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    break;
                case 1: // orange
                    break;
                case 2: // magenta
                    break;
                case 3: // lightblue
                    break;
                case 4: // yellow
                    if (active)
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLampYellow.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    else
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLampYellow_on.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    break;
                case 5: // lime
                    break;
                case 6: // pink
                    break;
                case 7: // gray
                    break;
                case 8: // lightgray
                    break;
                case 9: // cyan
                    break;
                case 10: // purple
                    break;
                case 11: // blue
                    if (active)
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLampBlue.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    else
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLampBlue_on.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    break;
                case 12: // brown
                    if (active)
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLampBrown.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    else
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLampBrown_on.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    break;
                case 13: // green
                    if (active)
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLampGreen.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    else
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLampGreen_on.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    break;
                case 14: // red
                    if (active)
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLampRed.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    else
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLampRed_on.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    break;
                case 15: // black
                    break;
                default:
                    if (active)
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLamp.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    else
                    {
                        worldIn.setBlockState(pos, ModBlocks.electricLamp_on.getDefaultState().withProperty(FACING,facing), 2);
                    }
                    break;
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = this.getStateFromMeta(meta);

        switch (facing) {
            case UP:
            default:
                return iblockstate.withProperty(FACING, EnumFacing.DOWN);
            case DOWN:
                return iblockstate.withProperty(FACING, EnumFacing.UP);
            case NORTH:
                return iblockstate.withProperty(FACING, EnumFacing.SOUTH);
            case SOUTH:
                return iblockstate.withProperty(FACING, EnumFacing.NORTH);
            case EAST:
                return iblockstate.withProperty(FACING, EnumFacing.WEST);
            case WEST:
                return iblockstate.withProperty(FACING, EnumFacing.EAST);
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        setState(isOn,worldIn,pos,state);
        return true;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess access, IBlockState state, BlockPos pos, EnumFacing facing)
    {
        return BlockFaceShape.UNDEFINED;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.electricLamp);
    }

    @Override
    public Class<TileEntityElectricLamp> getTileEntityClass() {
        return TileEntityElectricLamp.class;
    }

    @Nullable
    @Override
    public TileEntityElectricLamp createTileEntity(World world, IBlockState state) {
        return new TileEntityElectricLamp();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    /*
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote)
        {
            if (this.isOn )
            {
                if(worldIn.getBlockState(pos).getBlock() instanceof IChargeable){
                    ((IChargeable) worldIn.getBlockState(pos).getBlock()).decreaseCharge();
                }
            }
        }
    }
    */
    /*
    @Override
    public void decreaseCharge(ItemStack stack) {

    }

    @Override
    public void increaseCharge(ItemStack stack) {

    }

    @Override
    public void setCharge(int value, ItemStack stack) {

    }

    @Override
    public int getCharge(ItemStack stack) {
        return 0;
    }

    @Override
    public int getMaxCharge(ItemStack stack) {
        return 0;
    }
    */
}
