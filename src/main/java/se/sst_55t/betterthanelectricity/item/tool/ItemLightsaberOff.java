package se.sst_55t.betterthanelectricity.item.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.item.IChargeable;
import se.sst_55t.betterthanelectricity.item.ItemBase;
import se.sst_55t.betterthanelectricity.item.ModItems;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Timmy on 2016-11-26.
 */
public class ItemLightsaberOff extends Item implements IChargeable
{
    private String name;
    private static final int maxCharge = 320;

    public ItemLightsaberOff(String name) {
        this.name = name;
        this.setMaxDamage(400);
        setRegistryName(name);
        setUnlocalizedName(name);
        this.setCreativeTab(CreativeTabs.COMBAT);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack heldItemstack = playerIn.getHeldItem(handIn);
        if(heldItemstack.getItem() == this)
        {
            int currentCharge = ((IChargeable) heldItemstack.getItem()).getCharge(heldItemstack);
            if(!(currentCharge > 0))
            {
                return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
            }
            if (heldItemstack.getItem() == ModItems.lightsaberRedOff)
            {
                playerIn.setHeldItem(handIn, new ItemStack(ModItems.lightsaberRed));
                ((IChargeable) playerIn.getHeldItem(handIn).getItem()).setCharge(currentCharge, playerIn.getHeldItem(handIn));
            }
            else if (heldItemstack.getItem() == ModItems.lightsaberGreenOff)
            {
                playerIn.setHeldItem(handIn, new ItemStack(ModItems.lightsaberGreen));
                ((IChargeable) playerIn.getHeldItem(handIn).getItem()).setCharge(currentCharge, playerIn.getHeldItem(handIn));
            }
            else if (heldItemstack.getItem() == ModItems.lightsaberBlueOff)
            {
                playerIn.setHeldItem(handIn, new ItemStack(ModItems.lightsaberBlue));
                ((IChargeable) playerIn.getHeldItem(handIn).getItem()).setCharge(currentCharge, playerIn.getHeldItem(handIn));
            }
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }

        return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
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
        tooltip.add("");
        tooltip.add("When in main hand:");
        tooltip.add("1.6 Attack Speed");
        tooltip.add("12 Attack Damage");
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 -((double)getCharge(stack) / (double)maxCharge);
    }


    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return false;
    }

    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
    /*
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)9.0F, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.0D, 0));
        }

        return multimap;
    }
    */

    public void registerItemModel() {
        BTEMod.proxy.registerItemRenderer(this, 0, name);
    }

}