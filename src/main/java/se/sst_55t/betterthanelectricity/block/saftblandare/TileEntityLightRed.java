package se.sst_55t.betterthanelectricity.block.saftblandare;

/**
 * Created by Timeout on 2017-08-20.
 */

import com.elytradev.mirage.lighting.IColoredLight;
import com.elytradev.mirage.lighting.Light;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface="com.elytradev.mirage.lighting.IColoredLight", modid="mirage")
public class TileEntityLightRed extends TileEntity implements IColoredLight{

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }


    @Optional.Method(modid="mirage")
    @Override
    public Light getColoredLight() {
        return Light.builder()
                .pos(this.getPos())
                .color(1,0,0)
                .radius(5)
                .build();
    }
}