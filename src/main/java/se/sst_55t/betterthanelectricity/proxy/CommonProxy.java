package se.sst_55t.betterthanelectricity.proxy;


import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


/**
 * Created by Timeout on 2017-08-20.
 */
public class CommonProxy {
    public void registerItemRenderer(Item item, int meta, String id) {}

    public void registerRenderers(){}

    public void preInit(FMLPreInitializationEvent event){}

    public void init(FMLInitializationEvent event){}

    public void postInit(FMLPostInitializationEvent event){}

}
