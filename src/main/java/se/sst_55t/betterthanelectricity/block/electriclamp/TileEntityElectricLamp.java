package se.sst_55t.betterthanelectricity.block.electriclamp;

import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.ILightEventConsumer;
import com.elytradev.mirage.lighting.Light;
//import elucent.albedo.lighting.Light;
import elucent.albedo.lighting.ILightProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2018-03-23.
 */
@Optional.Interface(iface="com.elytradev.mirage.lighting.ILightEventConsumer", modid="mirage")
//@Optional.Interface(iface="elucent.albedo.lighting.ILightProvider", modid="albedo")
public class TileEntityElectricLamp extends TileEntity implements ILightEventConsumer {



    @Optional.Method(modid="mirage")
    @Override
    public void gatherLights(GatherLightsEvent gatherLightsEvent)
    {
        float r = 0;
        float g = 0;
        float b = 0;

        switch (((BlockElectricLamp)world.getBlockState(this.pos).getBlock()).color)
        {
            case 0: // white
                r = 1;
                g = 1;
                b = 1;
                break;
            case 1: // orange
                r = 1;
                g = 0.5F;
                b = 0;
                break;
            case 2: // magenta
                r = 1;
                g = 0;
                b = 1;
                break;
            case 3: // lightblue
                r = 0;
                g = 0.625F;
                b = 1;
                break;
            case 4: // yellow
                r = 1;
                g = 1;
                b = 0;
                break;
            case 5: // lime
                r = 0;
                g = 1;
                b = 0.25F;
                break;
            case 6: // pink
                r = 1;
                g = 0.5F;
                b = 0.75F;
                break;
            case 7: // gray
                r = 0.33F;
                g = 0.33F;
                b = 0.33F;
                break;
            case 8: // lightgray
                r = 0.66F;
                g = 0.66F;
                b = 0.66F;
                break;
            case 9: // cyan
                r = 0;
                g = 0.75F;
                b = 0.75F;
                break;
            case 10: // purple
                r = 0.5F;
                g = 0;
                b = 0.5F;
                break;
            case 11: // blue
                r = 0;
                g = 0;
                b = 1;
                break;
            case 12: // brown
                r = 0.375F;
                g = 0.25F;
                b = 0.125F;
                break;
            case 13: // green
                r = 0;
                g = 0.5F;
                b = 0;
                break;
            case 14: // red
                r = 1;
                g = 0;
                b = 0;
                break;
            case 15: // black
                r = 0;
                g = 0;
                b = 0;
                break;
            default:
                r = 1;
                g = 1;
                b = 1;
                break;
        }

        if(((BlockElectricLamp)this.getBlockType()).isOn)
        {
            gatherLightsEvent.add(Light.builder()
                    .pos(this.pos)
                    .color(r, g, b)
                    .radius(15)
                    .build());
        }
    }


    /*
    //@Nullable
    @Optional.Method(modid="albedo")
    @Override
    public Light provideLight() {

        float r;
        float g;
        float b;

        switch (((BlockElectricLamp)this.getBlockType()).color)
        {
            case 0: // white
                r = 1.0F;
                g = 1.0F;
                b = 1.0F;
                break;
            case 1: // orange
                r = 1.0F;
                g = 0.5F;
                b = 0.0F;
                break;
            case 2: // magenta
                r = 1.0F;
                g = 0.0F;
                b = 1.0F;
                break;
            case 3: // lightblue
                r = 0.0F;
                g = 0.625F;
                b = 1.0F;
                break;
            case 4: // yellow
                r = 1.0F;
                g = 1.0F;
                b = 0.0F;
                break;
            case 5: // lime
                r = 0.0F;
                g = 1.0F;
                b = 0.25F;
                break;
            case 6: // pink
                r = 1.0F;
                g = 0.5F;
                b = 0.75F;
                break;
            case 7: // gray
                r = 0.33F;
                g = 0.33F;
                b = 0.33F;
                break;
            case 8: // lightgray
                r = 0.66F;
                g = 0.66F;
                b = 0.66F;
                break;
            case 9: // cyan
                r = 0.0F;
                g = 0.75F;
                b = 0.75F;
                break;
            case 10: // purple
                r = 0.5F;
                g = 0.0F;
                b = 0.5F;
                break;
            case 11: // blue
                r = 0.0F;
                g = 0.0F;
                b = 1.0F;
                break;
            case 12: // brown
                r = 0.375F;
                g = 0.25F;
                b = 0.125F;
                break;
            case 13: // green
                r = 0.0F;
                g = 0.5F;
                b = 0.0F;
                break;
            case 14: // red
                r = 1.0F;
                g = 0.0F;
                b = 0.0F;
                break;
            case 15: // black
                r = 0.0F;
                g = 0.0F;
                b = 0.0F;
                break;
            default:
                r = 1.0F;
                g = 1.0F;
                b = 1.0F;
                break;
        }

        return Light.builder()
                .pos(getPos())
                .color(r, g, b)
                .radius(15)
                .build();
    }
    */

}
