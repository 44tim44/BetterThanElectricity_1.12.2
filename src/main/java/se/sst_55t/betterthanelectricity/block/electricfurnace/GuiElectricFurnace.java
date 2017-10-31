package se.sst_55t.betterthanelectricity.block.electricfurnace;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import se.sst_55t.betterthanelectricity.BTEMod;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.block.IGenerator;
import se.sst_55t.betterthanelectricity.item.IBattery;

import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiElectricFurnace extends GuiContainer
{
    private static final ResourceLocation electricFurnaceTextures =
            new ResourceLocation(BTEMod.MODID,
                    "textures/gui/electric_furnace.png");

    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;
    private final TileEntityElectricFurnace tileFurnace;

    public GuiElectricFurnace(InventoryPlayer playerInv, TileEntityElectricFurnace furnaceInv)
    {
        super(new ContainerElectricFurnace(playerInv, furnaceInv));
        this.playerInventory = playerInv;
        this.tileFurnace = furnaceInv;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new HintButton(0, this.guiLeft + 56, this.guiTop + 36));
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);

        for (int i = 0; i < buttonList.size(); i++) {
            if (buttonList.get(i) instanceof GuiButton) {
                GuiButton btn = (GuiButton) buttonList.get(i);
                if (btn.isMouseOver())
                {
                    float charge = this.tileFurnace.getChargeRate();
                    String[] desc = { "Current Charge: " +  charge , "Required Charge: " + tileFurnace.getConsumeRate()};
                    @SuppressWarnings("rawtypes")
                    List temp = Arrays.asList(desc);
                    drawHoveringText(temp, mouseX, mouseY, fontRenderer);
                }
            }
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.tileFurnace.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(electricFurnaceTextures);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        int k = this.getBurnLeftScaled(14);
        this.drawTexturedModalRect(i + 56, j + 36 + 14 - k, 176, 14 - k, 14, k);

        int l = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
    }

    private int getCookProgressScaled(int pixels)
    {
        int i = this.tileFurnace.getField(2);
        int j = this.tileFurnace.getField(3);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    private int getBurnLeftScaled(int pixels)
    {
        return Math.round(this.tileFurnace.getGUIChargeRatio() * pixels);
    }

    @SideOnly(Side.CLIENT)
    static class HintButton extends GuiButton {
        private static final String __OBFID = "CL_00000743";

        protected HintButton(int buttonID, int posx, int posy) {
            super(buttonID, posx, posy, 16, 16, "");
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(electricFurnaceTextures);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                this.drawTexturedModalRect(this.x, this.y, 0, 182, this.width, this.height);
            }
        }

    }
}