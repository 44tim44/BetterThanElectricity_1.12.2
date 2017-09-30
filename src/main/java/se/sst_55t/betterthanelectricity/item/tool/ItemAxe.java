package se.sst_55t.betterthanelectricity.item.tool;

import se.sst_55t.betterthanelectricity.BTEMod;


/**
 * Created by Timmy on 2016-11-26.
 */
public class ItemAxe extends net.minecraft.item.ItemAxe{

    private String name;

    public ItemAxe(ToolMaterial material, String name) {
        super(material, 8f, -3.1f);
        setRegistryName(name);
        setUnlocalizedName(name);
        this.name = name;
    }

    public void registerItemModel() {
        BTEMod.proxy.registerItemRenderer(this, 0, name);
    }

}