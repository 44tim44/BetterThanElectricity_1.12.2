package se.sst_55t.betterthanelectricity.block.solarpanel;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraftforge.items.ItemStackHandler;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.ModGuiHandler;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.block.BlockSlabVerticalBase;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2017-08-22.
 */
public class BlockSolarPanel extends Block implements ITileEntityProvider{

    public BlockSolarPanel() {
        super(Material.IRON);
        this.isBlockContainer = true;
        this.setUnlocalizedName("solar_panel_block");
        this.setRegistryName("solar_panel_block");
        this.setHardness(2.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setLightOpacity(0);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ){

        if (world.isRemote) {

            final TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof TileEntitySolarPanel && !tile.isInvalid()) {

                final TileEntitySolarPanel panel = (TileEntitySolarPanel) tile;
            }
        }
        if (!world.isRemote) {
            if (player.isSneaking()) {
                // ...
            } else {
                player.openGui(BTEMod.instance, ModGuiHandler.SOLARPANEL, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }

        return true;
    }

    @Override
    public void breakBlock (World world, BlockPos pos, IBlockState state) {

        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TileEntitySolarPanel)
        {
            ItemStackHandler stacks = ((TileEntitySolarPanel) tileentity).getContents();
            for(int i = 0; i < stacks.getSlots(); i++)
            {
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(),pos.getZ(),stacks.extractItem(i,64,false));
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySolarPanel();
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(this.getRegistryName());
    }

    public Class<TileEntitySolarPanel> getTileEntityClass() {
        return TileEntitySolarPanel.class;
    }

    public void registerItemModel(Item itemBlock) {
        BTEMod.proxy.registerItemRenderer(itemBlock, 0, "solar_panel_block");
    }
}
