package se.sst_55t.betterthanelectricity.recipe;

import se.sst_55t.betterthanelectricity.block.ModBlocks;
import se.sst_55t.betterthanelectricity.item.ModItems;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * Created by Timeout on 2017-08-20.
 */
public class PulverizerRecipes
{
    private static final PulverizerRecipes SMELTING_BASE = new PulverizerRecipes();
    /** The list of smelting results. */
    private final Map<ItemStack, ItemStack> smeltingList = Maps.<ItemStack, ItemStack>newHashMap();
    /** A list which contains how many experience points each recipe output will give. */
    private final Map<ItemStack, Float> experienceList = Maps.<ItemStack, Float>newHashMap();

    /**
     * Returns an instance of PulverizerRecipes.
     */
    public static PulverizerRecipes instance()
    {
        return SMELTING_BASE;
    }

    private PulverizerRecipes()
    {
        this.addSmeltingRecipeForBlock(Blocks.IRON_ORE, new ItemStack
                (ModItems.ironDust,2), 0.7F);
        this.addSmeltingRecipeForBlock(Blocks.GOLD_ORE, new ItemStack
                (ModItems.goldDust,2), 1.0F);
        this.addSmelting(Items.COAL, new ItemStack
                (ModItems.coalDust,2), 0.7F);
        this.addSmeltingRecipeForBlock(ModBlocks.oreCopper, new ItemStack
                (ModItems.copperDust,2), 0.7F);
        this.addSmeltingRecipeForBlock(ModBlocks.oreTin, new ItemStack
                (ModItems.tinDust,2), 1.0F);
        this.addSmeltingRecipeForBlock(ModBlocks.oreAluminum, new ItemStack
                (ModItems.aluminumDust,2), 1.0F);


        this.addSmeltingRecipeForBlock(Blocks.COBBLESTONE, new ItemStack
                (Blocks.GRAVEL,2), 0.1F);
        this.addSmeltingRecipeForBlock(Blocks.STONE, new ItemStack
                (Blocks.GRAVEL,2), 0.1F);
        this.addSmeltingRecipeForBlock(Blocks.GRAVEL, new ItemStack(Blocks
                .SAND,2), 0.1F);
    }

    /**
     * Adds a smelting recipe, where the input item is an instance of Block.
     */
    public void addSmeltingRecipeForBlock(Block input, ItemStack stack, float experience)
    {
        this.addSmelting(Item.getItemFromBlock(input), stack, experience);
    }

    /**
     * Adds a smelting recipe using an Item as the input item.
     */
    public void addSmelting(Item input, ItemStack stack, float experience)
    {
        this.addSmeltingRecipe(new ItemStack(input, 1, 32767), stack, experience);
    }

    /**
     * Adds a smelting recipe using an ItemStack as the input for the recipe.
     */
    public void addSmeltingRecipe(ItemStack input, ItemStack stack, float experience)
    {
        if (getSmeltingResult(input) != ItemStack.EMPTY) { net.minecraftforge.fml.common.FMLLog.info("Ignored smelting recipe with conflicting input: " + input + " = " + stack); return; }
        this.smeltingList.put(input, stack);
        this.experienceList.put(stack, Float.valueOf(experience));
    }

    /**
     * Returns the smelting result of an item.
     */
    public ItemStack getSmeltingResult(ItemStack stack)
    {
        for (Map.Entry<ItemStack, ItemStack> entry : this.smeltingList.entrySet())
        {
            if (this.compareItemStacks(stack, entry.getKey()))
            {
                return entry.getValue();
            }
        }

        return ItemStack.EMPTY;
    }

    /**
     * Compares two itemstacks to ensure that they are the same. This checks both the item and the metadata of the item.
     */
    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2)
    {
        return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
    }

    public Map<ItemStack, ItemStack> getSmeltingList()
    {
        return this.smeltingList;
    }

    public float getSmeltingExperience(ItemStack stack)
    {
        float ret = stack.getItem().getSmeltingExperience(stack);
        if (ret != -1) return ret;

        for (Map.Entry<ItemStack, Float> entry : this.experienceList.entrySet())
        {
            if (this.compareItemStacks(stack, (ItemStack)entry.getKey()))
            {
                return ((Float)entry.getValue()).floatValue();
            }
        }

        return 0.0F;
    }
}
