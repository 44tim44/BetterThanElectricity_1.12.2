package se.sst_55t.betterthanelectricity.block.windmill;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import se.sst_55t.betterthanelectricity.block.ModBlocks;

/**
 * Created by Timeout on 2018-03-25.
 */
public class TESRWindWheel extends TileEntitySpecialRenderer<TileEntityWindWheel>{

    @Override
    public void render(TileEntityWindWheel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        BlockPos pos = te.getPos();
        IBlockAccess world = te.getWorld();
        IBlockState iblockstate = world.getBlockState(pos).getActualState(world,pos);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bb = tessellator.getBuffer();
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

        if(world.getBlockState(pos.offset(iblockstate.getValue(BlockWindWheel.FACING).getOpposite())).getBlock() instanceof BlockWindMill)
        {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();

            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            bb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

            GlStateManager.translate(x, y, z);
            GlStateManager.translate(0.5, 0.5, 0.5);
            long time = System.currentTimeMillis() - 1522021537115L; //% 36000L;
            TileEntityWindMill tewm = ((TileEntityWindMill)world.getTileEntity(pos.offset(iblockstate.getValue(BlockWindWheel.FACING).getOpposite())));

            if(tewm.isCharging())
            {
                float rotation = time / (0.2F*(float)tewm.getItemChargeTime()) / 2;
                rotation = rotation % 360.0F;
                if (iblockstate.getValue(BlockWindWheel.FACING) == EnumFacing.EAST)
                {
                    GlStateManager.rotate(rotation, 1, 0, 0);
                }
                else if (iblockstate.getValue(BlockWindWheel.FACING) == EnumFacing.WEST)
                {
                    GlStateManager.rotate(-rotation, 1, 0, 0);
                }
                else if (iblockstate.getValue(BlockWindWheel.FACING) == EnumFacing.SOUTH)
                {
                    GlStateManager.rotate(rotation, 0, 0, 1);
                }
                else
                {
                    GlStateManager.rotate(-rotation, 0, 0, 1);
                }
            }

            GlStateManager.translate(-0.5, -0.5, -0.5);
            GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());

            dispatcher.getBlockModelRenderer().renderModel(te.getWorld(), dispatcher.getModelForState(iblockstate), iblockstate, pos, bb, false, MathHelper.getPositionRandom(te.getPos()));

            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}
