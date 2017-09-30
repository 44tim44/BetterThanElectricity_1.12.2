package se.sst_55t.betterthanelectricity.recipe;

import se.sst_55t.betterthanelectricity.block.ModBlocks;
import se.sst_55t.betterthanelectricity.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Timeout on 2017-08-20.
 */
public class ModRecipes {
    public static void init() {
        GameRegistry.addSmelting(ModBlocks.oreCopper,
                new ItemStack(ModItems.ingotCopper), 0.7f);
        GameRegistry.addSmelting(ModBlocks.oreTin,
                new ItemStack(ModItems.ingotTin), 0.7f);
        GameRegistry.addSmelting(ModBlocks.oreAluminum,
                new ItemStack(ModItems.ingotAluminum), 0.7f);

        GameRegistry.addSmelting(ModItems.bronzeDust,
                new ItemStack(ModItems.ingotBronze), 0.7f);
        GameRegistry.addSmelting(ModItems.carbonatedIronDust,
                new ItemStack(ModItems.ingotSteel), 0.7f);
        GameRegistry.addSmelting(ModItems.ironDust,
                new ItemStack(Items.IRON_INGOT), 0.7f);
        GameRegistry.addSmelting(ModItems.goldDust,
                new ItemStack(Items.GOLD_INGOT), 0.7f);
        GameRegistry.addSmelting(ModItems.copperDust,
                new ItemStack(ModItems.ingotCopper), 0.7f);
        GameRegistry.addSmelting(ModItems.aluminumDust,
                new ItemStack(ModItems.ingotAluminum), 0.7f);
        GameRegistry.addSmelting(ModItems.tinDust,
                new ItemStack(ModItems.ingotTin), 0.7f);
    }
}
