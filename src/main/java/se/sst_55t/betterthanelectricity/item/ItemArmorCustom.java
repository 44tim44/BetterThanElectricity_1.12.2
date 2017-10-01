package se.sst_55t.betterthanelectricity.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import se.sst_55t.betterthanelectricity.BTEMod;

/**
 * Created by Timeout on 2017-10-01.
 */
public class ItemArmorCustom extends net.minecraft.item.ItemArmor {

    public String name;

    public ItemArmorCustom(ArmorMaterial materialIn, EntityEquipmentSlot slot, String name)
    {
        super(materialIn, 0, slot);
        setRegistryName(name);
        setUnlocalizedName(name);
        this.name = name;
    }

    public void registerItemModel() {
        BTEMod.proxy.registerItemRenderer(this, 0, name);
    }
}
