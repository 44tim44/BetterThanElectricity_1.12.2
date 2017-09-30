package se.sst_55t.betterthanelectricity;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

/**
 * Created by Timeout on 2017-09-21.
 */
public class ModEnums
{
    public enum BlockType implements IStringSerializable {
        SMOOTHSTONE("smoothstone",MaterialType.ROCK),
        GRASS("grass",MaterialType.GRASS),
        DIRT("dirt",MaterialType.GROUND),
        WOOD("wood", MaterialType.WOOD),
        COPPER("copper",MaterialType.IRON),
        TIN("tin",MaterialType.IRON),
        BRONZE("bronze",MaterialType.IRON),
        STEEL("steel",MaterialType.IRON),
        VANILLASTONESLAB("stone",MaterialType.ROCK);

        final String name;
        final MaterialType materialType;

        BlockType(String name, MaterialType materialType) {
            this.name = name;
            this.materialType = materialType;
        }

        @Override
        public String getName() {
            return name;
        }

        public MaterialType getMaterialType()
        {
            return materialType;
        }

    }

    public enum FireType {
        WOOD(5, 20),
        DOUBLE_WOOD(5, 20),
        STAIR(5, 20),
        SLAB(5, 20),
        LOG(5, 5),
        LEAF(30, 60),
        TNT(16, 100),
        PLANT(60, 100),
        WOOL(30, 100),
        VINE(15, 100),
        COAL(5, 5),
        HAY(60, 20),
        CARPET(60, 20),
        TUDOR(1, 1);

        private int fireSpread;
        private int flamability;

        FireType(int flamability, int fireSpread) {
            this.flamability = flamability;
            this.fireSpread = fireSpread;
        }

        public int getFireSpread() {
            return fireSpread;
        }

        public int getFlamability() {
            return flamability;
        }
    }

    public enum MaterialType {

        AIR(Material.AIR, null),
        GRASS(Material.GRASS, SoundType.PLANT),
        GROUND(Material.GROUND, SoundType.GROUND),
        WOOD(Material.WOOD, SoundType.WOOD),
        ROCK(Material.ROCK, SoundType.STONE),
        STONE(Material.ROCK, SoundType.STONE),
        IRON(Material.IRON, SoundType.METAL),
        METAL(Material.IRON, SoundType.METAL),
        ANVIL(Material.ANVIL, SoundType.ANVIL),
        WATER(Material.WATER, null),
        LAVA(Material.LAVA, null),
        LEAVES(Material.LEAVES, SoundType.PLANT),
        PLANTS(Material.PLANTS, SoundType.PLANT),
        VINE(Material.VINE, SoundType.PLANT),
        SPONGE(Material.SPONGE, null),
        CLOTH(Material.CLOTH, SoundType.CLOTH),
        FIRE(Material.FIRE, null),
        SAND(Material.SAND, null),
        CIRCUITS(Material.CIRCUITS, null),
        CARPET(Material.CARPET, SoundType.CLOTH),
        GLASS(Material.GLASS, SoundType.GLASS),
        REDSTONE_LIGHT(Material.REDSTONE_LIGHT, null),
        TNT(Material.TNT, null),
        CORAL(Material.CORAL, null),
        ICE(Material.ICE, null),
        PACKED_ICE(Material.PACKED_ICE, null),
        SNOW(Material.SNOW, SoundType.SNOW),
        CRAFTED_SNOW(Material.CRAFTED_SNOW, SoundType.SNOW),
        CACTUS(Material.CACTUS, null),
        CLAY(Material.CLAY, null),
        GOURD(Material.GOURD, SoundType.GROUND),
        DRAGON_EGG(Material.DRAGON_EGG, null),
        PORTAL(Material.PORTAL, null),
        CAKE(Material.CAKE, null),
        WEB(Material.WEB, null);

        public final Material material;
        public final SoundType sound;

        MaterialType(Material material, SoundType sound) {
            this.material = material;
            this.sound = sound;
        }

        public Material getMaterial() {
            return material;

        }

        public SoundType getSound() {
            return sound;
        }
    }

    public enum ColorType {
        OBSIDIAN_BLACK(8476209, MapColor.OBSIDIAN),
        BLACK(1644825, MapColor.BLACK),
        RED(10040115, MapColor.RED),
        TNT_RED(16711680, MapColor.TNT),
        NETHERRACK_RED(7340544, MapColor.NETHERRACK),
        GRASS_GREEN(8368696, MapColor.GRASS),
        FOLIAGE_GREEN(31744, MapColor.FOLIAGE),
        EMERALD_GREEN(55610, MapColor.EMERALD),
        GREEN(6717235, MapColor.GREEN),
        DIRT_BROWN(9923917, MapColor.DIRT),
        WOOD_BROWN(9402184, MapColor.WOOD),
        BROWN(6704179, MapColor.BROWN),
        ICE_BLUE(10526975, MapColor.ICE),
        WATER_BLUE(4210943, MapColor.WATER),
        LAPIS_BLUE(4882687, MapColor.LAPIS),
        DIAMOND_BLUE(6085589, MapColor.DIAMOND),
        BLUE(3361970, MapColor.BLUE),
        PURPLE(8339378, MapColor.PURPLE),
        CYAN(5013401, MapColor.CYAN),
        SILVER_LIGHT_GRAY(10066329, MapColor.SILVER),
        CLAY_GRAY(10791096, MapColor.CLAY),
        STONE_GRAY(7368816, MapColor.STONE),
        GRAY(5000268, MapColor.GRAY),
        PINK(15892389, MapColor.PINK),
        LIME(8375321, MapColor.LIME),
        SAND_YELLOW(16247203, MapColor.SAND),
        CLOTH_YELLOW(13092807, MapColor.CLOTH),
        GOLD_YELLOW(16445005, MapColor.GOLD),
        YELLOW(15066419, MapColor.YELLOW),
        LIGHT_BLUE(6724056, MapColor.LIGHT_BLUE),
        MAGENTA(11685080, MapColor.MAGENTA),
        ADOBE_ORANGE(14188339, MapColor.ADOBE),
        IRON_WHITE(10987431, MapColor.IRON),
        QUARTZ_WHITE(16776437, MapColor.QUARTZ),
        SNOW_WHITE(16777215, MapColor.SNOW),
        AIR(0, MapColor.AIR);

        public final int value;
        public final MapColor colorMap;

        ColorType(int value, MapColor colorMap) {
            this.value = value;
            this.colorMap = colorMap;
        }

        public int getDecimal() {
            return value;
        }

        public MapColor getMapColor() {
            return colorMap;
        }
    }
}
