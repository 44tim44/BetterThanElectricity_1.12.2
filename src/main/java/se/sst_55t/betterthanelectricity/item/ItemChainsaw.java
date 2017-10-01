package se.sst_55t.betterthanelectricity.item;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.sound.LoopableSound;
import se.sst_55t.betterthanelectricity.util.ModSoundEvents;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Timeout on 2017-09-27.
 */
public class ItemChainsaw extends Item implements IChargeable{

    private static final int maxCharge = 1280;
    private static boolean playingIdle;

    public ItemChainsaw() {
        setUnlocalizedName("chainsaw");
        setRegistryName("chainsaw");
        setMaxStackSize(1);
        setMaxDamage(1300);
        setCreativeTab(CreativeTabs.TOOLS);
    }

    public void decreaseCharge(ItemStack stack){
        this.setCharge(this.getDamage(stack)-1,stack);
    }

    public void increaseCharge(ItemStack stack){

        this.setCharge(this.getDamage(stack)+1,stack);
    }

    public void setCharge(int value, ItemStack stack)
    {
        if(value > this.maxCharge)
        {
            this.setDamage(stack,maxCharge);
        }
        else if(value < 0)
        {
            this.setDamage(stack,0);
        }
        else
        {
            this.setDamage(stack,value);
        }
    }

    public int getCharge(ItemStack stack){
        return this.getDamage(stack);
    }

    public int getMaxCharge(ItemStack stack){
        return maxCharge;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add("Charge: " + getCharge(stack) + "/" + maxCharge);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 -((double)getCharge(stack) / (double)maxCharge);
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        if(((ItemChainsaw)itemstack.getItem()).getCharge(itemstack)>0)
        {
            if (!player.canPlayerEdit(pos.offset(facing), facing, itemstack))
            {
                return EnumActionResult.FAIL;
            }
            else
            {
                IBlockState iblockstate = worldIn.getBlockState(pos);
                Block block = iblockstate.getBlock();
                Material material = iblockstate.getMaterial();

                if (block == Blocks.LOG)
                {
                    this.fellTree(itemstack, player, worldIn, pos, iblockstate);
                    return EnumActionResult.SUCCESS;
                }
                else if (material == Material.WOOD)
                {
                    this.chopBlock(itemstack, player, worldIn, pos, iblockstate);
                    return EnumActionResult.SUCCESS;
                }
                else
                {
                    return EnumActionResult.FAIL;
                }
            }
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    protected void chopBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.playSound(player, pos, ModSoundEvents.CHAINSAW_USE, SoundCategory.BLOCKS, 0.5F, 1.0F);

        if (!worldIn.isRemote)
        {
            worldIn.destroyBlock(pos,true);
            ((ItemChainsaw)stack.getItem()).decreaseCharge(stack);
        }
    }

    protected void fellTree(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.playSound(player, pos, ModSoundEvents.CHAINSAW_USE, SoundCategory.PLAYERS, 0.5F, 1.0F);

        if (!worldIn.isRemote)
        {
            BlockPos tempPos = pos;
            boolean done = false;
            //int amountOfLogs = 0;

            while(!done){
                if(worldIn.getBlockState(tempPos).getBlock() != Blocks.LOG){
                    done = true;
                } else {
                    //amountOfLogs++;
                    worldIn.destroyBlock(tempPos,true);
                    ((ItemChainsaw)stack.getItem()).decreaseCharge(stack);
                    tempPos = tempPos.offset(EnumFacing.UP);
                }
                if(((ItemChainsaw)stack.getItem()).getCharge(stack) == 0)
                {
                    done = true;
                }
            }
        }
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    /*
    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            if(player.getActiveItemStack() == stack)
            {
                if(!playingIdle)
                {
                    ISound chainsawIdle = new LoopableSound(BTEMod.MODID + ":chainsaw.idle",0.1F,1);
                    worldIn.playSound(player, player.getPosition(), new SoundEvent(chainsawIdle), SoundCategory.PLAYERS, 0.5F, 1.0F);
                    playingIdle = true;
                }
            }
            else
            {
                playingIdle = false;
            }
        }
    }
    */


    public void registerItemModel() {
        BTEMod.proxy.registerItemRenderer(this, 0, "chainsaw");
    }
}
