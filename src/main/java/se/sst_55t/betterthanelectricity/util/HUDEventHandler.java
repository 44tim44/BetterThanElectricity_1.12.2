package se.sst_55t.betterthanelectricity.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.item.IChargeable;
import se.sst_55t.betterthanelectricity.item.ModItems;

/**
 * Created by Timeout on 2017-10-14.
 */
public class HUDEventHandler {

    private static final ResourceLocation JETPACK_EMPTY = new ResourceLocation(BTEMod.MODID, "textures/gui/jetpack_empty_small.png");
    private static final ResourceLocation JETPACK_FULL = new ResourceLocation(BTEMod.MODID, "textures/gui/jetpack_full_small.png");
    private static final ResourceLocation GRAVITYBOOTS_EMPTY = new ResourceLocation(BTEMod.MODID, "textures/gui/gravity_boots_empty_small.png");
    private static final ResourceLocation GRAVITYBOOTS_FULL = new ResourceLocation(BTEMod.MODID, "textures/gui/gravity_boots_full_small.png");

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void drawHud(RenderGameOverlayEvent.Post event)
    {
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;
            int xMax = event.getResolution().getScaledWidth();
            int yMax = event.getResolution().getScaledHeight();
            int xMin = 0;
            int yMin = 0;

            ItemStack bootStack = player.inventory.armorInventory.get(0);
            ItemStack chestplateStack = player.inventory.armorInventory.get(2);

            int bootCharge;
            int chestplateCharge;

            if (!bootStack.isEmpty()) {
                if (bootStack.getItem() == ModItems.gravityBoots) {
                    bootCharge = ((IChargeable) bootStack.getItem()).getCharge(bootStack);
                    int bootMaxCharge = ((IChargeable) bootStack.getItem()).getMaxCharge(bootStack);

                    int k = getChargeScaled(bootCharge, bootMaxCharge, 11);

                    int x = xMin + 4;
                    int y = yMin + 4 + 16 + 4;

                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GlStateManager.color(1F, 1F, 1F, 0.75F);
                    mc.renderEngine.bindTexture(GRAVITYBOOTS_EMPTY);
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
                    mc.renderEngine.bindTexture(GRAVITYBOOTS_FULL);
                    Gui.drawModalRectWithCustomSizedTexture(x, y + 14 - k, xMin, yMin + 14 - k, 16, k, 16, 16);
                    GlStateManager.popMatrix();
                }
            }


            if (!chestplateStack.isEmpty()) {
                if (chestplateStack.getItem() == ModItems.jetpack) {
                    chestplateCharge = ((IChargeable) chestplateStack.getItem()).getCharge(chestplateStack);
                    int chestplateMaxCharge = ((IChargeable) chestplateStack.getItem()).getMaxCharge(chestplateStack);

                    int k = getChargeScaled(chestplateCharge, chestplateMaxCharge, 15);

                    int x = xMin + 4;
                    int y = yMin + 4;

                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GlStateManager.color(1F, 1F, 1F, 0.75F);
                    mc.renderEngine.bindTexture(JETPACK_EMPTY);
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
                    mc.renderEngine.bindTexture(JETPACK_FULL);
                    Gui.drawModalRectWithCustomSizedTexture(x, y + 15 - k, xMin, yMin + 15 - k, 16, k, 16, 16);
                    GlStateManager.popMatrix();
                }
            }
        }
    }

    private int getChargeScaled(int charge, int maxCharge, int pixels)
    {
        return (int)Math.round((double)charge / (double)maxCharge * (double)pixels);
    }
}
