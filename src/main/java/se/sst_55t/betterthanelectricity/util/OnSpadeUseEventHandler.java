package se.sst_55t.betterthanelectricity.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import se.sst_55t.betterthanelectricity.block.ModBlocks;

/**
 * Created by Timeout on 2017-10-13.
 */
public class OnSpadeUseEventHandler
{
    @SubscribeEvent
    public void useSpade(PlayerInteractEvent.RightClickBlock event)
    {
        ItemStack itemstack = event.getEntityPlayer().getHeldItem(event.getHand());
        if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemSpade)
        {
            EntityPlayer player = event.getEntityPlayer();
            BlockPos pos = event.getPos();
            World worldIn = event.getWorld();

            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (event.getFace() != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && block == ModBlocks.grass_slab)
            {
                IBlockState iblockstate1 = ModBlocks.grass_path_slab.getDefaultState();
                event.getWorld().playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);

                if (!worldIn.isRemote)
                {
                    if(iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP)
                    {
                        event.getWorld().setBlockState(pos, iblockstate1.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP));
                    }
                    else
                    {
                        event.getWorld().setBlockState(pos, iblockstate1.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM));
                    }

                    event.getEntityPlayer().getHeldItem(event.getHand()).damageItem(1, player);
                }
            }
        }
    }
}
