package se.sst_55t.betterthanelectricity.block.windmill;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.ModGuiHandler;
import se.sst_55t.betterthanelectricity.block.ModBlocks;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2017-08-22.
 */
public class BlockWindMill extends Block implements ITileEntityProvider{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private final boolean isActive;
    private static boolean keepInventory;
    protected String name;

    public BlockWindMill(boolean isActive, String name) {
        super(Material.ROCK);
        this.name = name;
        this.isActive = isActive;
        this.isBlockContainer = true;
        setUnlocalizedName(name);
        setRegistryName(name);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setLightOpacity(0);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){

        if (world.isRemote) {

            final TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileEntityWindMill && !tile.isInvalid()) {

                final TileEntityWindMill windMill = (TileEntityWindMill) tile;
            }
        }
        if (!world.isRemote) {
            if (player.isSneaking()) {
                // ...
            } else {
                player.openGui(BTEMod.instance, ModGuiHandler.WINDMILL, world, pos.getX(), pos.getY(), pos.getZ());
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
            worldIn.setBlockState(pos, ModBlocks.windMill_on.getDefaultState()
                    .withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, ModBlocks.windMill_on.getDefaultState()
                    .withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }
        else
        {
            worldIn.setBlockState(pos, ModBlocks.windMill.getDefaultState()
                    .withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, ModBlocks.windMill.getDefaultState()
                    .withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }

        keepInventory = false;

        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    @Override
    public void breakBlock (World world, BlockPos pos, IBlockState state) {

        if (!keepInventory)
        {
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity instanceof TileEntityWindMill) {
                ItemStackHandler stacks = ((TileEntityWindMill) tileentity).getContents();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stacks.extractItem(i, 64, false));
                }
            }
        }

        super.breakBlock(world, pos, state);
        world.removeTileEntity(pos);
    }

    @Override
    public boolean eventReceived (IBlockState state, World world, BlockPos pos, int id, int param) {

        super.eventReceived(state, world, pos, id, param);
        final TileEntity tileentity = world.getTileEntity(pos);
        return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = this.getDefaultState();
        switch (placer.getHorizontalFacing()){
            case NORTH:
            default:
                return iblockstate.withProperty(FACING, EnumFacing.SOUTH);
            case SOUTH:
                return iblockstate.withProperty(FACING, EnumFacing.NORTH);
            case EAST:
                return iblockstate.withProperty(FACING, EnumFacing.WEST);
            case WEST:
                return iblockstate.withProperty(FACING, EnumFacing.EAST);
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();
        switch (meta){
            case 0:
                return iblockstate.withProperty(FACING, EnumFacing.DOWN);
            case 1:
                return iblockstate.withProperty(FACING, EnumFacing.UP);
            case 2:
                return iblockstate.withProperty(FACING, EnumFacing.NORTH);
            case 3:
                return iblockstate.withProperty(FACING, EnumFacing.SOUTH);
            case 4:
                return iblockstate.withProperty(FACING, EnumFacing.WEST);
            case 5:
                return iblockstate.withProperty(FACING, EnumFacing.EAST);
        }
        return iblockstate.withProperty(FACING, EnumFacing.NORTH);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        switch ((EnumFacing)state.getValue(FACING)){
            case DOWN:
                return 0;
            case UP:
                return 1;
            case NORTH:
                return 2;
            case SOUTH:
                return 3;
            case WEST:
                return 4;
            case EAST:
                return 5;
        }
        return 2;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityWindMill();
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(this.getRegistryName());
    }

    public Class<TileEntityWindMill> getTileEntityClass() {
        return TileEntityWindMill.class;
    }

    public void registerItemModel(Item itemBlock) {
        BTEMod.proxy.registerItemRenderer(itemBlock, 0, name);
    }
}
