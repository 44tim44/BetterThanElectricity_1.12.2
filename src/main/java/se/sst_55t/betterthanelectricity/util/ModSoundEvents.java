package se.sst_55t.betterthanelectricity.util;


import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import se.sst_55t.betterthanelectricity.BTEMod;

/**
 * Registers this mod's {@link SoundEvent}s.
 *
 * @author Choonster
 */
@SuppressWarnings("WeakerAccess")
@ObjectHolder(BTEMod.MODID)
public class ModSoundEvents {

    @ObjectHolder("record.solaris")
    public static final SoundEvent DRILL_SPIN = createSoundEvent("drill.spin");
    public static final SoundEvent DOOR_SLIDE = createSoundEvent("door.slide");


    /**
     * Create a {@link SoundEvent}.
     *
     * @param soundName The SoundEvent's name without the testmod3 prefix
     * @return The SoundEvent
     */
    private static SoundEvent createSoundEvent(final String soundName) {
        final ResourceLocation soundID = new ResourceLocation(BTEMod.MODID, soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }

    @Mod.EventBusSubscriber(modid = BTEMod.MODID)
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().registerAll(
                    DRILL_SPIN,
                    DOOR_SLIDE
            );
        }
    }
}