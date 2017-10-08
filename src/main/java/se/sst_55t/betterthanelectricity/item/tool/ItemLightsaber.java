package se.sst_55t.betterthanelectricity.item.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.item.IChargeable;
import se.sst_55t.betterthanelectricity.item.ModItems;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Timmy on 2016-11-26.
 */
public class ItemLightsaber extends net.minecraft.item.ItemSword implements IChargeable
{
    private final float attackDamage;
    private final ToolMaterial material;
    private String name;
    private static final int maxCharge = 320;

    public ItemLightsaber(String name) {
        super(BTEMod.steelToolMaterial);
        this.material = BTEMod.steelToolMaterial;
        this.name = name;
        this.attackDamage = 11.0F;
        this.setMaxDamage(400);
        setRegistryName(name);
        setUnlocalizedName(name);
        this.setCreativeTab(CreativeTabs.COMBAT);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack heldItemstack = playerIn.getHeldItem(handIn);
        if(heldItemstack.getItem() == this)
        {
            int currentCharge = ((IChargeable) heldItemstack.getItem()).getCharge(heldItemstack);
            if (heldItemstack.getItem() == ModItems.lightsaberRed)
            {
                playerIn.setHeldItem(handIn, new ItemStack(ModItems.lightsaberRedOff));
                ((IChargeable) playerIn.getHeldItem(handIn).getItem()).setCharge(currentCharge, playerIn.getHeldItem(handIn));
            }
            else if (heldItemstack.getItem() == ModItems.lightsaberGreen)
            {
                playerIn.setHeldItem(handIn, new ItemStack(ModItems.lightsaberGreenOff));
                ((IChargeable) playerIn.getHeldItem(handIn).getItem()).setCharge(currentCharge, playerIn.getHeldItem(handIn));
            }
            else if (heldItemstack.getItem() == ModItems.lightsaberBlue)
            {
                playerIn.setHeldItem(handIn, new ItemStack(ModItems.lightsaberBlueOff));
                ((IChargeable) playerIn.getHeldItem(handIn).getItem()).setCharge(currentCharge, playerIn.getHeldItem(handIn));
            }
            return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }

        return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        if(((IChargeable)stack.getItem()).getCharge(stack) == 0)
        {
            System.out.println("Lightsaber charge == 0");
            if (stack.getItem() == ModItems.lightsaberRed)
            {
                stack = new ItemStack(ModItems.lightsaberRedOff);
                System.out.println("replaced with redoff");
                ((EntityPlayer)entityIn).inventory.setInventorySlotContents(itemSlot,stack);
                ((IChargeable) stack.getItem()).setCharge(0, stack);
            }
            else if (stack.getItem() == ModItems.lightsaberGreen)
            {
                stack = new ItemStack(ModItems.lightsaberGreenOff);
                ((EntityPlayer)entityIn).inventory.setInventorySlotContents(itemSlot,stack);
                System.out.println("replaced with greenoff");
                ((IChargeable) stack.getItem()).setCharge(0, stack);
            }
            else if (stack.getItem() == ModItems.lightsaberBlue)
            {
                stack = new ItemStack(ModItems.lightsaberBlueOff);
                ((EntityPlayer)entityIn).inventory.setInventorySlotContents(itemSlot,stack);
                System.out.println("replaced with blueoff");
                ((IChargeable) stack.getItem()).setCharge(0, stack);
            }
        }
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

    /**
     * Returns the amount of damage this item will deal. One heart of damage is equal to 2 damage points.
     */
    @Override
    public float getDamageVsEntity()
    {
        return 6.0F;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state)
    {
        Block block = state.getBlock();

        if (block == Blocks.WEB)
        {
            return 15.0F;
        }
        else
        {
            Material material = state.getMaterial();
            return material != Material.PLANTS && material != Material.VINE && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD ? 1.0F : 1.5F;
        }
    }

    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        ((IChargeable)stack.getItem()).decreaseCharge(stack);
        return true;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger the "Use Item" statistic.
     */
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        if ((double)state.getBlockHardness(worldIn, pos) != 0.0D)
        {
            ((IChargeable)stack.getItem()).decreaseCharge(stack);
            ((IChargeable)stack.getItem()).decreaseCharge(stack);
        }

        return true;
    }

    /**
     * Check whether this Item can harvest the given Block
     */
    @Override
    public boolean canHarvestBlock(IBlockState blockIn)
    {
        return blockIn.getBlock() == Blocks.WEB;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    @Override
    public int getItemEnchantability()
    {
        return this.material.getEnchantability();
    }

    /**
     * Return the name for this tool's material.
     */
    @Override
    public String getToolMaterialName()
    {
        return this.material.toString();
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return false;
    }

    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
     */
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double)this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4D, 0));
        }

        return multimap;
    }

    public void registerItemModel() {
        BTEMod.proxy.registerItemRenderer(this, 0, name);
    }

}