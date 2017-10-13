package se.sst_55t.betterthanelectricity.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import se.sst_55t.betterthanelectricity.ModEnums;

import java.util.Random;

/**
 * Created by Timeout on 2017-09-21.
 */
public class BlockSlabDoubleGrassPath extends BlockSlabGrassPath {

    public BlockSlabDoubleGrassPath(String name) {
        super(name);
    }

    @Override
    public boolean isDouble() {
        return true;
    }

}