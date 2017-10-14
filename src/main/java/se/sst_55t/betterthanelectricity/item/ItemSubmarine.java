package se.sst_55t.betterthanelectricity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.entity.item.EntitySubmarine;

/**
 * Created by Timeout on 2017-10-14.
 */
public class ItemSubmarine extends ItemBase {
    public ItemSubmarine(String name) {
        super(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!playerIn.capabilities.isCreativeMode)
        {
            playerIn.getHeldItem(handIn).shrink(1);
        }
        //worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.4F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isRemote)
        {
            BlockPos pos = playerIn.getPosition().offset(playerIn.getHorizontalFacing());
            EntitySubmarine submarine = new EntitySubmarine(worldIn,pos.getX(), pos.getY(), pos.getZ());
            worldIn.spawnEntity(submarine);
        }
        return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
