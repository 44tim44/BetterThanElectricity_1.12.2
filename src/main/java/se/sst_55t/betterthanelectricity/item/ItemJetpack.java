package se.sst_55t.betterthanelectricity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.network.PacketRequestUpdatePedestal;
import se.sst_55t.betterthanelectricity.network.PacketToServerJetpack;
import se.sst_55t.betterthanelectricity.util.ModSoundEvents;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Timeout on 2017-10-01.
 */
public class ItemJetpack extends ItemArmorCustom implements IChargeable, ISpecialArmor {

    private static final int maxCharge = 320 * 50;

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

    @SideOnly(Side.CLIENT)
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

        if (entityIn instanceof EntityPlayer)
        {
            ItemStack chestplateStack = ((EntityPlayer) entityIn).inventory.armorInventory.get(2);
            if (!chestplateStack.isEmpty())
            {
                if (chestplateStack.getItem() == ModItems.jetpack)
                {
                    if (((IChargeable) stack.getItem()).getCharge(stack) > 0)
                    {
                        if(Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown())
                        {
                            if (!(entityIn.motionY >= 0.5F)) {
                                entityIn.setVelocity(entityIn.motionX, entityIn.motionY + 0.15F, entityIn.motionZ);
                                BTEMod.network.sendToServer(new PacketToServerJetpack(0,((EntityPlayer) entityIn).getName()));
                            }
                        }
                    }

                    if (((IChargeable) stack.getItem()).getCharge(stack) > 0)
                    {
                        if(Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown() && !entityIn.onGround)
                        {
                            if ((entityIn.motionY > 0.15F))
                            {
                                entityIn.setVelocity(entityIn.motionX, entityIn.motionY - 0.15F, entityIn.motionZ);
                                BTEMod.network.sendToServer(new PacketToServerJetpack(3,((EntityPlayer) entityIn).getName()));
                            }
                            else if ((entityIn.motionY < -0.15F))
                            {
                                entityIn.setVelocity(entityIn.motionX, entityIn.motionY + 0.15F, entityIn.motionZ);
                                BTEMod.network.sendToServer(new PacketToServerJetpack(3,((EntityPlayer) entityIn).getName()));
                            }
                            else
                            {
                                entityIn.setVelocity(entityIn.motionX, 0, entityIn.motionZ);
                                BTEMod.network.sendToServer(new PacketToServerJetpack(3,((EntityPlayer) entityIn).getName()));
                            }

                        }
                    }

                    if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown() && !entityIn.onGround)
                    {
                        if (((IChargeable) stack.getItem()).getCharge(stack) > 0)
                        {
                            double calculatedX;
                            double calculatedZ;
                            if(Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown())
                            {
                                if (!(Math.sqrt(Math.pow(entityIn.motionX,2) + Math.pow(entityIn.motionZ,2)) >= 0.8F))
                                {
                                    calculatedX = (double) (-MathHelper.sin(entityIn.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entityIn.rotationPitch / 180.0F * (float) Math.PI) * 0.2f);
                                    calculatedZ = (double) (MathHelper.cos(entityIn.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entityIn.rotationPitch / 180.0F * (float) Math.PI) * 0.2f);
                                    entityIn.setVelocity(entityIn.motionX + calculatedX, entityIn.motionY, entityIn.motionZ + calculatedZ);
                                    BTEMod.network.sendToServer(new PacketToServerJetpack(2, ((EntityPlayer) entityIn).getName()));
                                }
                            }
                            else
                            {
                                if (!(Math.sqrt(Math.pow(entityIn.motionX,2) + Math.pow(entityIn.motionZ,2)) >= 0.4F))
                                {
                                    calculatedX = (double) (-MathHelper.sin(entityIn.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entityIn.rotationPitch / 180.0F * (float) Math.PI) * 0.1F);
                                    calculatedZ = (double) (MathHelper.cos(entityIn.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entityIn.rotationPitch / 180.0F * (float) Math.PI) * 0.1F);
                                    entityIn.setVelocity(entityIn.motionX + calculatedX, entityIn.motionY, entityIn.motionZ + calculatedZ);
                                    BTEMod.network.sendToServer(new PacketToServerJetpack(1, ((EntityPlayer) entityIn).getName()));
                                }
                            }
                        }
                    }

                    if (Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown() && !entityIn.onGround)
                    {
                        if (((IChargeable) stack.getItem()).getCharge(stack) > 0)
                        {
                            if (!(Math.sqrt(Math.pow(entityIn.motionX,2) + Math.pow(entityIn.motionZ,2)) >= 0.3F))
                            {
                                double calculatedX = (double) (-MathHelper.sin((entityIn.rotationYaw - 180) / 180.0F * (float) Math.PI) * 0.05F);
                                double calculatedZ = (double) (MathHelper.cos((entityIn.rotationYaw - 180) / 180.0F * (float) Math.PI) * 0.05F);
                                entityIn.setVelocity(entityIn.motionX + calculatedX, entityIn.motionY, entityIn.motionZ + calculatedZ);
                                BTEMod.network.sendToServer(new PacketToServerJetpack(1, ((EntityPlayer) entityIn).getName()));
                            }
                        }
                    }

                    if (Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown() && !entityIn.onGround)
                    {
                        if (((IChargeable) stack.getItem()).getCharge(stack) > 0)
                        {
                            if (!(Math.sqrt(Math.pow(entityIn.motionX,2) + Math.pow(entityIn.motionZ,2)) >= 0.3F))
                            {
                                double calculatedX = (double) (-MathHelper.sin((entityIn.rotationYaw - 90) / 180.0F * (float) Math.PI) * 0.05F);
                                double calculatedZ = (double) (MathHelper.cos((entityIn.rotationYaw - 90) / 180.0F * (float) Math.PI) * 0.05F);
                                entityIn.setVelocity(entityIn.motionX + calculatedX, entityIn.motionY,entityIn.motionZ + calculatedZ);
                                BTEMod.network.sendToServer(new PacketToServerJetpack(1, ((EntityPlayer) entityIn).getName()));
                            }
                        }
                    }

                    if (Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown() && !entityIn.onGround)
                    {
                        if (((IChargeable) stack.getItem()).getCharge(stack) > 0)
                        {
                            if (!(Math.sqrt(Math.pow(entityIn.motionX,2) + Math.pow(entityIn.motionZ,2)) >= 0.3F)) {
                                double calculatedX = (double) (-MathHelper.sin((entityIn.rotationYaw + 90) / 180.0F * (float) Math.PI) * 0.05F);
                                double calculatedZ = (double) (MathHelper.cos((entityIn.rotationYaw + 90) / 180.0F * (float) Math.PI) * 0.05F);
                                entityIn.setVelocity(entityIn.motionX + calculatedX, entityIn.motionY, entityIn.motionZ + calculatedZ);
                                BTEMod.network.sendToServer(new PacketToServerJetpack(1, ((EntityPlayer) entityIn).getName()));
                            }
                        }
                    }
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
