package se.sst_55t.betterthanelectricity.block.solarpanel;

import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.block.ModBlocks;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Timeout on 2017-08-23.
 */
public class GuiSolarPanel extends GuiContainer{
    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(BTEMod.MODID, "textures/gui/solar_panel.png");
    private InventoryPlayer playerInv;
    private TileEntitySolarPanel tileSolar;

    public GuiSolarPanel(Container container, InventoryPlayer playerInv, TileEntitySolarPanel tesp) {
        super(container);
        this.playerInv = playerInv;
        this.tileSolar = tesp;
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

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(BG_TEXTURE);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        if (tileSolar.isCharging())
        {
            //this.drawTexturedModalRect(x + 80, y + 36, 176, 0, 14, 14);
            this.drawTexturedModalRect(x + 61, y + 34, 176, 14, 18, 18);
            //int k = this.getChargeLeftScaled(14);
            if(this.tileSolar.isGivingCharge())
            {
                this.drawTexturedModalRect(x + 80, y + 36, 176, 0, 14, 14);
            }
            else
            {
                this.drawTexturedModalRect(x + 80, y + 36, 176, 0, 0, 0);
            }
        }
        else
        {
            this.drawTexturedModalRect(x + 61, y + 34, 176, 14, 0, 0);
            this.drawTexturedModalRect(x + 80, y + 36, 176, 0, 0, 0);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = I18n.format(ModBlocks.solarPanel.getUnlocalizedName() + ".name");
        fontRenderer.drawString(name, xSize / 2 - fontRenderer.getStringWidth(name) / 2, 6, 0x404040);
        fontRenderer.drawString(playerInv.getDisplayName().getUnformattedText(), 8, ySize - 94, 0x404040);
        float chargeRate = this.tileSolar.getItemChargeTime();
        if (tileSolar.isCharging()) {
            fontRenderer.drawString(String.format("%.2f", 1.0F / (chargeRate / 20.0F)) + " Energy/sec", 85, ySize - 94, 0x404040);
        } else {
            fontRenderer.drawString(String.format("%.2f", 0.0F) + " Energy/sec", 85, ySize - 94, 0x404040);
            //fontRenderer.drawString("Charge rate: " + String.format("%.2f", 0.0F) + " Energy/sec", 8, 18, 0x404040);
        }
    }

    private int getChargeLeftScaled(int pixels)
    {
        int i = this.tileSolar.getChargeTime();
        int j = this.tileSolar.getTotalChargeTime();
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }
}
