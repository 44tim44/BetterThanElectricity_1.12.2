package se.sst_55t.betterthanelectricity.block.cementmixer;


import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.ModGuiHandler;
import se.sst_55t.betterthanelectricity.block.BlockContainerBase;
import se.sst_55t.betterthanelectricity.block.ModBlocks;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Timmy on 2016-11-27.
 */
public class BlockConcreteMixer extends BlockContainerBase {

    private final boolean isActive;
    private static boolean keepInventory;

    public BlockConcreteMixer(boolean isActive, String name) {
        super(Material.ROCK, name);
        this.isActive = isActive;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.concrete_mixer);
    }

    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            TileEntity tileentity = world.getTileEntity(pos);
            if (player.isSneaking()) {
            } else {
                player.openGui(BTEMod.instance, ModGuiHandler.CONCRETEMIXER, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;

        if (active)
        {
            worldIn.setBlockState(pos, ModBlocks.concrete_mixer_on.getDefaultState(), 3);
            worldIn.setBlockState(pos, ModBlocks.concrete_mixer_on.getDefaultState(), 3);
        }
        else
        {
            worldIn.setBlockState(pos, ModBlocks.concrete_mixer.getDefaultState(), 3);
            worldIn.setBlockState(pos, ModBlocks.concrete_mixer.getDefaultState(), 3);
        }

        keepInventory = false;

        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    public Class<TileEntityConcreteMixer> getTileEntityClass() {
        return TileEntityConcreteMixer.class;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntityConcreteMixer createNewTileEntity(World world, int meta)
    {
        return new TileEntityConcreteMixer();
    }


    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {

        if (stack.hasDisplayName())
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityConcreteMixer)
            {
                ((TileEntityConcreteMixer)tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!keepInventory)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityConcreteMixer)
            {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityConcreteMixer)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ModBlocks.concrete_mixer);
    }

    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }


    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(this.getRegistryName());
    }
}
