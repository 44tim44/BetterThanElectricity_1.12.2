package se.sst_55t.betterthanelectricity.util.submarine;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import se.sst_55t.betterthanelectricity.entity.item.EntitySubmarine;

/**
 * Created by Timeout on 2017-10-14.
 */
public class SubmarineFactory implements IRenderFactory<EntitySubmarine> {

    public static final SubmarineFactory INSTANCE = new SubmarineFactory();

    @Override
    public Render<? super EntitySubmarine> createRenderFor(RenderManager manager) {
        return new RenderSubmarine(manager);
    }
}