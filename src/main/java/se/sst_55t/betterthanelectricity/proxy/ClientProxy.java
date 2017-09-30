package se.sst_55t.betterthanelectricity.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.block.BlockDoorBase;
import se.sst_55t.betterthanelectricity.block.ModBlocks;
import se.sst_55t.betterthanelectricity.block.table.TESRTable;
import se.sst_55t.betterthanelectricity.block.table.TileEntityTable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.*;
import se.sst_55t.betterthanelectricity.item.ModItems;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2017-08-20.
 */
public class ClientProxy extends CommonProxy
{
    @Override
    public void registerItemRenderer(Item item, int meta, String id)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(BTEMod.MODID + ":" + id, "inventory"));
    }

    @Override
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTable.class, new TESRTable());
        ModelLoader.setCustomStateMapper(ModBlocks.glassDoor, (new StateMap.Builder()).ignore(BlockDoorBase.POWERED).build());
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        initColorsBlocksItems();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler
    {
        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event)
        {
            ModItems.registerModels();
            ModBlocks.registerModels();
        }

    }

    @SuppressWarnings("all")
    public void initColorsBlocksItems(){
        final BlockColors blockcolors = Minecraft.getMinecraft().getBlockColors();
        blockcolors.registerBlockColorHandler(new IBlockColor()
        {
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
            {
                return worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
            }
        }, ModBlocks.grass_slab);
        blockcolors.registerBlockColorHandler(new IBlockColor()
        {
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
            {
                return worldIn != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(worldIn, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
            }
        }, ModBlocks.grass_doubleslab);
        final ItemColors itemcolors = Minecraft.getMinecraft().getItemColors();
        itemcolors.registerItemColorHandler(new IItemColor()
        {
            public int getColorFromItemstack(ItemStack stack, int tintIndex)
            {
                IBlockState iblockstate = ((ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
                return blockcolors.colorMultiplier(iblockstate, (IBlockAccess)null, (BlockPos)null, tintIndex);
            }
        }, ModBlocks.grass_slab, ModBlocks.grass_doubleslab);
    }
}
