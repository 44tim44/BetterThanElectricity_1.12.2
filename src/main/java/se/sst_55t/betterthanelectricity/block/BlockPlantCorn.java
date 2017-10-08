package se.sst_55t.betterthanelectricity.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.item.ModItems;

import java.util.Random;

/**
 * Created by Timeout on 2017-08-20.
 */


public class BlockPlantCorn extends BlockBush {

    protected String name;
    public static final PropertyEnum<BlockDoublePlant.EnumBlockHalf> HALF = PropertyEnum.<BlockDoublePlant.EnumBlockHalf>create("half", BlockDoublePlant.EnumBlockHalf.class);

    public BlockPlantCorn(String name)
    {
        this.name = name;
        this.setDefaultState(this.blockState.getBaseState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER));
        setUnlocalizedName(name);
        setRegistryName(name);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FULL_BLOCK_AABB;
    }

    /**
     * Return true if the block can sustain a Bush
     */
    @Override
    protected boolean canSustainBush(IBlockState state)
    {
        return state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS;
    }

    public static boolean canGrowOn(IBlockState state)
    {
        return state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.GRASS;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
    }

    @Override
    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(worldIn, pos, state))
        {
            boolean flag = state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER;
            BlockPos blockpos = flag ? pos : pos.up();
            BlockPos blockpos1 = flag ? pos.down() : pos;
            Block block = (Block)(flag ? this : worldIn.getBlockState(blockpos).getBlock());
            Block block1 = (Block)(flag ? worldIn.getBlockState(blockpos1).getBlock() : this);

            if (!flag) this.dropBlockAsItem(worldIn, pos, state, 0); //Forge move above the setting to air.

            if (block == this)
            {
                worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
            }

            if (block1 == this)
            {
                worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 3);
            }
        }
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() != this) return super.canBlockStay(worldIn, pos, state); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        if (state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
        {
            return worldIn.getBlockState(pos.down()).getBlock() == this;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.up());
            return iblockstate.getBlock() == this && super.canBlockStay(worldIn, pos, iblockstate);
        }
    }

    protected Item getSeed() {
        return ModItems.cornSeed;
    }

    protected Item getCrop() {
        return ModItems.corn;
    }

    @Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        super.getDrops(drops, world, pos, state, 0);

        Random rand = world instanceof World ? ((World)world).rand : new Random();
        int k = 3 + fortune;
        for (int i = 0; i < k; ++i)
        {
            if (rand.nextInt(14) <= 7)
            {
                drops.add(new ItemStack(this.getSeed(), 1, 0));
            }
        }
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);

        if (false && !worldIn.isRemote) // Forge: NOP all this.
        {
            int j = 3 + fortune;
            for (int k = 0; k < j; ++k)
            {
                if (worldIn.rand.nextInt(14) <= 7)
                {
                    spawnAsEntity(worldIn, pos, new ItemStack(this.getSeed()));
                }
            }
        }
    }


    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        if (state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
        {
            return Items.AIR;
        }
        else
        {
            return getCrop();
        }
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ModItems.cornSeed);
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
    }

    /**
     * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually
     * collect this block
     */
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
        {
            if (worldIn.getBlockState(pos.down()).getBlock() == this)
            {
                if (player.capabilities.isCreativeMode)
                {
                    worldIn.setBlockToAir(pos.down());
                }
                else
                {
                    worldIn.destroyBlock(pos.down(), true);
                }
            }
        }
        else if (worldIn.getBlockState(pos.up()).getBlock() == this)
        {
            worldIn.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */

    public IBlockState getStateFromMeta(int meta)
    {
        if(meta == 1)
        {
            return this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER);
        }
        return this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER);
    }

    public int getMetaFromState(IBlockState state)
    {
        if(state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
        {
            return 1;
        }

        return 0;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {HALF});
    }



    public void registerItemModel(Item itemBlock) {
        BTEMod.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(this.getRegistryName());
    }

    @Override
    public BlockPlantCorn setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    /**
     * Sets how many hits it takes to break a block.
     */
    @Override
    public BlockPlantCorn setHardness(float hardness)
    {
        this.blockHardness = hardness;

        if (this.blockResistance < hardness * 5.0F)
        {
            this.blockResistance = hardness * 5.0F;
        }

        return this;
    }
}