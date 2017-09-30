package se.sst_55t.betterthanelectricity.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * Created by Timeout on 2017-09-28.
 */
public class InventoryUtils {
    /**
     * An entity inventory type.
     */
    public enum EntityInventoryType {
        MAIN,
        HAND,
        ARMOUR;

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ENGLISH);
        }
    }

    /**
     * Get the main inventory of the specified entity.
     * <p>
     * For players, this returns the main inventory. For other entities, this returns null.
     *
     * @param entity The entity
     * @return The inventory, if any
     */
    @Nullable
    public static IItemHandler getMainInventory(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        }

        return null;
    }

    /**
     * Get the hand inventory of the specified entity.
     * <p>
     * For players, this returns the off hand inventory. For other entities, this returns the {@link EnumFacing#UP} {@link IItemHandler} capability.
     *
     * @param entity The entity
     * @return The hand inventory, if any
     */
    @Nullable
    public static IItemHandler getHandInventory(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return new PlayerOffhandInvWrapper(((EntityPlayer) entity).inventory);
        }

        return entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
    }

    /**
     * Get the armour inventory of the specified entity.
     * <p>
     * For players, this returns the armour inventory. For other entities, this returns the {@link EnumFacing#NORTH} {@link IItemHandler} capability.
     *
     * @param entity The entity
     * @return The inventory, if any
     */
    @Nullable
    public static IItemHandler getArmourInventory(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return new PlayerArmorInvWrapper(((EntityPlayer) entity).inventory);
        }

        return entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
    }

    /**
     * Get the entity's inventory of the specified type.
     *
     * @param entity        The entity
     * @param inventoryType The inventory type.
     * @return The inventory, if any
     */
    @Nullable
    public static IItemHandler getInventoryForType(final Entity entity, final EntityInventoryType inventoryType) {
        switch (inventoryType) {
            case MAIN:
                return getMainInventory(entity);
            case HAND:
                return getHandInventory(entity);
            case ARMOUR:
                return getArmourInventory(entity);
        }

        return null;
    }

    /**
     * Try to perform an operation for each of the specified inventory types, stopping at the first successful operation.
     * <p>
     * Only performs the operation on inventory types that exist for the entity.
     * <p>
     * This is mainly useful in {@Link Item#onUpdate(ItemStack, World, Entity, int, boolean)}, where the item can be in any of the player's inventories.
     *
     * @param entity         The entity
     * @param operation      The operation to perform
     * @param inventoryTypes The inventory types to perform the operation on, in order
     * @return The inventory type of the first successful operation, or null if all operations failed
     */
    @Nullable
    public static EntityInventoryType forEachEntityInventory(final Entity entity, final Predicate<IItemHandler> operation, final EntityInventoryType... inventoryTypes) {
        for (final EntityInventoryType inventoryType : inventoryTypes) {
            final IItemHandler inventory = getInventoryForType(entity, inventoryType);
            if (inventory != null && operation.test(inventory)) {
                return inventoryType;
            }
        }

        return null;
    }
}
