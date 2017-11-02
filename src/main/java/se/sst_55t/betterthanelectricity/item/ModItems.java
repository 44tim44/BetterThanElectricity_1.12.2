package se.sst_55t.betterthanelectricity.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.block.ModBlocks;
import se.sst_55t.betterthanelectricity.item.tool.*;
import net.minecraft.creativetab.CreativeTabs;
//import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by Timeout on 2017-08-20.
 */
public class ModItems {
    public static ItemBase copperDust = new ItemBase("copper_dust").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase tinDust = new ItemBase("tin_dust").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase aluminumDust = new ItemBase("aluminum_dust").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase bronzeDust = new ItemBase("bronze_dust").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase ironDust = new ItemBase("iron_dust").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase goldDust = new ItemBase("gold_dust").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase coalDust = new ItemBase("coal_dust").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase carbonatedIronDust = new ItemBase("carbonated_iron_dust").setCreativeTab(CreativeTabs.MATERIALS);


    public static ItemBase ingotCopper = new ItemBase("ingot_copper").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase ingotTin = new ItemBase("ingot_tin").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase ingotAluminum = new ItemBase("ingot_aluminum").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase ingotSteel = new ItemBase("ingot_steel").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase ingotBronze = new ItemBase("ingot_bronze").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase ruby = new ItemBase("ruby").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase sapphire = new ItemBase("sapphire").setCreativeTab(CreativeTabs.MATERIALS);

    public static ItemBase coalLump = new ItemBase("coal_lump").setCreativeTab(CreativeTabs.MATERIALS);
    public static ItemBase coalChunk = new ItemBase("coal_chunk").setCreativeTab(CreativeTabs.MATERIALS);

    public static ItemBase woodSap = new ItemBase("wood_sap").setCreativeTab(CreativeTabs.MISC);
    public static ItemBase rubber = new ItemBase("rubber").setCreativeTab(CreativeTabs.MISC);

    //Tools
    public static ItemBase steelRod = new ItemBase("steel_rod").setCreativeTab(CreativeTabs.MATERIALS);

    public static ItemKnife moraKnife = new ItemKnife(BTEMod.steelToolMaterial,"mora_knife");
    public static ItemLightsaberOff lightsaberRedOff = new ItemLightsaberOff("lightsaber_red_off");
    public static ItemLightsaberOff lightsaberGreenOff = new ItemLightsaberOff("lightsaber_green_off");
    public static ItemLightsaberOff lightsaberBlueOff = new ItemLightsaberOff("lightsaber_blue_off");
    public static ItemLightsaber lightsaberRed = new ItemLightsaber("lightsaber_red");
    public static ItemLightsaber lightsaberGreen = new ItemLightsaber("lightsaber_green");
    public static ItemLightsaber lightsaberBlue = new ItemLightsaber("lightsaber_blue");
    public static ItemSword steelSword = new ItemSword(BTEMod.steelToolMaterial,"steel_sword");
    public static ItemSword bronzeSword = new ItemSword(BTEMod.bronzeToolMaterial,"bronze_sword");

    public static ItemPickaxe steelPickaxe = new ItemPickaxe(BTEMod.steelToolMaterial,"steel_pickaxe");
    public static ItemPickaxe bronzePickaxe = new ItemPickaxe(BTEMod.bronzeToolMaterial,"bronze_pickaxe");

    public static ItemAxe steelAxe = new ItemAxe(BTEMod.steelToolMaterial,"steel_axe");
    public static ItemAxe bronzeAxe = new ItemAxe(BTEMod.bronzeToolMaterial,"bronze_axe");

    public static ItemShovel steelShovel = new ItemShovel(BTEMod.steelToolMaterial,"steel_shovel");
    public static ItemShovel bronzeShovel = new ItemShovel(BTEMod.bronzeToolMaterial,"bronze_shovel");

    public static ItemWoodTap woodTap = new ItemWoodTap("wood_tap");

    // Food and crops
    public static ItemCornSeed cornSeed = new ItemCornSeed();
    public static ItemFoodBase corn = new ItemFoodBase("corn",2,0.6F,false).setCreativeTab(CreativeTabs.FOOD);
    public static ItemFoodBase cornCooked = new ItemFoodBase("corn_cooked",5,0.6F,false).setCreativeTab(CreativeTabs.FOOD);
    public static ItemFoodBase popcorn = new ItemFoodBase("popcorn",2,0.6F,false).setCreativeTab(CreativeTabs.FOOD);

    public static ItemFoodBase cookedEgg = new ItemFoodBase("cooked_egg",2,0.6F,false).setCreativeTab(CreativeTabs.FOOD);

    // Electric Stuff
    public static ItemBattery battery = new ItemBattery();
    public static ItemBatteryPack batteryPack = new ItemBatteryPack();
    public static ItemBase electricCircuit = new ItemBase("electric_circuit").setCreativeTab(CreativeTabs.MISC);
    public static ItemBase electricCircuitAdvanced = new ItemBase("electric_circuit_advanced").setCreativeTab(CreativeTabs.MISC);

    public static ItemMiningDrill miningDrill = new ItemMiningDrill();
    public static ItemBase drillHead = new ItemBase("drill_head").setCreativeTab(CreativeTabs.MISC);

    public static ItemChainsaw chainsaw = new ItemChainsaw();
    public static ItemBase chainsawHead = new ItemBase("chainsaw_head").setCreativeTab(CreativeTabs.MISC);

    public static ItemBase glassDoorItem = new ItemDoorCustom(ModBlocks.glassDoor,"glass_door_item").setCreativeTab(CreativeTabs.REDSTONE);

    // Armor
    public static ItemArmorCustom steelHelmet = new ItemArmorCustom(BTEMod.steelArmorMaterial, EntityEquipmentSlot.HEAD, "steel_helmet");
    public static ItemArmorCustom steelChestplate = new ItemArmorCustom(BTEMod.steelArmorMaterial, EntityEquipmentSlot.CHEST, "steel_chestplate");
    public static ItemArmorCustom steelLeggings = new ItemArmorCustom(BTEMod.steelArmorMaterial, EntityEquipmentSlot.LEGS, "steel_leggings");
    public static ItemArmorCustom steelBoots = new ItemArmorCustom(BTEMod.steelArmorMaterial, EntityEquipmentSlot.FEET, "steel_boots");

    public static ItemGravityBoots gravityBoots = new ItemGravityBoots(BTEMod.machineArmorMaterial, EntityEquipmentSlot.FEET, "gravity_boots");
    public static ItemJetpack jetpack = new ItemJetpack(BTEMod.machineArmorMaterial, EntityEquipmentSlot.CHEST,"jetpack");

    public static ItemSubmarine submarine_item = (ItemSubmarine) new ItemSubmarine("submarine_item").setCreativeTab(CreativeTabs.TRANSPORTATION);
    public static ItemBase propeller = new ItemBase("propeller").setCreativeTab(CreativeTabs.MISC);

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                copperDust,
                tinDust,
                aluminumDust,
                bronzeDust,
                ironDust,
                goldDust,
                coalDust,
                carbonatedIronDust,

                ingotCopper,
                ingotTin,
                ingotAluminum,
                ingotSteel,
                ingotBronze,
                ruby,
                sapphire,

                coalLump,
                coalChunk,

                woodSap,
                rubber,

                steelRod,

                moraKnife,
                lightsaberRedOff,
                lightsaberGreenOff,
                lightsaberBlueOff,
                lightsaberRed,
                lightsaberGreen,
                lightsaberBlue,
                steelSword,
                bronzeSword,

                steelPickaxe,
                bronzePickaxe,
                steelAxe,
                bronzeAxe,
                steelShovel,
                bronzeShovel,

                woodTap,

                cornSeed,
                corn,
                cornCooked,
                popcorn,
                cookedEgg,

                battery,
                batteryPack,
                electricCircuit,
                electricCircuitAdvanced,

                miningDrill,
                drillHead,

                chainsaw,
                chainsawHead,

                glassDoorItem,

                steelHelmet,
                steelChestplate,
                steelLeggings,
                steelBoots,

                gravityBoots,
                jetpack,

                submarine_item,
                propeller
        );
    }

    public static void registerModels() {
        copperDust.registerItemModel();
        tinDust.registerItemModel();
        aluminumDust.registerItemModel();
        bronzeDust.registerItemModel();
        ironDust.registerItemModel();
        goldDust.registerItemModel();
        coalDust.registerItemModel();
        carbonatedIronDust.registerItemModel();

        ingotCopper.registerItemModel();
        ingotTin.registerItemModel();
        ingotAluminum.registerItemModel();
        ingotSteel.registerItemModel();
        ingotBronze.registerItemModel();
        ruby.registerItemModel();
        sapphire.registerItemModel();

        coalLump.registerItemModel();
        coalChunk.registerItemModel();
        woodSap.registerItemModel();
        rubber.registerItemModel();

        steelRod.registerItemModel();

        moraKnife.registerItemModel();
        lightsaberRedOff.registerItemModel();
        lightsaberGreenOff.registerItemModel();
        lightsaberBlueOff.registerItemModel();
        lightsaberRed.registerItemModel();
        lightsaberGreen.registerItemModel();
        lightsaberBlue.registerItemModel();
        steelSword.registerItemModel();
        bronzeSword.registerItemModel();
        steelPickaxe.registerItemModel();
        bronzePickaxe.registerItemModel();
        steelAxe.registerItemModel();
        bronzeAxe.registerItemModel();
        steelShovel.registerItemModel();
        bronzeShovel.registerItemModel();

        woodTap.registerItemModel();

        cornSeed.registerItemModel();
        corn.registerItemModel();
        cornCooked.registerItemModel();
        popcorn.registerItemModel();
        cookedEgg.registerItemModel();


        battery.registerItemModel();
        batteryPack.registerItemModel();
        electricCircuit.registerItemModel();
        electricCircuitAdvanced.registerItemModel();

        miningDrill.registerItemModel();
        drillHead.registerItemModel();

        chainsaw.registerItemModel();
        chainsawHead.registerItemModel();

        glassDoorItem.registerItemModel();

        steelHelmet.registerItemModel();
        steelChestplate.registerItemModel();
        steelLeggings.registerItemModel();
        steelBoots.registerItemModel();

        gravityBoots.registerItemModel();
        jetpack.registerItemModel();

        submarine_item.registerItemModel();
        propeller.registerItemModel();
    }

}