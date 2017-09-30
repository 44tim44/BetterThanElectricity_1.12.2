package se.sst_55t.betterthanelectricity;

import se.sst_55t.betterthanelectricity.block.electricfurnace.ContainerElectricFurnace;
import se.sst_55t.betterthanelectricity.block.electricfurnace.GuiElectricFurnace;
import se.sst_55t.betterthanelectricity.block.electricfurnace.TileEntityElectricFurnace;
import se.sst_55t.betterthanelectricity.block.pulverizer.ContainerPulverizer;
import se.sst_55t.betterthanelectricity.block.pulverizer.TileEntityPulverizer;
import se.sst_55t.betterthanelectricity.block.pulverizer.GuiPulverizer;
import se.sst_55t.betterthanelectricity.block.solarpanel.ContainerSolarPanel;
import se.sst_55t.betterthanelectricity.block.solarpanel.GuiSolarPanel;
import se.sst_55t.betterthanelectricity.block.solarpanel.TileEntitySolarPanel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import se.sst_55t.betterthanelectricity.block.windmill.ContainerWindMill;
import se.sst_55t.betterthanelectricity.block.windmill.GuiWindMill;
import se.sst_55t.betterthanelectricity.block.windmill.TileEntityWindMill;


/**
 * Created by Timmy on 2016-11-27.
 */
public class ModGuiHandler implements IGuiHandler {

    public static final int PULVERIZER = 0;
    public static final int ELECFURNACE = 1;
    public static final int SOLARPANEL = 2;
    public static final int WINDMILL = 3;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case PULVERIZER:
                return new ContainerPulverizer(player.inventory, (TileEntityPulverizer)world.getTileEntity(new BlockPos(x, y, z)));
            case ELECFURNACE:
                return new ContainerElectricFurnace(player.inventory, (TileEntityElectricFurnace)world.getTileEntity(new BlockPos(x, y, z)));
            case SOLARPANEL:
                return new ContainerSolarPanel(player.inventory,(TileEntitySolarPanel)world.getTileEntity(new BlockPos(x, y, z)));
            case WINDMILL:
                return new ContainerWindMill(player.inventory,(TileEntityWindMill)world.getTileEntity(new BlockPos(x, y, z)));
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case PULVERIZER:
                TileEntityPulverizer tep = (TileEntityPulverizer) te;
                return new GuiPulverizer(player.inventory, tep);
            case ELECFURNACE:
                TileEntityElectricFurnace teef = (TileEntityElectricFurnace) te;
                return new GuiElectricFurnace(player.inventory, teef);
            case SOLARPANEL:
                TileEntitySolarPanel tesp = (TileEntitySolarPanel) te;
                return new GuiSolarPanel((Container) getServerGuiElement(ID, player, world, x, y, z), player.inventory, tesp);
            case WINDMILL:
                TileEntityWindMill tewm = (TileEntityWindMill) te;
                return new GuiWindMill((Container) getServerGuiElement(ID, player, world, x, y, z), player.inventory, tewm);
            default:
                return null;
        }
    }
}

