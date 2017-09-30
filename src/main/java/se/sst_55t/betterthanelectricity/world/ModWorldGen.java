package se.sst_55t.betterthanelectricity.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import se.sst_55t.betterthanelectricity.block.ModBlocks;

import java.util.Random;

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

}