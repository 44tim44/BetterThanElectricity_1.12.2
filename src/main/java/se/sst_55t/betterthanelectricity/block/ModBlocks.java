package se.sst_55t.betterthanelectricity.block;

/**
 * Created by Timeout on 2017-08-20.
 */

import net.minecraft.init.Blocks;
import se.sst_55t.betterthanelectricity.ModEnums.BlockType;
import se.sst_55t.betterthanelectricity.block.chargingstation.BlockChargingStation;
import se.sst_55t.betterthanelectricity.block.compactor.BlockCompactor;
import se.sst_55t.betterthanelectricity.block.counter.BlockCounter;
import se.sst_55t.betterthanelectricity.block.electricfurnace.BlockElectricFurnace;
import se.sst_55t.betterthanelectricity.block.fuelgenerator.BlockFuelGenerator;
import se.sst_55t.betterthanelectricity.block.pulverizer.BlockPulverizer;
import se.sst_55t.betterthanelectricity.block.solarpanel.BlockSolarPanel;
import se.sst_55t.betterthanelectricity.block.table.BlockTable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import se.sst_55t.betterthanelectricity.block.windmill.BlockWindMill;

public class ModBlocks {

    /* Ores */
    public static BlockOre oreCopper = new BlockOre("ore_copper").setCreativeTab(CreativeTabs.MATERIALS);
    public static BlockOre oreTin = new BlockOre("ore_tin").setCreativeTab(CreativeTabs.MATERIALS);
    public static BlockOre oreAluminum = new BlockOre("ore_aluminum").setCreativeTab(CreativeTabs.MATERIALS);

    /* Machines */
    public static BlockCounter counter = new BlockCounter();
    public static BlockPulverizer pulverizer = (BlockPulverizer) new BlockPulverizer(false,"pulverizer_block").setCreativeTab(CreativeTabs.DECORATIONS).setHardness(2.0F);
    public static BlockPulverizer pulverizer_on = (BlockPulverizer) new BlockPulverizer(true,"pulverizer_block_on").setHardness(2.0F);
    public static BlockElectricFurnace electricFurnace = (BlockElectricFurnace) new BlockElectricFurnace(false,"electric_furnace_block").setCreativeTab(CreativeTabs.DECORATIONS).setHardness(2.0F);
    public static BlockElectricFurnace electricFurnace_on = (BlockElectricFurnace) new BlockElectricFurnace(true,"electric_furnace_block_on").setHardness(2.0F);
    public static BlockBase machineblock = new BlockBase(Material.ROCK,"machine_block").setCreativeTab(CreativeTabs.DECORATIONS).setHardness(2.0F);
    public static BlockSolarPanel solarPanel = (BlockSolarPanel)new BlockSolarPanel().setCreativeTab(CreativeTabs.DECORATIONS).setHardness(2.0F);
    public static BlockWindMill windMill = (BlockWindMill)new BlockWindMill(false, "wind_mill_block").setCreativeTab(CreativeTabs.DECORATIONS).setHardness(2.0F);
    public static BlockWindMill windMill_on = (BlockWindMill)new BlockWindMill(true, "wind_mill_block_on").setHardness(2.0F);
    public static BlockFuelGenerator fuelGenerator = (BlockFuelGenerator) new BlockFuelGenerator(false,"fuel_generator_block").setCreativeTab(CreativeTabs.DECORATIONS).setHardness(2.0F);
    public static BlockFuelGenerator fuelGenerator_on = (BlockFuelGenerator) new BlockFuelGenerator(true,"fuel_generator_block_on").setHardness(2.0F);
    public static BlockChargingStation chargingStation = (BlockChargingStation) new BlockChargingStation(false, "charging_station_block").setCreativeTab(CreativeTabs.DECORATIONS).setHardness(2.0F);
    public static BlockCompactor compactor = (BlockCompactor) new BlockCompactor(false,"compactor_block").setCreativeTab(CreativeTabs.DECORATIONS).setHardness(2.0F);
    public static BlockCompactor compactor_on = (BlockCompactor) new BlockCompactor(true,"compactor_block_on").setHardness(2.0F);

    /* Building Blocks */
    public static BlockBase stone_tile = new BlockBase(Material.ROCK,"stone_tile").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(5.0F);

    public static BlockBase planks_white = new BlockBase(Material.WOOD,"planks_white").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_lightgray = new BlockBase(Material.WOOD,"planks_lightgray").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_gray = new BlockBase(Material.WOOD,"planks_gray").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_black = new BlockBase(Material.WOOD,"planks_black").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_brown = new BlockBase(Material.WOOD,"planks_brown").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_red = new BlockBase(Material.WOOD,"planks_red").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_orange = new BlockBase(Material.WOOD,"planks_orange").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_yellow = new BlockBase(Material.WOOD,"planks_yellow").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_lime = new BlockBase(Material.WOOD,"planks_lime").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_green = new BlockBase(Material.WOOD,"planks_green").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_cyan = new BlockBase(Material.WOOD,"planks_cyan").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_lightblue = new BlockBase(Material.WOOD,"planks_lightblue").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_blue = new BlockBase(Material.WOOD,"planks_blue").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_purple = new BlockBase(Material.WOOD,"planks_purple").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_magenta = new BlockBase(Material.WOOD,"planks_magenta").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);
    public static BlockBase planks_pink = new BlockBase(Material.WOOD,"planks_pink").setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(2.0F);

    public static BlockSlabBase planks_slab_white = new BlockSlabBase(BlockType.WOOD,"planks_slab_white").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_lightgray = new BlockSlabBase(BlockType.WOOD,"planks_slab_lightgray").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_gray = new BlockSlabBase(BlockType.WOOD,"planks_slab_gray").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_black = new BlockSlabBase(BlockType.WOOD,"planks_slab_black").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_brown = new BlockSlabBase(BlockType.WOOD,"planks_slab_brown").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_red = new BlockSlabBase(BlockType.WOOD,"planks_slab_red").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_orange = new BlockSlabBase(BlockType.WOOD,"planks_slab_orange").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_yellow = new BlockSlabBase(BlockType.WOOD,"planks_slab_yellow").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_lime = new BlockSlabBase(BlockType.WOOD,"planks_slab_lime").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_green = new BlockSlabBase(BlockType.WOOD,"planks_slab_green").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_cyan = new BlockSlabBase(BlockType.WOOD,"planks_slab_cyan").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_lightblue = new BlockSlabBase(BlockType.WOOD,"planks_slab_lightblue").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_blue = new BlockSlabBase(BlockType.WOOD,"planks_slab_blue").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_purple = new BlockSlabBase(BlockType.WOOD,"planks_slab_purple").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_magenta = new BlockSlabBase(BlockType.WOOD,"planks_slab_magenta").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_slab_pink = new BlockSlabBase(BlockType.WOOD,"planks_slab_pink").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase planks_doubleslab_white = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_white").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_lightgray = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_lightgray").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_gray = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_gray").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_black = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_black").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_brown = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_brown").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_red = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_red").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_orange = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_orange").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_yellow = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_yellow").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_lime = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_lime").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_green = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_green").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_cyan = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_cyan").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_lightblue = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_lightblue").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_blue = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_blue").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_purple = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_purple").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_magenta = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_magenta").setHardness(2.0F);
    public static BlockSlabBase planks_doubleslab_pink = new BlockSlabDoubleBase(BlockType.WOOD,"planks_doubleslab_pink").setHardness(2.0F);

    public static BlockStairsBase planks_stairs_white = new BlockStairsBase(planks_white.getDefaultState(),"planks_stairs_white").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_lightgray = new BlockStairsBase(planks_lightgray.getDefaultState(),"planks_stairs_lightgray").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_gray = new BlockStairsBase(planks_gray.getDefaultState(),"planks_stairs_gray").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_black = new BlockStairsBase(planks_black.getDefaultState(),"planks_stairs_black").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_brown = new BlockStairsBase(planks_brown.getDefaultState(),"planks_stairs_brown").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_red = new BlockStairsBase(planks_red.getDefaultState(),"planks_stairs_red").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_orange = new BlockStairsBase(planks_orange.getDefaultState(),"planks_stairs_orange").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_yellow = new BlockStairsBase(planks_yellow.getDefaultState(),"planks_stairs_yellow").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_lime = new BlockStairsBase(planks_lime.getDefaultState(),"planks_stairs_lime").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_green = new BlockStairsBase(planks_green.getDefaultState(),"planks_stairs_green").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_cyan = new BlockStairsBase(planks_cyan.getDefaultState(),"planks_stairs_cyan").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_lightblue = new BlockStairsBase(planks_lightblue.getDefaultState(),"planks_stairs_lightblue").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_blue = new BlockStairsBase(planks_blue.getDefaultState(),"planks_stairs_blue").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_purple = new BlockStairsBase(planks_purple.getDefaultState(),"planks_stairs_purple").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_magenta = new BlockStairsBase(planks_magenta.getDefaultState(),"planks_stairs_magenta").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockStairsBase planks_stairs_pink = new BlockStairsBase(planks_pink.getDefaultState(),"planks_stairs_pink").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    public static BlockSlabBase smoothstone_slab = new BlockSlabBase(BlockType.SMOOTHSTONE,"smoothstone_slab").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabBase smoothstone_doubleslab = new BlockSlabDoubleBase(BlockType.SMOOTHSTONE,"smoothstone_doubleslab").setHardness(2.0F);
    public static BlockSlabGrass grass_slab = new BlockSlabGrass(BlockType.GRASS,"grass_slab").setHardness(0.6F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabGrass grass_doubleslab = new BlockSlabDoubleGrass(BlockType.GRASS,"grass_doubleslab").setHardness(0.6F);
    public static BlockSlabGrassSnowed grass_snowed_slab = new BlockSlabGrassSnowed(BlockType.GRASS,"grass_snowed_slab").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabGrassSnowed grass_snowed_doubleslab = new BlockSlabDoubleGrassSnowed(BlockType.GRASS,"grass_snowed_doubleslab");
    public static BlockSlabDirt dirt_slab = new BlockSlabDirt(BlockType.DIRT,"dirt_slab").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabDirt dirt_doubleslab = new BlockSlabDoubleDirt(BlockType.DIRT,"dirt_doubleslab");

    public static BlockSlabVerticalBase smoothstone_vertical_slab = new BlockSlabVerticalBase(BlockType.SMOOTHSTONE,"smoothstone_vertical_slab").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabVerticalBaseDouble smoothstone_vertical_doubleslab = new BlockSlabVerticalBaseDouble(BlockType.SMOOTHSTONE,"smoothstone_vertical_doubleslab").setHardness(2.0F);
    public static BlockSlabVerticalBase stone_vertical_slab = new BlockSlabVerticalBase(BlockType.VANILLASTONESLAB,"stone_vertical_slab").setHardness(2.0F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    public static BlockSlabVerticalBaseDouble stone_vertical_doubleslab = new BlockSlabVerticalBaseDouble(BlockType.VANILLASTONESLAB,"stone_vertical_doubleslab").setHardness(2.0F);

    public static BlockStairsBase smoothstone_stairs = new BlockStairsBase(Blocks.STONE.getDefaultState(),"smoothstone_stairs").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    /* Other */
    public static BlockCropCorn cropCorn = new BlockCropCorn();
    public static BlockPlantCorn plantCorn = new BlockPlantCorn("plant_corn");

    public static BlockChair chairOak = new BlockChair(Material.WOOD, SoundType.WOOD,"chair_oak");
    public static BlockChair chairSpruce = new BlockChair(Material.WOOD, SoundType.WOOD,"chair_spruce");
    public static BlockChair chairBirch = new BlockChair(Material.WOOD, SoundType.WOOD,"chair_birch");
    public static BlockChair chairJungle = new BlockChair(Material.WOOD, SoundType.WOOD,"chair_jungle");
    public static BlockChair chairAcacia = new BlockChair(Material.WOOD, SoundType.WOOD,"chair_acacia");
    public static BlockChair chairDarkOak = new BlockChair(Material.WOOD, SoundType.WOOD,"chair_dark_oak");

    public static BlockTable tableOak = new BlockTable(Material.WOOD,"table_oak");
    public static BlockTable tableSpruce = new BlockTable(Material.WOOD,"table_spruce");
    public static BlockTable tableBirch = new BlockTable(Material.WOOD,"table_birch");
    public static BlockTable tableJungle = new BlockTable(Material.WOOD,"table_jungle");
    public static BlockTable tableAcacia = new BlockTable(Material.WOOD,"table_acacia");
    public static BlockTable tableDarkOak = new BlockTable(Material.WOOD,"table_dark_oak");

    public static BlockBase glassDoor = new BlockDoorBase(Material.IRON,"glass_door",true).setHardness(5.0F);

    public static BlockBase electricLamp = new BlockElectricLamp(Material.CIRCUITS,"electric_lamp",false).setCreativeTab(CreativeTabs.DECORATIONS);
    public static BlockBase electricLamp_on = new BlockElectricLamp(Material.CIRCUITS,"electric_lamp_on",true);


    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                /* Ores */
                oreCopper,
                oreTin,
                oreAluminum,

                /* Plants */
                cropCorn,
                plantCorn,

                /* Machines */
                counter,
                pulverizer,
                pulverizer_on,
                electricFurnace,
                electricFurnace_on,
                machineblock,
                solarPanel,
                windMill,
                windMill_on,
                fuelGenerator,
                fuelGenerator_on,
                chargingStation,
                compactor,
                compactor_on,

                /* Building Blocks */
                stone_tile,

                planks_white,
                planks_lightgray,
                planks_gray,
                planks_black,
                planks_brown,
                planks_red,
                planks_orange,
                planks_yellow,
                planks_lime,
                planks_green,
                planks_cyan,
                planks_lightblue,
                planks_blue,
                planks_purple,
                planks_magenta,
                planks_pink,

                planks_slab_white,
                planks_slab_lightgray,
                planks_slab_gray,
                planks_slab_black,
                planks_slab_brown,
                planks_slab_red,
                planks_slab_orange,
                planks_slab_yellow,
                planks_slab_lime,
                planks_slab_green,
                planks_slab_cyan,
                planks_slab_lightblue,
                planks_slab_blue,
                planks_slab_purple,
                planks_slab_magenta,
                planks_slab_pink,
                planks_doubleslab_white,
                planks_doubleslab_lightgray,
                planks_doubleslab_gray,
                planks_doubleslab_black,
                planks_doubleslab_brown,
                planks_doubleslab_red,
                planks_doubleslab_orange,
                planks_doubleslab_yellow,
                planks_doubleslab_lime,
                planks_doubleslab_green,
                planks_doubleslab_cyan,
                planks_doubleslab_lightblue,
                planks_doubleslab_blue,
                planks_doubleslab_purple,
                planks_doubleslab_magenta,
                planks_doubleslab_pink,

                planks_stairs_white,
                planks_stairs_lightgray,
                planks_stairs_gray,
                planks_stairs_black,
                planks_stairs_brown,
                planks_stairs_red,
                planks_stairs_orange,
                planks_stairs_yellow,
                planks_stairs_lime,
                planks_stairs_green,
                planks_stairs_cyan,
                planks_stairs_lightblue,
                planks_stairs_blue,
                planks_stairs_purple,
                planks_stairs_magenta,
                planks_stairs_pink,

                smoothstone_slab,
                smoothstone_doubleslab,
                grass_slab,
                grass_doubleslab,
                grass_snowed_slab,
                grass_snowed_doubleslab,
                dirt_slab,
                dirt_doubleslab,

                smoothstone_vertical_slab,
                smoothstone_vertical_doubleslab,
                stone_vertical_slab,
                stone_vertical_doubleslab,

                smoothstone_stairs,

                /* Other */
                chairOak,
                chairSpruce,
                chairBirch,
                chairJungle,
                chairAcacia,
                chairDarkOak,
                tableOak,
                tableSpruce,
                tableBirch,
                tableJungle,
                tableAcacia,
                tableDarkOak,

                glassDoor,

                electricLamp,
                electricLamp_on

        );

        GameRegistry.registerTileEntity(pulverizer.getTileEntityClass(), pulverizer.getRegistryName().toString());
        GameRegistry.registerTileEntity(electricFurnace.getTileEntityClass(), electricFurnace.getRegistryName().toString());
        GameRegistry.registerTileEntity(solarPanel.getTileEntityClass(), solarPanel.getRegistryName().toString());
        GameRegistry.registerTileEntity(windMill.getTileEntityClass(), windMill.getRegistryName().toString());
        GameRegistry.registerTileEntity(fuelGenerator.getTileEntityClass(), fuelGenerator.getRegistryName().toString());
        GameRegistry.registerTileEntity(chargingStation.getTileEntityClass(),chargingStation.getRegistryName().toString());
        GameRegistry.registerTileEntity(compactor.getTileEntityClass(),compactor.getRegistryName().toString());
        GameRegistry.registerTileEntity(tableOak.getTileEntityClass(), tableOak.getRegistryName().toString());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                /* Ores */
                oreCopper.createItemBlock(),
                oreTin.createItemBlock(),
                oreAluminum.createItemBlock(),

                /* Machines */
                counter.createItemBlock(),
                pulverizer.createItemBlock(),
                pulverizer_on.createItemBlock(),
                electricFurnace.createItemBlock(),
                electricFurnace_on.createItemBlock(),
                machineblock.createItemBlock(),
                solarPanel.createItemBlock(),
                windMill.createItemBlock(),
                windMill_on.createItemBlock(),
                fuelGenerator.createItemBlock(),
                fuelGenerator_on.createItemBlock(),
                chargingStation.createItemBlock(),
                compactor.createItemBlock(),
                compactor_on.createItemBlock(),

                /* Building Blocks */
                stone_tile.createItemBlock(),

                planks_white.createItemBlock(),
                planks_lightgray.createItemBlock(),
                planks_gray.createItemBlock(),
                planks_black.createItemBlock(),
                planks_brown.createItemBlock(),
                planks_red.createItemBlock(),
                planks_orange.createItemBlock(),
                planks_yellow.createItemBlock(),
                planks_lime.createItemBlock(),
                planks_green.createItemBlock(),
                planks_cyan.createItemBlock(),
                planks_lightblue.createItemBlock(),
                planks_blue.createItemBlock(),
                planks_purple.createItemBlock(),
                planks_magenta.createItemBlock(),
                planks_pink.createItemBlock(),

                planks_slab_white.createItemSlab(planks_slab_white,planks_slab_white,planks_doubleslab_white),
                planks_slab_lightgray.createItemSlab(planks_slab_lightgray,planks_slab_lightgray,planks_doubleslab_lightgray),
                planks_slab_gray.createItemSlab(planks_slab_gray,planks_slab_gray,planks_doubleslab_gray),
                planks_slab_black.createItemSlab(planks_slab_black,planks_slab_black,planks_doubleslab_black),
                planks_slab_brown.createItemSlab(planks_slab_brown,planks_slab_brown,planks_doubleslab_brown),
                planks_slab_red.createItemSlab(planks_slab_red,planks_slab_red,planks_doubleslab_red),
                planks_slab_orange.createItemSlab(planks_slab_orange,planks_slab_orange,planks_doubleslab_orange),
                planks_slab_yellow.createItemSlab(planks_slab_yellow,planks_slab_yellow,planks_doubleslab_yellow),
                planks_slab_lime.createItemSlab(planks_slab_lime,planks_slab_lime,planks_doubleslab_lime),
                planks_slab_green.createItemSlab(planks_slab_green,planks_slab_green,planks_doubleslab_green),
                planks_slab_cyan.createItemSlab(planks_slab_cyan,planks_slab_cyan,planks_doubleslab_cyan),
                planks_slab_lightblue.createItemSlab(planks_slab_lightblue,planks_slab_lightblue,planks_doubleslab_lightblue),
                planks_slab_blue.createItemSlab(planks_slab_blue,planks_slab_blue,planks_doubleslab_blue),
                planks_slab_purple.createItemSlab(planks_slab_purple,planks_slab_purple,planks_doubleslab_purple),
                planks_slab_magenta.createItemSlab(planks_slab_magenta,planks_slab_magenta,planks_doubleslab_magenta),
                planks_slab_pink.createItemSlab(planks_slab_pink,planks_slab_pink,planks_doubleslab_pink),

                planks_stairs_white.createItemBlock(),
                planks_stairs_lightgray.createItemBlock(),
                planks_stairs_gray.createItemBlock(),
                planks_stairs_black.createItemBlock(),
                planks_stairs_brown.createItemBlock(),
                planks_stairs_red.createItemBlock(),
                planks_stairs_orange.createItemBlock(),
                planks_stairs_yellow.createItemBlock(),
                planks_stairs_lime.createItemBlock(),
                planks_stairs_green.createItemBlock(),
                planks_stairs_cyan.createItemBlock(),
                planks_stairs_lightblue.createItemBlock(),
                planks_stairs_blue.createItemBlock(),
                planks_stairs_purple.createItemBlock(),
                planks_stairs_magenta.createItemBlock(),
                planks_stairs_pink.createItemBlock(),

                smoothstone_slab.createItemSlab(smoothstone_slab,smoothstone_slab,smoothstone_doubleslab),
                grass_slab.createItemSlab(grass_slab,grass_slab,grass_doubleslab),
                grass_snowed_slab.createItemSlab(grass_snowed_slab,grass_snowed_slab,grass_snowed_doubleslab),
                dirt_slab.createItemSlab(dirt_slab,dirt_slab,dirt_doubleslab),

                smoothstone_vertical_slab.createItemSlab(smoothstone_vertical_slab,smoothstone_vertical_slab,smoothstone_vertical_doubleslab),
                stone_vertical_slab.createItemSlab(stone_vertical_slab,stone_vertical_slab,stone_vertical_doubleslab),

                smoothstone_stairs.createItemBlock(),

                /* Other */
                chairOak.createItemBlock(),
                chairSpruce.createItemBlock(),
                chairBirch.createItemBlock(),
                chairJungle.createItemBlock(),
                chairAcacia.createItemBlock(),
                chairDarkOak.createItemBlock(),
                tableOak.createItemBlock(),
                tableSpruce.createItemBlock(),
                tableBirch.createItemBlock(),
                tableJungle.createItemBlock(),
                tableAcacia.createItemBlock(),
                tableDarkOak.createItemBlock(),

                electricLamp.createItemBlock(),
                electricLamp_on.createItemBlock()
        );
    }

    public static void registerModels() {
        oreCopper.registerItemModel(Item.getItemFromBlock(oreCopper));
        oreTin.registerItemModel(Item.getItemFromBlock(oreTin));
        oreAluminum.registerItemModel(Item.getItemFromBlock(oreAluminum));
        counter.registerItemModel(Item.getItemFromBlock(counter));
        pulverizer.registerItemModel(Item.getItemFromBlock(pulverizer));
        pulverizer_on.registerItemModel(Item.getItemFromBlock(pulverizer_on));
        electricFurnace.registerItemModel(Item.getItemFromBlock(electricFurnace));
        electricFurnace_on.registerItemModel(Item.getItemFromBlock(electricFurnace_on));
        machineblock.registerItemModel(Item.getItemFromBlock(machineblock));
        solarPanel.registerItemModel(Item.getItemFromBlock(solarPanel));
        windMill.registerItemModel(Item.getItemFromBlock(windMill));
        windMill_on.registerItemModel(Item.getItemFromBlock(windMill_on));
        fuelGenerator.registerItemModel(Item.getItemFromBlock(fuelGenerator));
        fuelGenerator_on.registerItemModel(Item.getItemFromBlock(fuelGenerator_on));
        chargingStation.registerItemModel(Item.getItemFromBlock(chargingStation));
        compactor.registerItemModel(Item.getItemFromBlock(compactor));
        compactor_on.registerItemModel(Item.getItemFromBlock(compactor_on));

        stone_tile.registerItemModel(Item.getItemFromBlock(stone_tile));

        planks_white.registerItemModel(Item.getItemFromBlock(planks_white));
        planks_lightgray.registerItemModel(Item.getItemFromBlock(planks_lightgray));
        planks_gray.registerItemModel(Item.getItemFromBlock(planks_gray));
        planks_black.registerItemModel(Item.getItemFromBlock(planks_black));
        planks_brown.registerItemModel(Item.getItemFromBlock(planks_brown));
        planks_red.registerItemModel(Item.getItemFromBlock(planks_red));
        planks_orange.registerItemModel(Item.getItemFromBlock(planks_orange));
        planks_yellow.registerItemModel(Item.getItemFromBlock(planks_yellow));
        planks_lime.registerItemModel(Item.getItemFromBlock(planks_lime));
        planks_green.registerItemModel(Item.getItemFromBlock(planks_green));
        planks_cyan.registerItemModel(Item.getItemFromBlock(planks_cyan));
        planks_lightblue.registerItemModel(Item.getItemFromBlock(planks_lightblue));
        planks_blue.registerItemModel(Item.getItemFromBlock(planks_blue));
        planks_purple.registerItemModel(Item.getItemFromBlock(planks_purple));
        planks_magenta.registerItemModel(Item.getItemFromBlock(planks_magenta));
        planks_pink.registerItemModel(Item.getItemFromBlock(planks_pink));

        planks_slab_white.registerItemModel(Item.getItemFromBlock(planks_slab_white));
        planks_slab_lightgray.registerItemModel(Item.getItemFromBlock(planks_slab_lightgray));
        planks_slab_gray.registerItemModel(Item.getItemFromBlock(planks_slab_gray));
        planks_slab_black.registerItemModel(Item.getItemFromBlock(planks_slab_black));
        planks_slab_brown.registerItemModel(Item.getItemFromBlock(planks_slab_brown));
        planks_slab_red.registerItemModel(Item.getItemFromBlock(planks_slab_red));
        planks_slab_orange.registerItemModel(Item.getItemFromBlock(planks_slab_orange));
        planks_slab_yellow.registerItemModel(Item.getItemFromBlock(planks_slab_yellow));
        planks_slab_lime.registerItemModel(Item.getItemFromBlock(planks_slab_lime));
        planks_slab_green.registerItemModel(Item.getItemFromBlock(planks_slab_green));
        planks_slab_cyan.registerItemModel(Item.getItemFromBlock(planks_slab_cyan));
        planks_slab_lightblue.registerItemModel(Item.getItemFromBlock(planks_slab_lightblue));
        planks_slab_blue.registerItemModel(Item.getItemFromBlock(planks_slab_blue));
        planks_slab_purple.registerItemModel(Item.getItemFromBlock(planks_slab_purple));
        planks_slab_magenta.registerItemModel(Item.getItemFromBlock(planks_slab_magenta));
        planks_slab_pink.registerItemModel(Item.getItemFromBlock(planks_slab_pink));

        planks_stairs_white.registerItemModel(Item.getItemFromBlock(planks_stairs_white));
        planks_stairs_lightgray.registerItemModel(Item.getItemFromBlock(planks_stairs_lightgray));
        planks_stairs_gray.registerItemModel(Item.getItemFromBlock(planks_stairs_gray));
        planks_stairs_black.registerItemModel(Item.getItemFromBlock(planks_stairs_black));
        planks_stairs_brown.registerItemModel(Item.getItemFromBlock(planks_stairs_brown));
        planks_stairs_red.registerItemModel(Item.getItemFromBlock(planks_stairs_red));
        planks_stairs_orange.registerItemModel(Item.getItemFromBlock(planks_stairs_orange));
        planks_stairs_yellow.registerItemModel(Item.getItemFromBlock(planks_stairs_yellow));
        planks_stairs_lime.registerItemModel(Item.getItemFromBlock(planks_stairs_lime));
        planks_stairs_green.registerItemModel(Item.getItemFromBlock(planks_stairs_green));
        planks_stairs_cyan.registerItemModel(Item.getItemFromBlock(planks_stairs_cyan));
        planks_stairs_lightblue.registerItemModel(Item.getItemFromBlock(planks_stairs_lightblue));
        planks_stairs_blue.registerItemModel(Item.getItemFromBlock(planks_stairs_blue));
        planks_stairs_purple.registerItemModel(Item.getItemFromBlock(planks_stairs_purple));
        planks_stairs_magenta.registerItemModel(Item.getItemFromBlock(planks_stairs_magenta));
        planks_stairs_pink.registerItemModel(Item.getItemFromBlock(planks_stairs_pink));

        smoothstone_slab.registerItemModel(Item.getItemFromBlock(smoothstone_slab));
        grass_slab.registerItemModel(Item.getItemFromBlock(grass_slab));
        grass_snowed_slab.registerItemModel(Item.getItemFromBlock(grass_snowed_slab));
        dirt_slab.registerItemModel(Item.getItemFromBlock(dirt_slab));

        smoothstone_vertical_slab.registerItemModel(Item.getItemFromBlock(smoothstone_vertical_slab));
        stone_vertical_slab.registerItemModel(Item.getItemFromBlock(stone_vertical_slab));

        smoothstone_stairs.registerItemModel(Item.getItemFromBlock(smoothstone_stairs));

        chairOak.registerItemModel(Item.getItemFromBlock(chairOak));
        chairSpruce.registerItemModel(Item.getItemFromBlock(chairSpruce));
        chairBirch.registerItemModel(Item.getItemFromBlock(chairBirch));
        chairJungle.registerItemModel(Item.getItemFromBlock(chairJungle));
        chairAcacia.registerItemModel(Item.getItemFromBlock(chairAcacia));
        chairDarkOak.registerItemModel(Item.getItemFromBlock(chairDarkOak));
        tableOak.registerItemModel(Item.getItemFromBlock(tableOak));
        tableSpruce.registerItemModel(Item.getItemFromBlock(tableSpruce));
        tableBirch.registerItemModel(Item.getItemFromBlock(tableBirch));
        tableJungle.registerItemModel(Item.getItemFromBlock(tableJungle));
        tableAcacia.registerItemModel(Item.getItemFromBlock(tableAcacia));
        tableDarkOak.registerItemModel(Item.getItemFromBlock(tableDarkOak));

        electricLamp.registerItemModel(Item.getItemFromBlock(electricLamp));
        electricLamp_on.registerItemModel(Item.getItemFromBlock(electricLamp_on));

    }

}