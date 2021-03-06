package se.sst_55t.betterthanelectricity.item.tool;

import se.sst_55t.betterthanelectricity.BTEMod;

/**
 * Created by Timmy on 2016-11-26.
 */
public class ItemShovel extends net.minecraft.item.ItemSpade {

    private String name;

    public ItemShovel(ToolMaterial material, String name) {
        super(material);
        setRegistryName(name);
        setUnlocalizedName(name);
        this.name = name;
    }

    public void registerItemModel() {
        BTEMod.proxy.registerItemRenderer(this, 0, name);
    }

}