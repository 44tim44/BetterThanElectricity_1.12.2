package se.sst_55t.betterthanelectricity.util;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;

/**
 * Created by Timeout on 2017-10-03.
 */
@Mod.EventBusSubscriber
public class VanillaRecipeDeleter {
    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        ResourceLocation stoneSlabLocation = new ResourceLocation("minecraft:stone_slab");
        IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) event.getRegistry();
        modRegistry.remove(stoneSlabLocation);
    }
}
