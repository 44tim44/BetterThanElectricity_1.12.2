package se.sst_55t.betterthanelectricity.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import se.sst_55t.betterthanelectricity.BTEMod;

/**
 * Created by Timeout on 2017-08-20.
 */
public class ItemFoodBase extends ItemFood {
    protected String name;

    public ItemFoodBase(String name, int healamount, float saturation, boolean isWolfFood) {
        super(healamount,saturation, isWolfFood);
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public void registerItemModel() {
        BTEMod.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public ItemFoodBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}
