package se.sst_55t.betterthanelectricity.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import se.sst_55t.betterthanelectricity.item.IChargeable;
import se.sst_55t.betterthanelectricity.item.ModItems;

/**
 * Created by Timeout on 2017-10-01.
 */
public class FallDamageEventHandler {

    @SubscribeEvent
    public void livingFall(LivingFallEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            ItemStack bootStack = ((EntityPlayer) event.getEntityLiving()).inventory.armorInventory.get(0);
            if (!bootStack.isEmpty())
            {
                if (bootStack.getItem() == ModItems.gravityBoots)
                {
                    BlockPos pos = event.getEntityLiving().getPosition();
                    if(event.getDistance() > 3)
                    {
                        if (((IChargeable) bootStack.getItem()).getCharge(bootStack) > 0) {
                            ((IChargeable) bootStack.getItem()).decreaseCharge(bootStack);
                            event.setDamageMultiplier(0);
                        }
                    }
                }
            }
        }
    }
}
