package se.sst_55t.betterthanelectricity.block.chargingstation;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.BTEMod;

@SideOnly(Side.CLIENT)
public class GuiChargingStation extends GuiContainer
{
    private static final ResourceLocation chargingStationTextures =
            new ResourceLocation(BTEMod.MODID,
                    "textures/gui/fuel_generator.png");

    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;
    private final TileEntityChargingStation tileChargingStation;

    public GuiChargingStation(InventoryPlayer playerInv, TileEntityChargingStation tecs)
    {
        super(new ContainerChargingStation(playerInv, tecs));
        this.playerInventory = playerInv;
        this.tileChargingStation = tecs;
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
        String s = this.tileChargingStation.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
        float chargeRate = this.tileChargingStation.getOutputRate((ItemStack)null);
        fontRenderer.drawString(String.format("%.2f", 1.0F / (chargeRate / 20.0F)) + " Energy/sec", 85, ySize - 94, 0x404040);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(chargingStationTextures);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);

        if (TileEntityChargingStation.isTakingCharge(this.tileChargingStation))
        {
            //int k = this.getBurnLeftScaled(14);
            this.drawTexturedModalRect(x + 71, y + 36, 176, 0, 14, 14);
        }
        if (TileEntityChargingStation.isGivingCharge(this.tileChargingStation))
        {
            //int l = this.getCookProgressScaled(14);
            this.drawTexturedModalRect(x + 89, y + 36, 176, 0, 14, 14);
        }
    }

    private int getCookProgressScaled(int pixels)
    {
        int i = this.tileChargingStation.getField(2);
        int j = this.tileChargingStation.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels)
    {
        int i = this.tileChargingStation.getField(1);

        if (i == 0)
        {
            i = 200;
        }

        return this.tileChargingStation.getField(0) * pixels / i;
    }
}