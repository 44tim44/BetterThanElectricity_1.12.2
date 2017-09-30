package se.sst_55t.betterthanelectricity.item;

import se.sst_55t.betterthanelectricity.block.BlockSlabVerticalBase;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
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
public class ItemVerticalSlab extends ItemBlock {
    private final BlockSlabVerticalBase singleSlab;
    private final BlockSlabVerticalBase doubleSlab;

    public ItemVerticalSlab(BlockSlabVerticalBase block, BlockSlabVerticalBase singleSlab, BlockSlabVerticalBase doubleSlab) {
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
            switch (iblockstate.getValue(BlockSlabVerticalBase.POSITION)){
                case NORTH:
                    if(side == EnumFacing.SOUTH) return true;
                case SOUTH:
                    if(side == EnumFacing.NORTH) return true;
                case EAST:
                    if(side == EnumFacing.WEST) return true;
                case WEST:
                    if(side == EnumFacing.EAST) return true;
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
                BlockSlabVerticalBase.EnumPosition enumPos = iblockstate.getValue(BlockSlabVerticalBase.POSITION);

                if ((facing == EnumFacing.NORTH && enumPos == BlockSlabVerticalBase.EnumPosition.SOUTH ||
                        facing == EnumFacing.SOUTH && enumPos == BlockSlabVerticalBase.EnumPosition.NORTH ||
                        facing == EnumFacing.EAST && enumPos == BlockSlabVerticalBase.EnumPosition.WEST ||
                        facing == EnumFacing.WEST && enumPos == BlockSlabVerticalBase.EnumPosition.EAST)) {
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
