package se.sst_55t.betterthanelectricity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Timeout on 2017-10-01.
 */
public class ItemJetpack extends ItemArmorCustom implements IChargeable, ISpecialArmor {

    private static final int maxCharge = 640 * 50;

    public ItemJetpack(ArmorMaterial materialIn, EntityEquipmentSlot slot, String name) {
        super(materialIn, slot, name);
        setMaxDamage(1300*50);
    }

    public void decreaseCharge(ItemStack stack){
        this.setCharge(this.getDamage(stack)-1,stack);
    }

    public void increaseCharge(ItemStack stack){

        this.setCharge(this.getDamage(stack)+50,stack);
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
        tooltip.add("Charge: " + Math.round(getCharge(stack)/50.0F) + "/" + maxCharge/50);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 -((double)(getCharge(stack)/50.0F) / (double)(maxCharge/50.0F));
    }

    @Override
    public boolean isDamageable(){
        return false;
    }


    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

        if (entityIn instanceof EntityPlayer)
        {
            ItemStack chestplateStack = ((EntityPlayer) entityIn).inventory.armorInventory.get(2);
            if (!chestplateStack.isEmpty())
            {
                if (chestplateStack.getItem() == ModItems.jetpack)
                {
                    int jumpKeyCode = Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode();
                    int forwardKeyCode = Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode();
                    if (((IChargeable) stack.getItem()).getCharge(stack) > 0)
                    {
                        ((EntityPlayer)entityIn).capabilities.setFlySpeed(0.5F);
                        //if (Keyboard.isKeyDown(jumpKeyCode))
                        if(Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown())
                        {
                            entityIn.setVelocity(entityIn.motionX, 0.5F, entityIn.motionZ);
                            ((IChargeable) stack.getItem()).decreaseCharge(stack);
                        }
                    }
                    else
                    {
                        ((EntityPlayer)entityIn).capabilities.setFlySpeed(0.05F);
                    }

                    if (Keyboard.isKeyDown(jumpKeyCode) && Keyboard.isKeyDown(forwardKeyCode))
                    {
                        if (((IChargeable) stack.getItem()).getCharge(stack) > 0)
                        {
                            double calculatedX= (double) (-MathHelper.sin(entityIn.rotationYaw/ 180.0F * (float) Math.PI)* MathHelper.cos(entityIn.rotationPitch / 180.0F* (float) Math.PI) * 0.8f);
                            double calculatedZ = (double) (MathHelper.cos(entityIn.rotationYaw	/ 180.0F * (float) Math.PI)* MathHelper.cos(entityIn.rotationPitch / 180.0F* (float) Math.PI) * 0.8f);
                            entityIn.setVelocity(calculatedX, entityIn.motionY, calculatedZ);
                            ((IChargeable) stack.getItem()).decreaseCharge(stack);
                        }
                    }


                    //super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
                    /*
                    if (((IChargeable) stack.getItem()).getCharge(stack) > 0)
                    {
                        ((EntityPlayer)entityIn).capabilities.allowFlying = true;
                    }
                    else
                    {
                        ((EntityPlayer)entityIn).capabilities.allowFlying = false;
                    }
                    */
                }
            }
        }
    }

    @Override
    public ISpecialArmor.ArmorProperties getProperties(EntityLivingBase player, @Nonnull ItemStack armor, DamageSource source, double damage, int slot) {
        return new ISpecialArmor.ArmorProperties(0, 0, 0);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, @Nonnull ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, @Nonnull ItemStack stack, DamageSource source, int damage, int slot) {
        stack.damageItem(0, entity);
    }

}
