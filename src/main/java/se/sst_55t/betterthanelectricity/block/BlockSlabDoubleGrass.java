package se.sst_55t.betterthanelectricity.block;

import se.sst_55t.betterthanelectricity.ModEnums.BlockType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

/**
 * Created by Timeout on 2017-09-21.
 */
public class BlockSlabDoubleGrass extends BlockSlabGrass {

    public BlockSlabDoubleGrass(BlockType type, String name) {
        super(type, name);

    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.grass_slab);
    }

    @Override
    public boolean isDouble() {
        return true;
    }
}