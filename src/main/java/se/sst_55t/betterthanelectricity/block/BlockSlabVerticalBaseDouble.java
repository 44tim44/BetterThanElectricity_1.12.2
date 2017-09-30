package se.sst_55t.betterthanelectricity.block;

import se.sst_55t.betterthanelectricity.ModEnums;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by Timeout on 2017-09-24.
 */
public class BlockSlabVerticalBaseDouble extends BlockSlabVerticalBase
{
    public BlockSlabVerticalBaseDouble(ModEnums.BlockType type, String name) {
        super(type, name);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        switch (type) {
            case SMOOTHSTONE:
                return Item.getItemFromBlock(ModBlocks.smoothstone_slab);
            default:
                return Item.getItemFromBlock(Blocks.AIR);
        }
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean flag)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
    }

    @Override
    public boolean isDouble() {
        return true;
    }

    /**
     * Sets how many hits it takes to break a block.
     */
    @Override
    public BlockSlabVerticalBaseDouble setHardness(float hardness)
    {
        this.blockHardness = hardness;

        if (this.blockResistance < hardness * 5.0F)
        {
            this.blockResistance = hardness * 5.0F;
        }

        return this;
    }
}
