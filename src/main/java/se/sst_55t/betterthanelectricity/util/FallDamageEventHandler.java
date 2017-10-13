package se.sst_55t.betterthanelectricity.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
            ItemStack chestplateStack = ((EntityPlayer) event.getEntityLiving()).inventory.armorInventory.get(2);
            if(!chestplateStack.isEmpty())
            {
                if(chestplateStack.getItem() == ModItems.jetpack)
                {
                    if(((EntityPlayer) event.getEntityLiving()).motionY > -0.3)
                    {
                        event.setDistance(0);
                    }
                    else if(((EntityPlayer) event.getEntityLiving()).motionY > -0.4)
                    {
                        if(!(event.getDistance() < 1)) event.setDistance(1);
                    }
                    else if(((EntityPlayer) event.getEntityLiving()).motionY > -0.6)
                    {
                        if(!(event.getDistance() < 2)) event.setDistance(2);
                    }
                    else if(((EntityPlayer) event.getEntityLiving()).motionY > -0.7)
                    {
                        if(!(event.getDistance() < 3)) event.setDistance(3);
                    }
                    else if(((EntityPlayer) event.getEntityLiving()).motionY > -0.8)
                    {
                        if(!(event.getDistance() < 4)) event.setDistance(4);
                    }
                    else if(((EntityPlayer) event.getEntityLiving()).motionY > -0.85)
                    {
                        if(!(event.getDistance() < 5)) event.setDistance(5);
                    }
                    else if(((EntityPlayer) event.getEntityLiving()).motionY > -0.9)
                    {
                        if(!(event.getDistance() < 6)) event.setDistance(6);
                    }
                    else if(((EntityPlayer) event.getEntityLiving()).motionY > -1.0)
                    {
                        if(!(event.getDistance() < 7)) event.setDistance(7);
                    }
                    else if(((EntityPlayer) event.getEntityLiving()).motionY > -1.05)
                    {
                        if(!(event.getDistance() < 8)) event.setDistance(8);
                    }
                    else if(((EntityPlayer) event.getEntityLiving()).motionY > -1.1)
                    {
                        if(!(event.getDistance() < 9)) event.setDistance(9);
                    }
                    else if(((EntityPlayer) event.getEntityLiving()).motionY > -1.15)
                    {
                        if(!(event.getDistance() < 10)) event.setDistance(10);

                    }

                }
            }

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
