package se.sst_55t.betterthanelectricity.block;

/**
 * Created by Timeout on 2017-08-20.
 */
import se.sst_55t.betterthanelectricity.BTEMod;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockHorizontalBase extends BlockHorizontal {

    protected String name;

    public BlockHorizontalBase(Material material, String name) {
        super(material);

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
    public BlockHorizontalBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}