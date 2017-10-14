package se.sst_55t.betterthanelectricity.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.entity.item.EntitySubmarine;

/**
 * Created by Timeout on 2017-10-14.
 */
public class ModEntities {

    public static void register(IForgeRegistry<EntityEntry> registry) {
        registry.registerAll(
                createBuilder("submarine")
                        .entity(EntitySubmarine.class)
                        .tracker(64, 20, false)
                        .build()
        );
    }

    private static int entityID = 0;

    /**
     * Create an {@link EntityEntryBuilder} with the specified unlocalised/registry name and an automatically-assigned network ID.
     *
     * @param name The name
     * @param <E>  The entity type
     * @return The builder
     */
    private static <E extends Entity> EntityEntryBuilder<E> createBuilder(final String name) {
        final EntityEntryBuilder<E> builder = EntityEntryBuilder.create();
        final ResourceLocation registryName = new ResourceLocation(BTEMod.MODID, name);
        return builder.id(registryName, entityID++).name(registryName.toString());
    }
}
