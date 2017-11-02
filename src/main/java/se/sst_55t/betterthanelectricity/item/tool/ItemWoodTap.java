package se.sst_55t.betterthanelectricity.item.tool;

import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.block.ModBlocks;
import se.sst_55t.betterthanelectricity.item.ItemBase;
import se.sst_55t.betterthanelectricity.item.ModItems;

/**
 * Created by Timeout on 2017-11-02.
 */
public class ItemWoodTap extends ItemBase {
    public ItemWoodTap(String name) {
        super(name);
        this.setMaxDamage(64);
        this.maxStackSize = 1;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(worldIn.getBlockState(pos).getBlock() == Blocks.LOG2 && worldIn.getBlockState(pos).getValue(BlockNewLog.VARIANT) == BlockPlanks.EnumType.ACACIA)
        {
            if(player.getHeldItem(hand).getItem() == ModItems.woodTap)
            {
                player.getHeldItem(hand).damageItem(1,player);
                worldIn.setBlockState(pos, ModBlocks.dry_acacia_log.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Z));

                ItemStack stack = new ItemStack(ModItems.woodSap);
                double d0 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
                double d1 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
                double d2 = (double)(worldIn.rand.nextFloat() * 0.5F) + 0.25D;
                EntityItem entityitem = new EntityItem(worldIn, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, stack);
                entityitem.setDefaultPickupDelay();
                worldIn.spawnEntity(entityitem);

                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }
}
