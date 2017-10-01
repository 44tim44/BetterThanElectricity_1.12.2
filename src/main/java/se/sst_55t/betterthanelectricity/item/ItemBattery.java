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
public class ItemBattery extends Item {

    private static final int maxCharge = 320;

    public ItemBattery() {
        setUnlocalizedName("battery");
        setRegistryName("battery");
        setMaxStackSize(1);
        setMaxDamage(650);
        setCreativeTab(CreativeTabs.MISC);
    }

    public void decreaseCharge(ItemStack stack){
        this.setCharge(this.getDamage(stack)-1,stack);
    }

    public void increaseCharge(ItemStack stack){

        this.setCharge(this.getDamage(stack)+1,stack);
    }

    public void setCharge(int value, ItemStack stack)
    {
        if(value > this.maxCharge)
        {
            this.setDamage(stack,maxCharge);
        }
        else if(value < 0)
        {
            this.setDamage(stack,0);
        }
        else
        {
            this.setDamage(stack,value);
        }
    }

    public int getCharge(ItemStack stack){
        return this.getDamage(stack);
    }

    public int getMaxCharge(ItemStack stack){
        return maxCharge;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("Charge: " + getCharge(stack) + "/" + maxCharge);
    }


    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 -((double)getCharge(stack) / (double)maxCharge);
    }

    public void registerItemModel() {
        BTEMod.proxy.registerItemRenderer(this, 0, "battery");
    }
}
