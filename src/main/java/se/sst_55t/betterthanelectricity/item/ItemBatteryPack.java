package se.sst_55t.betterthanelectricity.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.BTEMod;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Timeout on 2017-08-22.
 */
public class ItemBatteryPack extends ItemBattery implements IBattery{

    private static final int maxCharge = 1280;

    public ItemBatteryPack() {
        setUnlocalizedName("battery_pack");
        setRegistryName("battery_pack");
        setMaxStackSize(1);
        setMaxDamage(1300);
        setCreativeTab(CreativeTabs.MISC);
    }

    public void registerItemModel() {
        BTEMod.proxy.registerItemRenderer(this, 0, "battery_pack");
    }
}
