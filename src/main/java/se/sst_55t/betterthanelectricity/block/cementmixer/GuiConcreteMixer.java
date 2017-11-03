package se.sst_55t.betterthanelectricity.block.cementmixer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.block.compactor.ContainerCompactor;
import se.sst_55t.betterthanelectricity.block.compactor.TileEntityCompactor;

import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiConcreteMixer extends GuiContainer
{
    private static final ResourceLocation concreteMixerTextures = new ResourceLocation(BTEMod.MODID, "textures/gui/concrete_mixer.png");
    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;
    private final IInventory tileConcreteMixer;

    public GuiConcreteMixer(InventoryPlayer playerInv, IInventory tileConcreteMixer)
    {
        super(new ContainerConcreteMixer(playerInv, tileConcreteMixer));
        this.playerInventory = playerInv;
        this.tileConcreteMixer = tileConcreteMixer;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.tileConcreteMixer.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(concreteMixerTextures);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        if(TileEntityConcreteMixer.isBurning(this.tileConcreteMixer))
        {
            int k = this.getBurnLeftScaled(14);
            this.drawTexturedModalRect(i + 56, j + 36 + 14 - k, 176, 14 - k, 14, k);
        }

        int l = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
    }

    private int getCookProgressScaled(int pixels)
    {
        int i = this.tileConcreteMixer.getField(2);
        int j = this.tileConcreteMixer.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels)
    {
        int i = this.tileConcreteMixer.getField(1);

        if (i == 0)
        {
            i = 200;
        }

        return this.tileConcreteMixer.getField(0) * pixels / i;
    }

}