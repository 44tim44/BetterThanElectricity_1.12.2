package se.sst_55t.betterthanelectricity.block;

/**
 * Created by Timeout on 2017-08-20.
 */
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import se.sst_55t.betterthanelectricity.item.ModItems;

import java.util.Random;

public class BlockOre extends BlockBase {

    private boolean dropsItem;

    public BlockOre(String name, boolean dropsItem) {
        super(Material.ROCK, name);

        this.dropsItem = dropsItem;
        setHardness(3f);
        setResistance(5f);
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        if (!dropsItem)
        {
            return super.getItemDropped(state, rand, fortune);
        }
        else
        {
            if (this == ModBlocks.oreRuby)
            {
                return ModItems.ruby;
            }
            else if (this == ModBlocks.oreSapphire)
            {
                return ModItems.sapphire;
            }
        }
        return super.getItemDropped(state, rand, fortune);
    }

    @Override
    public BlockOre setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}