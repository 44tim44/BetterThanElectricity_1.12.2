package se.sst_55t.betterthanelectricity.block.solarpanel;

import net.minecraft.world.biome.BiomeDesert;
import se.sst_55t.betterthanelectricity.item.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2017-08-22.
 */
public class TileEntitySolarPanel extends TileEntity implements ITickable {

    private static final int BASE_CHARGE_RATE = 20; // Amount of ticks required to charge 1 energy.
    private int chargeTime;
    private int totalChargeTime;
    private ItemStackHandler inventory = new ItemStackHandler(1);

    public void update ()
    {
        ItemStack itemstack = inventory.getStackInSlot(0);

        if (isCharging() && (itemstack.getItem() instanceof IBattery || itemstack.getItem() instanceof IChargeable))
        {
            ++this.chargeTime;

            if (this.chargeTime == this.totalChargeTime)
            {
                this.chargeTime = 0;
                this.totalChargeTime = this.getItemChargeTime(itemstack);
                if(itemstack.getItem() instanceof IBattery)
                {
                    ((IBattery)itemstack.getItem()).increaseCharge(itemstack);
                }
                else if(itemstack.getItem() instanceof IChargeable)
                {
                    ((IChargeable)itemstack.getItem()).increaseCharge(itemstack);
                }
            }
        }
        else
        {
            this.chargeTime = 0;
        }
    }

    /**
     * Time to cook:
     * Lower number = faster.
     */
    public int getItemChargeTime(@Nullable ItemStack stack)
    {
       float temp = world.getBiome(this.pos).getTemperature();
       if(temp < 0.5F)
        {
            temp = 0.5F;
        }
        return Math.round((float)BASE_CHARGE_RATE / temp);
    }

    public int getCharge(){
        ItemStack itemstack = inventory.getStackInSlot(0);
        if (itemstack.isEmpty()){
            return -1;
        }
        else if (itemstack.getItem() instanceof IBattery)
        {
            return ((IBattery)itemstack.getItem()).getCharge(itemstack);
        }
        else if (itemstack.getItem() instanceof IChargeable)
        {
            return ((IChargeable)itemstack.getItem()).getCharge(itemstack);
        }
        else
        {
            return -1;
        }
    }

    public boolean isCharging()
    {
        if (this.hasWorld())
        {
            if (!this.world.provider.isNether() && this.world.canBlockSeeSky(this.pos.offset(EnumFacing.UP)) && !this.world.isRaining() && world.getWorldTime()%24000 < 12000 && world.getWorldTime()%24000 > 0) {
                return true;
            }
            else return false;
        }
        return false;
    }

    public ItemStackHandler getContents(){
        return inventory;
    }

    public void setChargeTime(int chargeTime) {
        this.chargeTime = chargeTime;
    }

    public void setTotalChargeTime(int totalChargeTime) {
        this.totalChargeTime = totalChargeTime;
    }

    public int getChargeTime() {
        return chargeTime;
    }

    public int getTotalChargeTime() {
        return totalChargeTime;
    }

    @Override
    public void readFromNBT (NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        this.chargeTime = compound.getInteger("ChargeTime");
        this.totalChargeTime = compound.getInteger("ChargeTimeTotal");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT (NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        compound.setInteger("ChargeTime", this.chargeTime);
        compound.setInteger("ChargeTimeTotal", this.totalChargeTime);
        return super.writeToNBT(compound);
    }


    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
    }
}
