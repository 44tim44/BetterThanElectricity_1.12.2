package se.sst_55t.betterthanelectricity.item;

import net.minecraft.item.ItemStack;
import se.sst_55t.betterthanelectricity.BTEMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by Timeout on 2017-08-20.
 */
public class ItemBase extends Item {
    protected String name;

    public ItemBase(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public void registerItemModel() {
        BTEMod.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    public int getItemBurnTime(ItemStack itemStack) {
        if(this == ModItems.coalDust)
        {
            return 1200;
        }
        else
        {
            return super.getItemBurnTime(itemStack);
        }
    }
}
