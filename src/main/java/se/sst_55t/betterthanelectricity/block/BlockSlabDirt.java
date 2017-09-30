package se.sst_55t.betterthanelectricity.block;

import se.sst_55t.betterthanelectricity.ModEnums;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Timeout on 2017-09-23.
 */
public class BlockSlabDirt extends BlockSlabBase {
    public BlockSlabDirt(ModEnums.BlockType type, String name) {
        super(type, name);
        setHardness(0.6F);
        this.setTickRandomly(true);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote && !isDouble()) {
            IBlockState blockThis = worldIn.getBlockState(pos);
            IBlockState blockBelow = worldIn.getBlockState(pos.down());

            // Transforms block below to dirt if it's a grassblock
            if (blockBelow == Blocks.GRASS.getDefaultState() && blockThis.getValue(HALF) == BlockSlab.EnumBlockHalf.BOTTOM) {
                worldIn.setBlockState(pos.down(), Blocks.DIRT.getDefaultState());
            }

            // Transforms this block to snowy grass slab if it's snowing
            if (worldIn.isRaining() && worldIn.getBiome(pos).isSnowyBiome() && worldIn.canBlockSeeSky(pos.offset(EnumFacing.UP))) {
                if (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
                    worldIn.setBlockState(pos, ModBlocks.grass_snowed_slab.getDefaultState().withProperty(HALF, BlockSlab.EnumBlockHalf.TOP));
                } else {
                    worldIn.setBlockState(pos, ModBlocks.grass_snowed_slab.getDefaultState().withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM));
                }
            }
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.isDouble())
        {
            worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
        }
    }

    @Override
    public BlockSlabDirt setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
