package se.sst_55t.betterthanelectricity;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.EntityEntry;
import se.sst_55t.betterthanelectricity.block.ModBlocks;
import se.sst_55t.betterthanelectricity.entity.EntitySittableBlock;
import se.sst_55t.betterthanelectricity.entity.ModEntities;
import se.sst_55t.betterthanelectricity.item.ModItems;
import se.sst_55t.betterthanelectricity.network.PacketRequestUpdatePedestal;
import se.sst_55t.betterthanelectricity.network.PacketToServerJetpack;
import se.sst_55t.betterthanelectricity.network.PacketUpdatePedestal;
import se.sst_55t.betterthanelectricity.proxy.CommonProxy;
import se.sst_55t.betterthanelectricity.recipe.ModRecipes;
import se.sst_55t.betterthanelectricity.util.FallDamageEventHandler;
import se.sst_55t.betterthanelectricity.util.HUDEventHandler;
import se.sst_55t.betterthanelectricity.util.OnSpadeUseEventHandler;
import se.sst_55t.betterthanelectricity.util.WeatherEventHandler;
import se.sst_55t.betterthanelectricity.world.ModWorldGen;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.logging.Logger;

/**
 * Created by Timeout on 2017-08-20.
 */

@Mod(modid = BTEMod.MODID, name = BTEMod.MODNAME, version = BTEMod.VERSION)
public class BTEMod {

    public static final String MODID = "betterthanelectricity";
    public static final String MODID_short = "bte";
    public static final String MODNAME = "Better Than Electricity";
    public static final String VERSION = "1.0.33";
    public static final String CLIENT_PROXY = "se.sst_55t.betterthanelectricity.proxy.ClientProxy";
    public static final String COMMON_PROXY = "se.sst_55t.betterthanelectricity.proxy.CommonProxy";

    public static final Item.ToolMaterial copperToolMaterial = EnumHelper
            .addToolMaterial("COPPER", 2, 250, 6, 2, 14);

    public static final Item.ToolMaterial bronzeToolMaterial = EnumHelper
            .addToolMaterial("BRONZE", 2, 500, 8, 4, 20);

    public static final Item.ToolMaterial steelToolMaterial = EnumHelper
            .addToolMaterial("STEEL", 2, 1000, 10, 3, 20);

    public static final ItemArmor.ArmorMaterial steelArmorMaterial = EnumHelper
            .addArmorMaterial("STEEL",MODID + ":steel", 15, new int[]{2, 5, 6, 2}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);

    public static final ItemArmor.ArmorMaterial machineArmorMaterial = EnumHelper
            .addArmorMaterial("MACHINE",MODID + ":machine", -1, new int[]{0, 0, 0, 0}, 9, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);


    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    @Mod.Instance
    public static BTEMod instance;

    public static SimpleNetworkWrapper network;

    public static final Logger LOGGER = Logger.getLogger(MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        GameRegistry.registerWorldGenerator(new ModWorldGen(), 3);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModGuiHandler());

        proxy.registerRenderers();

        int id = 0;
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID_short);
        network.registerMessage(new PacketUpdatePedestal.Handler(), PacketUpdatePedestal.class, id++, Side.CLIENT);
        network.registerMessage(new PacketRequestUpdatePedestal.Handler(), PacketRequestUpdatePedestal.class, id++, Side.SERVER);
        network.registerMessage(new PacketToServerJetpack.Handler(), PacketToServerJetpack.class, id++, Side.SERVER);

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        ModRecipes.init();
        //EntityRegistry.registerModEntity(new ResourceLocation("betterthanelectricity:mountable_block"), EntitySittableBlock.class, "MountableBlock", 0, this, 80, 1, false);
        MinecraftForge.EVENT_BUS.register(new FallDamageEventHandler());
        MinecraftForge.EVENT_BUS.register(new WeatherEventHandler());
        MinecraftForge.EVENT_BUS.register(new OnSpadeUseEventHandler());
        MinecraftForge.EVENT_BUS.register(new HUDEventHandler());
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            ModBlocks.register(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
        {
            ModEntities.register(event.getRegistry());
        }

    }


}
