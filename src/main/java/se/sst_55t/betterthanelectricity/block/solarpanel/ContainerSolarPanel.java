package se.sst_55t.betterthanelectricity.block.solarpanel;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Created by Timeout on 2017-08-22.
 */
public class ContainerSolarPanel extends Container implements IEnergyStorage{

    private long storedPower = 0;

    public ContainerSolarPanel(InventoryPlayer playerInventory, TileEntitySolarPanel solarPanel){
        IItemHandler inventory = solarPanel.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        addSlotToContainer(new SlotItemHandler(inventory, 0, 98, 35){
            @Override
            public void onSlotChanged()
            {
                solarPanel.setTotalChargeTime(solarPanel.getItemChargeTime(inventory.getStackInSlot(0)));
                solarPanel.setChargeTime(0);
                solarPanel.markDirty();
            }
        });

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack itemstack = null;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {

            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

            if (index < containerSlots) {
                if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }


    public long getStoredPower() {
        return this.storedPower;
    }


    public long getCapacity() {
        return SolarPanelConfig.panelCapacity;
    }


    public long takePower(long tesla, boolean simulated) {
        final long removedPower = Math.min(this.storedPower, Math.min(SolarPanelConfig.panelTransferRate, tesla));

        if (!simulated)
            this.storedPower -= removedPower;

        return removedPower;
    }

    public void generatePower(){
        this.storedPower += SolarPanelConfig.panelPowerGen;

        if (this.storedPower > this.getCapacity())
            this.storedPower = this.getCapacity();
    }

    protected void setPower (long power) {

        this.storedPower = power;
    }

    public static int getIntPower (long power) {

        if (power < Integer.MIN_VALUE)
            return Integer.MIN_VALUE;

        if (power > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;

        return (int) power;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return getIntPower(this.takePower(maxExtract, simulate));
    }

    @Override
    public int getEnergyStored() {
        return getIntPower(this.storedPower);
    }

    @Override
    public int getMaxEnergyStored() {
        return getIntPower(this.getCapacity());
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
