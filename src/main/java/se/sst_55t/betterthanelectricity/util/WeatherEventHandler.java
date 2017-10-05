package se.sst_55t.betterthanelectricity.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import se.sst_55t.betterthanelectricity.item.IChargeable;
import se.sst_55t.betterthanelectricity.item.ModItems;

/**
 * Created by Timeout on 2017-10-01.
 */
public class WeatherEventHandler {

    private int currentTick = 0;
    private final int MAX_WEATHER_TIME = 60;

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event){
        if (event.world.isRaining() || event.world.isThundering()){
            currentTick++;
            if(currentTick >= MAX_WEATHER_TIME*40){
                event.world.provider.resetRainAndThunder();
                currentTick = 0;
            }
        }
    }
}
