package se.sst_55t.betterthanelectricity.block;

import se.sst_55t.betterthanelectricity.ModEnums.BlockType;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Created by Timeout on 2017-09-21.
 */
public class BlockSlabGrass extends BlockSlabBase {

    public static final PropertyBool SNOWY = PropertyBool.create("snowy");

    public BlockSlabGrass(BlockType type, String name) {
        super(type, name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, Boolean.valueOf(false)));
        this.setTickRandomly(true);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        return state.withProperty(SNOWY, Boolean.valueOf(block == Blocks.SNOW || block == Blocks.SNOW_LAYER));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote && !isDouble())
        {
            IBlockState blockThis = worldIn.getBlockState(pos);
            IBlockState blockAbove = worldIn.getBlockState(pos.up());
            IBlockState blockBelow = worldIn.getBlockState(pos.down());
            // Transforms block below to dirt if it's a grassblock
            if(blockBelow == Blocks.GRASS.getDefaultState() && blockThis.getValue(HALF) == BlockSlab.EnumBlockHalf.BOTTOM)
            {
                worldIn.setBlockState(pos.down(),Blocks.DIRT.getDefaultState());
            }
            // Transforms this block to snowy grass slab if it's snowing
            if(worldIn.isRaining() && worldIn.getBiome(pos).isSnowyBiome() && worldIn.canBlockSeeSky(pos.offset(EnumFacing.UP)))
            {
                if(state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
                    worldIn.setBlockState(pos, ModBlocks.grass_snowed_slab.getDefaultState().withProperty(HALF,BlockSlab.EnumBlockHalf.TOP));
                }
                else
                {
                    worldIn.setBlockState(pos, ModBlocks.grass_snowed_slab.getDefaultState().withProperty(HALF,BlockSlab.EnumBlockHalf.BOTTOM));
                }
            }
            // Transforms this block to dirt slab if no light from sky
            if (worldIn.getLightFromNeighbors(pos.up()) < 4 && blockAbove.getLightOpacity(worldIn, pos.up()) > 2)
            {
                if(worldIn.getBlockState(pos).getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)
                {
                    worldIn.setBlockState(pos, ModBlocks.dirt_slab.getDefaultState().withProperty(HALF,BlockSlab.EnumBlockHalf.TOP));
                }
                else
                {
                    worldIn.setBlockState(pos, ModBlocks.dirt_slab.getDefaultState().withProperty(HALF,BlockSlab.EnumBlockHalf.BOTTOM));
                }
            }
            // Transforms this block to dirt slab if block above is a slab
            else if (blockAbove.getBlock() instanceof BlockSlabBase && blockAbove.getValue(HALF)== BlockSlab.EnumBlockHalf.BOTTOM)
            {
                if(worldIn.getBlockState(pos).getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)
                {
                    worldIn.setBlockState(pos, ModBlocks.dirt_slab.getDefaultState().withProperty(HALF,BlockSlab.EnumBlockHalf.TOP));
                }
            }
            // Grass Spread to surrounding blocks
            else
            {
                if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
                {
                    for (int i = 0; i < 4; ++i)
                    {
                        BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

                        if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos))
                        {
                            return;
                        }

                        IBlockState iblockstate = worldIn.getBlockState(blockpos.up());
                        IBlockState iblockstate1 = worldIn.getBlockState(blockpos);

                        if (iblockstate1.getBlock() == Blocks.DIRT && iblockstate1.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT && worldIn.getLightFromNeighbors(blockpos.up()) >= 4 && iblockstate.getLightOpacity(worldIn, pos.up()) <= 2)
                        {
                            worldIn.setBlockState(blockpos, Blocks.GRASS.getDefaultState());
                        }
                        else if (iblockstate1.getBlock() == ModBlocks.dirt_slab  && worldIn.getLightFromNeighbors(blockpos.up()) >= 4 && iblockstate.getLightOpacity(worldIn, pos.up()) <= 2)
                        {
                            if(iblockstate1.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
                                worldIn.setBlockState(blockpos, ModBlocks.grass_slab.getDefaultState().withProperty(HALF,BlockSlab.EnumBlockHalf.TOP));
                            }
                            else
                            {
                                worldIn.setBlockState(blockpos, ModBlocks.grass_slab.getDefaultState().withProperty(HALF,BlockSlab.EnumBlockHalf.BOTTOM));
                            }
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState().withProperty(SNOWY, Boolean.valueOf((meta & 7)!=0));
        if (!this.isDouble())
        {
            iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return iblockstate;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        if(state.getValue(SNOWY)){i = 1;}

        if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)
        {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        return this.isDouble() ? new BlockStateContainer(this, new IProperty[] {SNOWY}) : new BlockStateContainer(this, new IProperty[] {HALF, SNOWY});
    }

    @Override
    public BlockSlabGrass setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    /**
     * Sets how many hits it takes to break a block.
     */
    @Override
    public BlockSlabGrass setHardness(float hardness)
    {
        this.blockHardness = hardness;

        if (this.blockResistance < hardness * 5.0F)
        {
            this.blockResistance = hardness * 5.0F;
        }

        return this;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.isDouble())
        {
            worldIn.setBlockState(pos,Blocks.GRASS.getDefaultState());
        }
    }
}