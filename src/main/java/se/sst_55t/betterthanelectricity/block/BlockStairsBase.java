package se.sst_55t.betterthanelectricity.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import se.sst_55t.betterthanelectricity.BTEMod;

/**
 * Created by Timeout on 2017-09-27.
 */
public class BlockStairsBase extends BlockStairs {

    protected String name;

    protected BlockStairsBase(IBlockState modelState, String name) {
        super(modelState);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public void registerItemModel(Item itemBlock) {
        BTEMod.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(this.getRegistryName());
    }

    @Override
    public BlockStairsBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
