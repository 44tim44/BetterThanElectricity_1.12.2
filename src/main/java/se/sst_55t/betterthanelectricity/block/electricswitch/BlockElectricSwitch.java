package se.sst_55t.betterthanelectricity.block.electricswitch;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.block.BlockTileEntity;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2017-11-01.
 */
public class BlockElectricSwitch extends BlockTileEntity<TileEntityElectricSwitch>
{
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockElectricSwitch()
    {
        super(Material.IRON, "block_electric_switch");
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

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {POWERED});
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWERED, Boolean.valueOf((meta & 2) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        if (state.getValue(POWERED).booleanValue())
        {
            i |= 2;
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
