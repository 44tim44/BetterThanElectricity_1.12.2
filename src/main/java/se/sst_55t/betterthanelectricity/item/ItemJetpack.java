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
                boolean dirty = false;
                if (chestplateStack.getItem() == ModItems.jetpack)
                {
                    int jumpKeyCode = Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode();
                    int forwardKeyCode = Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode();
                    if (((IChargeable) stack.getItem()).getCharge(stack) > 0)
                    {
                        if(Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown())
                        {
                            //BTEMod.network.sendToServer(new PacketUpdateJetpack((EntityPlayer)entityIn,stack,0));
                            if(!(entityIn.motionY >= 0.5F)) {
                                entityIn.setVelocity(entityIn.motionX, entityIn.motionY+0.15F, entityIn.motionZ);
                                ((IChargeable) stack.getItem()).decreaseCharge(stack);
                                //if(worldIn.getWorldTime()%10 == 0) { worldIn.playSound((EntityPlayer) entityIn, entityIn.getPosition(), ModSoundEvents.JETPACK_THRUST, SoundCategory.PLAYERS, 1.0F, 1.0F);}
                                dirty = true;
                            }
                        }
                    }

                    if (Keyboard.isKeyDown(forwardKeyCode) && !entityIn.onGround)
                    {
                        if (((IChargeable) stack.getItem()).getCharge(stack) > 0)
                        {
                            double calculatedX;
                            double calculatedZ;
                            //BTEMod.network.sendToServer(new PacketUpdateJetpack((EntityPlayer)entityIn,stack,1));
                            if(Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown()) {
                                calculatedX = (double) (-MathHelper.sin(entityIn.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entityIn.rotationPitch / 180.0F * (float) Math.PI) * 0.8f);
                                calculatedZ = (double) (MathHelper.cos(entityIn.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entityIn.rotationPitch / 180.0F * (float) Math.PI) * 0.8f);
                            }
                            else
                            {
                                calculatedX = (double) (-MathHelper.sin(entityIn.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entityIn.rotationPitch / 180.0F * (float) Math.PI) * 0.4f);
                                calculatedZ = (double) (MathHelper.cos(entityIn.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(entityIn.rotationPitch / 180.0F * (float) Math.PI) * 0.4f);
                            }
                            entityIn.setVelocity(calculatedX, entityIn.motionY, calculatedZ);
                            ((IChargeable) stack.getItem()).decreaseCharge(stack);
                            dirty = true;
                        }
                    }

                    if(dirty)
                    {
                        if (worldIn.isRemote) {
                            BTEMod.network.sendToServer(new PacketToServerJetpack(((IChargeable)chestplateStack.getItem()).getCharge(chestplateStack)));
                        }
                        dirty = false;
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
