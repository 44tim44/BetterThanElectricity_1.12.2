package se.sst_55t.betterthanelectricity.block.chargingstation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;

/**
 * @author shadowfacts
 */
public class TESRChargingStation extends TileEntitySpecialRenderer<TileEntityChargingStation> {

    @Override
    public void render(TileEntityChargingStation te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ItemStack stack = te.getStackInSlot(0);
        if (!stack.isEmpty()) {
            if(TileEntityChargingStation.isBlockAboveAir(te)) {

                GlStateManager.enableRescaleNormal();
                GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
                GlStateManager.enableBlend();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
                GlStateManager.pushMatrix();
                double offset = Math.sin((te.getWorld().getTotalWorldTime() + partialTicks) / 8) / 12.0;
                //GlStateManager.translate(x + 0.5, y + 1.25 + offset, z + 0.5);
                //GlStateManager.rotate((te.getWorld().getTotalWorldTime() + partialTicks) * 4, 0, 1, 0);
                GlStateManager.translate(x + 0.5, y + 1.05, z + 0.35);
                GlStateManager.rotate(90,1,0,0);

                IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack, te.getWorld(), null);
                model = ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.GROUND, false);

                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);

                GlStateManager.popMatrix();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }
        }
    }



}