package se.sst_55t.betterthanelectricity.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Timeout on 2017-09-21.
 */
public class ItemSlabCustom extends ItemBlock {
    private final BlockSlab singleSlab;
    private final BlockSlab doubleSlab;

    public ItemSlabCustom(Block block, BlockSlab singleSlab, BlockSlab doubleSlab) {
        super(block);
        this.singleSlab = singleSlab;
        this.doubleSlab = doubleSlab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        BlockPos blockpos = pos;
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == this.singleSlab) {
            boolean flag = iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;

            if ((side == EnumFacing.UP && !flag || side == EnumFacing.DOWN && flag)) {
                return true;
            }
        }

        pos = pos.offset(side);
        IBlockState iblockstate1 = worldIn.getBlockState(pos);
        return iblockstate1.getBlock() == this.singleSlab ? true : super.canPlaceBlockOnSide(worldIn, blockpos, side, player, stack);
    }

    @Override
    public int getMetadata(int damage) {
        return 0;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.singleSlab.getUnlocalizedName();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        ItemStack stack = playerIn.getHeldItem(hand);

        if (!stack.isEmpty() && playerIn.canPlayerEdit(pos.offset(facing), facing, stack)) {
            Comparable<?> comparable = this.singleSlab.getTypeForItem(stack);
            IBlockState iblockstate = worldIn.getBlockState(pos);

            if (iblockstate.getBlock() == this.singleSlab) {
                BlockSlab.EnumBlockHalf blockslab$enumblockhalf = iblockstate.getValue(BlockSlab.HALF);

                if ((facing == EnumFacing.UP && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.BOTTOM || facing == EnumFacing.DOWN && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.TOP)) {
                    IBlockState iblockstate1 = this.doubleSlab.getDefaultState();
                    AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, pos);

                    if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, iblockstate1, 11)) {
                        SoundType soundtype = this.doubleSlab.getSoundType(iblockstate1, worldIn, pos, playerIn);
                        worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        stack.shrink(1);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            return this.tryPlace(playerIn, stack, worldIn, pos.offset(facing), comparable) ? EnumActionResult.SUCCESS : super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        } else {
            return EnumActionResult.FAIL;
        }
    }

    private boolean tryPlace(EntityPlayer player, ItemStack stack, World worldIn, BlockPos pos, Object itemSlabType) {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == this.singleSlab) {

            IBlockState iblockstate1 = this.doubleSlab.getDefaultState();
            AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, pos);

            if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, iblockstate1, 11)) {
                SoundType soundtype = this.doubleSlab.getSoundType(iblockstate1, worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                stack.shrink(1);
            }

            return true;
        }

        return false;
    }
}
