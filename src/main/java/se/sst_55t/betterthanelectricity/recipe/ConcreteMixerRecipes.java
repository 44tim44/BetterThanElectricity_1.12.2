package se.sst_55t.betterthanelectricity.recipe;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * Created by Timeout on 2017-08-20.
 */
public class ConcreteMixerRecipes
{
    private static final ConcreteMixerRecipes SMELTING_BASE = new ConcreteMixerRecipes();
    /** The list of smelting results. */
    private final Map<ItemStack, ItemStack> smeltingList = Maps.<ItemStack, ItemStack>newHashMap();
    /** A list which contains how many experience points each recipe output will give. */
    private final Map<ItemStack, Float> experienceList = Maps.<ItemStack, Float>newHashMap();

    /**
     * Returns an instance of PulverizerRecipes.
     */
    public static ConcreteMixerRecipes instance()
    {
        return SMELTING_BASE;
    }

    private ConcreteMixerRecipes()
    {
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,0),
                new ItemStack(Blocks.CONCRETE,1,0), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,1),
                new ItemStack(Blocks.CONCRETE,1,1), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,2),
                new ItemStack(Blocks.CONCRETE,1,2), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,3),
                new ItemStack(Blocks.CONCRETE,1,3), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,4),
                new ItemStack(Blocks.CONCRETE,1,4), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,5),
                new ItemStack(Blocks.CONCRETE,1,5), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,6),
                new ItemStack(Blocks.CONCRETE,1,6), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,7),
                new ItemStack(Blocks.CONCRETE,1,7), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,8),
                new ItemStack(Blocks.CONCRETE,1,8), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,9),
                new ItemStack(Blocks.CONCRETE,1,9), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,10),
                new ItemStack(Blocks.CONCRETE,1,10), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,11),
                new ItemStack(Blocks.CONCRETE,1,11), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,12),
                new ItemStack(Blocks.CONCRETE,1,12), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,13),
                new ItemStack(Blocks.CONCRETE,1,13), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,14),
                new ItemStack(Blocks.CONCRETE,1,14), 0.1F);
        this.addSmeltingRecipe(
                new ItemStack(Blocks.CONCRETE_POWDER,1,15),
                new ItemStack(Blocks.CONCRETE,1,15), 0.1F);
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
