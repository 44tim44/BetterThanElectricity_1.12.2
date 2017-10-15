package se.sst_55t.betterthanelectricity.util.submarine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.IMultipassModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.entity.item.EntitySubmarine;

@SideOnly(Side.CLIENT)
public class ModelSubmarine extends ModelBase implements IMultipassModel {
    public ModelRenderer[] boatSides = new ModelRenderer[5];
    public ModelRenderer[] glassSides = new ModelRenderer[6];
    public ModelRenderer[] propeller = new ModelRenderer[2];

    /**
     * Part of the model rendered to make it seem like there's no water in the boat
     */
    public ModelRenderer noWater;
    private final int patchList = GLAllocation.generateDisplayLists(1);

    public ModelSubmarine() {
        this.boatSides[0] = (new ModelRenderer(this, 0, 0)).setTextureSize(128, 128);
        this.boatSides[1] = (new ModelRenderer(this, 0, 19)).setTextureSize(128, 128);
        this.boatSides[2] = (new ModelRenderer(this, 0, 27)).setTextureSize(128, 128);
        this.boatSides[3] = (new ModelRenderer(this, 0, 35)).setTextureSize(128, 128);
        this.boatSides[4] = (new ModelRenderer(this, 0, 43)).setTextureSize(128, 128);

        this.glassSides[0] = (new ModelRenderer(this, 64, 0)).setTextureSize(128, 128);
        this.glassSides[1] = (new ModelRenderer(this, 72, 19)).setTextureSize(128, 128);
        this.glassSides[2] = (new ModelRenderer(this, 72, 37)).setTextureSize(128, 128);
        this.glassSides[3] = (new ModelRenderer(this, 64, 55)).setTextureSize(128, 128);
        this.glassSides[4] = (new ModelRenderer(this, 64, 73)).setTextureSize(128, 128);
        this.glassSides[5] = (new ModelRenderer(this, 0, 91)).setTextureSize(128, 128);

        this.propeller[0] = (new ModelRenderer(this, 0, 51)).setTextureSize(128,128);
        this.propeller[1] = (new ModelRenderer(this, 6, 51)).setTextureSize(128,128);

        int i = 32;
        int j = 6;
        int k = 20;
        int l = 4;
        int i1 = 28;
        this.boatSides[0].addBox(-14.0F, -9.0F, -3.0F, 28, 16, 3, 0.0F);
        this.boatSides[0].setRotationPoint(0.0F, 3.0F, 1.0F);
        this.boatSides[1].addBox(-14.0F, -7.0F, -1.0F, 16, 6, 2, 0.0F);
        this.boatSides[1].setRotationPoint(-15.0F, 4.0F, 6.0F);
        this.boatSides[2].addBox(-8.0F, -7.0F, -1.0F, 16, 6, 2, 0.0F);
        this.boatSides[2].setRotationPoint(15.0F, 4.0F, 0.0F);
        this.boatSides[3].addBox(-16.0F, -7.0F, -1.0F, 32, 6, 2, 0.0F);
        this.boatSides[3].setRotationPoint(0.0F, 4.0F, -9.0F);
        this.boatSides[4].addBox(-16.0F, -7.0F, -1.0F, 32, 6, 2, 0.0F);
        this.boatSides[4].setRotationPoint(0.0F, 4.0F, 9.0F);

        this.glassSides[0].addBox(-14.0F, -9.0F, -3.0F, 28, 16, 2, 0.0F);
        this.glassSides[0].setRotationPoint(0.0F, -22.0F, 1.0F);
        this.glassSides[1].addBox(-12.0F, -7.0F, -1.0F, 16, 16, 2, 0.0F);
        this.glassSides[1].setRotationPoint(-15.0F, -12.0F, 4.0F);
        this.glassSides[2].addBox(-8.0F, -7.0F, -1.0F, 16, 16, 2, 0.0F);
        this.glassSides[2].setRotationPoint(15.0F, -12.0F, 0.0F);
        this.glassSides[3].addBox(-14.0F, -7.0F, -1.0F, 28, 16, 2, 0.0F);
        this.glassSides[3].setRotationPoint(0.0F, -12.0F, -9.0F);
        this.glassSides[4].addBox(-14.0F, -7.0F, -1.0F, 28, 16, 2, 0.0F);
        this.glassSides[4].setRotationPoint(0.0F, -12.0F, 9.0F);
        this.glassSides[5].addBox(-14.0F, -9.0F, -3.0F, 28, 16, 16, 0.0F);
        this.glassSides[5].setRotationPoint(0.0F, -6.0F, 1.0F);


        this.boatSides[0].rotateAngleX = ((float) Math.PI / 2F);
        this.boatSides[1].rotateAngleY = ((float) Math.PI * 3F / 2F);
        this.boatSides[2].rotateAngleY = ((float) Math.PI / 2F);
        this.boatSides[3].rotateAngleY = (float) Math.PI;

        this.glassSides[0].rotateAngleX = ((float) Math.PI / 2F);
        this.glassSides[1].rotateAngleY = ((float) Math.PI * 3F / 2F);
        this.glassSides[2].rotateAngleY = ((float) Math.PI / 2F);
        this.glassSides[3].rotateAngleY = (float) Math.PI;
        this.glassSides[5].rotateAngleX = ((float) Math.PI / 2F);

        this.propeller[0].addBox(-1.0F, -4.0F, -1.0F, 1, 8, 2, 0.0F);
        this.propeller[0].setRotationPoint(-16.0F, 0.0F, 0.0F);
        this.propeller[1].addBox(-1.0F, -4.0F, -1.0F, 1, 8, 2, 0.0F);
        this.propeller[1].setRotationPoint(-16.0F, 0.0F, 0.0F);
        this.propeller[0].rotateAngleX = ((float) Math.PI / 2F);

        this.noWater = (new ModelRenderer(this, 0, 83)).setTextureSize(128, 128);
        this.noWater.addBox(-14.0F, -9.0F, -3.0F, 28, 16, 3, 0.0F);
        this.noWater.setRotationPoint(0.0F, -3.0F, 1.0F);
        this.noWater.rotateAngleX = ((float) Math.PI / 2F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        EntitySubmarine entityboat = (EntitySubmarine) entityIn;
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);

        for (int i = 0; i < 5; ++i) {
            this.boatSides[i].render(scale);
        }

        for (int i = 0; i < 6; ++i) {
            this.glassSides[i].render(scale);
        }

        this.renderPropeller(entityboat,scale,limbSwing);
    }

    public void renderMultipass(Entity p_187054_1_, float p_187054_2_, float p_187054_3_, float p_187054_4_, float p_187054_5_, float p_187054_6_, float scale) {
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.colorMask(false, false, false, false);
        //this.noWater.render(scale);
        GlStateManager.colorMask(true, true, true, true);
    }

    protected void renderPropeller(EntitySubmarine submarine, float scale, float limbSwing) {
        float f1 = submarine.getRowingTime(0, limbSwing);
        float f2 = submarine.getRowingTime(1, limbSwing);
        float f = (f1 + f2)/2.0F;

        ModelRenderer modelrenderer = this.propeller[0];
        ModelRenderer modelrenderer1 = this.propeller[1];

        modelrenderer.rotateAngleX = (float) (Math.PI * (f % 360));
        modelrenderer1.rotateAngleX = (float) (Math.PI * (f % 360) + (Math.PI / 2));

        modelrenderer.render(scale);
        modelrenderer1.render(scale);
    }
}