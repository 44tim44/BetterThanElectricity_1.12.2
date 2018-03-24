package se.sst_55t.betterthanelectricity.block.quarry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.block.ModBlocks;
import se.sst_55t.betterthanelectricity.block.chargingstation.TileEntityChargingStation;
import se.sst_55t.betterthanelectricity.block.table.TileEntityTable;
import se.sst_55t.betterthanelectricity.item.ModItems;

/**
 * @author shadowfacts
 */
@SideOnly(Side.CLIENT)
public class TESRQuarry extends FastTESR<TileEntityQuarry> {

    /*
    private IModel model;
    private IBakedModel bakedModel;

    private IBakedModel getBakedModel()
    {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedModel == null)
        {
            try
            {
                model = ModelLoaderRegistry.getModel(new ModelResourceLocation(BTEMod.MODID + ":table_oak"));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            bakedModel = model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        }
        return bakedModel;
    }


    @Override
    public void render(TileEntityQuarry te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        BlockPos pos = te.getMinerPosition();
        if (pos != null)
        {
            GlStateManager.pushAttrib();
            GlStateManager.pushMatrix();

            // Translate to the location of our tile entity
            GlStateManager.translate(pos.getX(), pos.getY()+1, pos.getZ());
            GlStateManager.disableRescaleNormal();



            GlStateManager.pushMatrix();
            RenderHelper.disableStandardItemLighting();
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
            } else {
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }

            World world = te.getWorld();
            Tessellator tessellator = Tessellator.getInstance();
            tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(
                    world,
                    getBakedModel(),
                    world.getBlockState(te.getPos()),
                    te.getPos(),
                    Tessellator.getInstance().getBuffer(), false);
            tessellator.draw();

            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();



            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
        }
    }
    */


    /*
    @Override
    public void render(TileEntityQuarry te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        BlockPos pos = te.getMinerPosition();
        if (pos != null)
        {

            ModelBakery.registerItemVariants(ModItems.corn, new ResourceLocation(BTEMod.MODID, "block/table_oak"));
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getModel(new ModelResourceLocation(BTEMod.MODID, "table_oak"));
            //net.minecraftforge.client.model.ModelLoader.getInventoryVariant(location.toString())

            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
            GlStateManager.enableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            GlStateManager.pushMatrix();
            double offset = Math.sin((te.getWorld().getTotalWorldTime() + partialTicks) / 8) / 12.0;
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
    */

    protected static BlockRendererDispatcher blockRenderer;

    @Override
    public void renderTileEntityFast(TileEntityQuarry te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        if(blockRenderer == null) blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        BlockPos minerPos = te.getMinerPosition().up();
        BlockPos pos = te.getPos();
        IBlockAccess world = te.getWorld();//MinecraftForgeClient.getRegionRenderCache(te.getWorld(), minerPos);
        IBlockState state = world.getBlockState(minerPos);

        if(world.getBlockState(minerPos).getBlock() == Blocks.AIR ||
                world.getBlockState(minerPos).getBlock() == Blocks.WATER ||
                world.getBlockState(minerPos).getBlock() == Blocks.FLOWING_WATER ||
                world.getBlockState(minerPos).getBlock() == Blocks.LAVA ||
                world.getBlockState(minerPos).getBlock() == Blocks.FLOWING_LAVA)
        {
            IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState(ModBlocks.quarry_miner.getDefaultState());
            buffer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
            blockRenderer.getBlockModelRenderer().renderModel(world, model, state, minerPos, buffer, false);
        }
    }

    @Override
    public boolean isGlobalRenderer(TileEntityQuarry te)
    {
        return true;
    }

}