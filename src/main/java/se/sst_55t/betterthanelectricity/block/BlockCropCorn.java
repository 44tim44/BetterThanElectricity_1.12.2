package se.sst_55t.betterthanelectricity.block;

/**
 * Created by Timeout on 2017-08-20.
 */
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.item.ModItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;

import java.util.Random;

public class BlockCropCorn extends BlockCrops {

    public static final PropertyEnum<BlockDoublePlant.EnumBlockHalf> HALF = PropertyEnum.<BlockDoublePlant.EnumBlockHalf>create("half", BlockDoublePlant.EnumBlockHalf.class);
    private static final AxisAlignedBB[] CROPS_LOWER_AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};
    private static final AxisAlignedBB[] CROPS_UPPER_AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.625D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)};

    public BlockCropCorn() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), Integer.valueOf(0)).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER));
        this.setTickRandomly(true);
        setUnlocalizedName("crop_corn");
        setRegistryName("crop_corn");
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        if(state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.LOWER) {
            return CROPS_LOWER_AABB[((Integer) state.getValue(this.getAgeProperty())).intValue()];
        }
        else
        {
            return CROPS_UPPER_AABB[((Integer) state.getValue(this.getAgeProperty())).intValue()];
        }
    }

    /**
     * Return true if the block can sustain a Bush
     */
    @Override
    protected boolean canSustainBush(IBlockState state)
    {
        return state.getBlock() == Blocks.FARMLAND;
    }

    public static boolean canGrowOn(IBlockState state)
    {
        return state.getBlock() == Blocks.FARMLAND;
    }

    @Override
    protected Item getSeed() {
        return ModItems.cornSeed;
    }

    @Override
    protected Item getCrop() {
        return ModItems.corn;
    }

    // Below methods is for double functionality


    @Override
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
            return super.getItemDropped(state, rand, fortune);
        }
    }

    public void placeAt(World worldIn, BlockPos lowerPos, BlockDoublePlant.EnumPlantType variant, int flags)
    {
        worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER), flags);
        worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), flags);
    }


    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if(state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.LOWER) {
            int bottomAge = state.getValue(AGE);
            worldIn.setBlockState(pos.up(), this.withAge(bottomAge).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
        }
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
        if((meta & 8) != 0)
        {
            return this.withAge(meta-8).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER);
        }
        return this.withAge(meta).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER);
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = this.getAge(state);
        if(state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
        {
            i |= 8;
        }

        return i;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE, HALF});
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        this.checkAndDropBlock(worldIn, pos, state);

        if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
        {
            int i = this.getAge(state);

            if (i < this.getMaxAge())
            {
                float f = getGrowthChance(this, worldIn, pos);

                if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0))
                {
                    if(state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
                        worldIn.setBlockState(pos, this.withAge(i+1).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
                        worldIn.setBlockState(pos.offset(EnumFacing.DOWN),this.withAge(i+1).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER),2);
                    }
                    else
                    {
                        worldIn.setBlockState(pos.offset(EnumFacing.UP), this.withAge(i+1).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
                        worldIn.setBlockState(pos,this.withAge(i+1).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER),2);
                    }
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
                }
            }
        }
    }

    @Override
    public void grow(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
        int j = this.getMaxAge();

        if (i > j)
        {
            i = j;
        }

        if(state.getValue(HALF) == BlockDoublePlant.EnumBlockHalf.UPPER) {
            worldIn.setBlockState(pos, this.withAge(i).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
            worldIn.setBlockState(pos.offset(EnumFacing.DOWN),this.withAge(i).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER),2);
        }
        else
        {
            worldIn.setBlockState(pos.offset(EnumFacing.UP), this.withAge(i).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.UPPER), 2);
            worldIn.setBlockState(pos,this.withAge(i).withProperty(HALF, BlockDoublePlant.EnumBlockHalf.LOWER),2);
        }
    }

    @Override
    public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos)
    {
        return net.minecraftforge.common.EnumPlantType.Crop;
    }
}