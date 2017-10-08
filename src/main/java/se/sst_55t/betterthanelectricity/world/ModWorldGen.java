package se.sst_55t.betterthanelectricity.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import se.sst_55t.betterthanelectricity.block.BlockCropCorn;
import se.sst_55t.betterthanelectricity.block.BlockPlantCorn;
import se.sst_55t.betterthanelectricity.block.ModBlocks;

import java.util.Random;

import static se.sst_55t.betterthanelectricity.block.BlockCropCorn.HALF;

/**
 * Created by Timmy on 2016-11-26.
 */
public class ModWorldGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ,
                         World world,
                         IChunkGenerator chunkGenerator,
                         IChunkProvider chunkProvider)
    {
        if (world.provider.getDimensionType() == DimensionType.OVERWORLD) { // the overworld
            generateOverworld(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
        }
    }

    private void generateOverworld(Random random, int chunkX, int chunkZ,
                                   World world,
                                   IChunkGenerator chunkGenerator,
                                   IChunkProvider chunkProvider)
    {
        generateOre(ModBlocks.oreCopper.getDefaultState(),
                world, random, chunkX * 16, chunkZ * 16,
                1, 64, 4 + random.nextInt(7), 8);

        generateOre(ModBlocks.oreTin.getDefaultState(),
                world, random, chunkX * 16, chunkZ * 16,
                1, 48, 4 + random.nextInt(7), 6);

        generateOre(ModBlocks.oreAluminum.getDefaultState(),
                world, random, chunkX * 16, chunkZ * 16,
                1, 32, 4 + random.nextInt(7), 6);

        generateOre(ModBlocks.oreRuby.getDefaultState(),
                world, random, chunkX * 16, chunkZ * 16,
                4, 32, 1, 3 + random.nextInt(6));

        generateOre(ModBlocks.oreSapphire.getDefaultState(),
                world, random, chunkX * 16, chunkZ * 16,
                4, 32, 1, 3 + random.nextInt(6));

        generateOre(Blocks.EMERALD_ORE.getDefaultState(),
                world, random, chunkX * 16, chunkZ * 16,
                4, 32, 1, 3 + random.nextInt(6));

        generateCorn(world, random, chunkX * 16, chunkZ * 16 + random.nextInt(20));
    }

    private void generateOre(IBlockState ore, World world, Random random,
                             int x, int z, int minY, int maxY,
                             int size, int chances)
    {
        int deltaY = maxY - minY;

        for (int i = 0; i < chances; i++) {
            BlockPos pos = new BlockPos(x + random.nextInt(16), minY + random.nextInt(deltaY), z + random.nextInt(16));

            WorldGenMinable generator = new WorldGenMinable(ore, size);
            generator.generate(world, random, pos);
        }
    }

    private void generatePlant(IBlockState plant, World world, Random rand,
                              int x, int z)
    {
        if(rand.nextInt(30) == 0) {
            int plantsInGroup = 3 + rand.nextInt(3); //between 7 and 14 plants per group.
            for(int i = 0; i < plantsInGroup; i++) {
                x = x + rand.nextInt(4);
                z = z + rand.nextInt(4);
                int y = world.getHeight(x, z);
                if(y > 0 && BlockCropCorn.canGrowOn(world.getBlockState(new BlockPos(x, y - 1, z)))) {
                    world.setBlockState(new BlockPos(x, y, z), plant.withProperty(BlockCrops.AGE,7));
                }
            }
        }
    }

    private void generateCorn(World world, Random rand,
                               int x, int z)
    {
        if(rand.nextInt(30) == 0) {
            int plantsInGroup = 3 + rand.nextInt(3); //between 7 and 14 plants per group.
            for(int i = 0; i < plantsInGroup; i++) {
                x = x + rand.nextInt(4);
                z = z + rand.nextInt(4);
                int y = world.getHeight(x, z);
                if(y > 0 && BlockPlantCorn.canGrowOn(world.getBlockState(new BlockPos(x, y - 1, z))) && world.isAirBlock(new BlockPos(x, y + 1, z))) {
                    world.setBlockState(new BlockPos(x, y, z), ModBlocks.plantCorn.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER));
                    world.setBlockState(new BlockPos(x, y+1, z), ModBlocks.plantCorn.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER));
                }
            }
        }
    }

}