package se.sst_55t.betterthanelectricity.block.electricswitch;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.block.BlockTileEntity;
import se.sst_55t.betterthanelectricity.block.ModBlocks;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2017-11-01.
 */
public class BlockElectricSwitch extends BlockTileEntity<TileEntityElectricSwitch>
{
    public static final PropertyDirection FACING = BlockDirectional.FACING;

    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockElectricSwitch()
    {
        super(Material.ROCK, "block_electric_switch");
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED,Boolean.valueOf(false)));
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            boolean flag = worldIn.isBlockPowered(pos);

            if (flag || blockIn.getDefaultState().canProvidePower())
            {
                boolean flag1 = state.getValue(POWERED).booleanValue();

                if (flag1 != flag)
                {
                    worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(flag)), 2);
                }
            }
        }
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);
    }


    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, POWERED});
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        boolean flag = (meta & 8) > 0;
        meta -= 8;
        IBlockState iblockstate = this.getDefaultState();
        iblockstate = iblockstate.withProperty(FACING, EnumFacing.getFront(meta));
        return iblockstate.withProperty(POWERED, Boolean.valueOf(flag));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = ((EnumFacing)state.getValue(FACING)).getIndex();
        if (state.getValue(POWERED).booleanValue())
        {
            i |= 8;
        }
        return i;
    }

    @Override
    public Class<TileEntityElectricSwitch> getTileEntityClass()
    {
        return TileEntityElectricSwitch.class;
    }

    @Nullable
    @Override
    public TileEntityElectricSwitch createTileEntity(World world, IBlockState state)
    {
        return new TileEntityElectricSwitch();
    }
}
