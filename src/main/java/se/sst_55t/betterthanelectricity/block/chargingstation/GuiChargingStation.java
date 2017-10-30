package se.sst_55t.betterthanelectricity.block.chargingstation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.block.IGenerator;
import se.sst_55t.betterthanelectricity.item.IBattery;

import java.util.Arrays;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiChargingStation extends GuiContainer
{
    private static final ResourceLocation chargingStationTextures =
            new ResourceLocation(BTEMod.MODID,
                    "textures/gui/charging_station.png");

    /** The player inventory bound to this GUI. */
    private final InventoryPlayer playerInventory;
    private final TileEntityChargingStation tileChargingStation;

    public GuiChargingStation(InventoryPlayer playerInv, TileEntityChargingStation tecs)
    {
        super(new ContainerChargingStation(playerInv, tecs));
        this.playerInventory = playerInv;
        this.tileChargingStation = tecs;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new HintButton(0, this.guiLeft + 143, this.guiTop + 22));
        buttonList.add(new HintButton(1, this.guiLeft + 98, this.guiTop + 49));
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
                    if(i == 0)
                    {
                        String[] desc = {"Charge: " + tileChargingStation.getCharge() + "/" + tileChargingStation.getMaxCharge()};
                        @SuppressWarnings("rawtypes")
                        List temp = Arrays.asList(desc);
                        drawHoveringText(temp, mouseX, mouseY, fontRenderer);
                    }

                    if(i == 1)
                    {
                        float charge = 0;
                        TileEntity te = tileChargingStation.getOutputTE();
                        ItemStack batteryStack = tileChargingStation.getStackInSlot(1);
                        if(te != null && te instanceof IGenerator)
                        {
                            charge = ((IGenerator)te).getChargeRate();
                        }
                        if(!batteryStack.isEmpty() && batteryStack.getItem() instanceof IBattery && ((IBattery)batteryStack.getItem()).getCharge(batteryStack) > 0)
                        {
                            charge = tileChargingStation.getConsumeRate();
                        }
                        String[] desc = { "Current Charge/sec: " +  charge};
                        @SuppressWarnings("rawtypes")
                        List temp = Arrays.asList(desc);
                        drawHoveringText(temp, mouseX, mouseY, fontRenderer);
                    }
                }
            }
        }
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
        fontRenderer.drawString(String.format("%.2f", 1.0F / (chargeRate / 20.0F)) + " Energy/sec", 80, ySize - 94, 0x404040);
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

        int l = this.getChargeRatioScaled(14);
        this.drawTexturedModalRect(x + 98, y + 49 + 14 - l, 176, 14 - l, 14, l);
        if(l > 0) this.drawTexturedModalRect(x + 115, y + 49, 206, 14, 23, 14);

        int m = this.getDischargeRatioScaled(14);
        this.drawTexturedModalRect(x + 98, y + 23 + 14 - m, 176, 14 - m, 14, m);
        if(m > 0) this.drawTexturedModalRect(x + 115, y + 23, 206, 0, 23, 14);
        /*
        if (TileEntityChargingStation.isTakingCharge(this.tileChargingStation))
        {
            //int k = this.getChargeRatioScaled(14);
            this.drawTexturedModalRect(x + 98, y + 49, 176, 0, 14, 14);
            this.drawTexturedModalRect(x + 115, y + 49, 206, 14, 23, 14);
        }

        if (TileEntityChargingStation.isGivingCharge(this.tileChargingStation))
        {
            //int l = this.getCookProgressScaled(14);
            this.drawTexturedModalRect(x + 98, y + 23, 176, 0, 14, 14);
            this.drawTexturedModalRect(x + 115, y + 23, 206, 0, 23, 14);
        }
        */
        int k = this.getChargeBar(42);
        this.drawTexturedModalRect(x + 143, y + 22 + 42 - k, 190, 0, 16, k);
    }

    private int getChargeRatioScaled(int pixels)
    {
        return Math.round(this.tileChargingStation.getGUIChargeRatio() * pixels);
    }

    private int getDischargeRatioScaled(int pixels)
    {
        return Math.round(this.tileChargingStation.getGUIDischargeRatio() * pixels);
    }

    private int getChargeBar(int pixels)
    {
        int i = this.tileChargingStation.getCharge();
        int j = this.tileChargingStation.getMaxCharge();
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }

    @SideOnly(Side.CLIENT)
    static class HintButton extends GuiButton {
        private static final String __OBFID = "CL_00000743";

        protected HintButton(int buttonID, int posx, int posy) {
            super(buttonID, posx, posy, 16, 42, "");
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(chargingStationTextures);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                this.drawTexturedModalRect(this.x, this.y, 0, 182, this.width, this.height);
            }
        }

    }
}