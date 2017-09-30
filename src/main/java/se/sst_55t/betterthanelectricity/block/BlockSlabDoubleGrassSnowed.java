package se.sst_55t.betterthanelectricity.block;

import se.sst_55t.betterthanelectricity.ModEnums;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

/**
 * Created by Timeout on 2017-09-23.
 */
public class BlockSlabDoubleGrassSnowed extends BlockSlabGrassSnowed {

    public BlockSlabDoubleGrassSnowed(ModEnums.BlockType type, String name) {
        super(type, name);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.grass_snowed_slab);
    }

    @Override
    public boolean isDouble() {
        return true;
    }
}
