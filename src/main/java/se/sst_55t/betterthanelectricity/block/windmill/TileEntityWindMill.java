package se.sst_55t.betterthanelectricity.block.windmill;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import se.sst_55t.betterthanelectricity.block.pulverizer.BlockPulverizer;
import se.sst_55t.betterthanelectricity.item.*;

import javax.annotation.Nullable;

import static se.sst_55t.betterthanelectricity.block.windmill.BlockWindMill.FACING;

/**
 * Created by Timeout on 2017-08-22.
 */
public class TileEntityWindMill extends TileEntity implements ITickable {

    private static final int BASE_CHARGE_RATE = 20; // Amount of ticks required to charge 1 energy.
    private int chargeTime;
    private int totalChargeTime;
    private ItemStackHandler inventory = new ItemStackHandler(1);

    public void update ()
    {
        BlockWindMill.setState(this.isCharging(), this.world, this.pos);

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
        int height = this.pos.getY();
        // Normalizes height to 0.0F-2.0F = y:62-y:140
        float value = ((float)(height - 62) / (float)(140 - 62)) * 2.0F;
        if(value < 0.1F) value = 0.1F;
        if(value > 2.0F) value = 2.0F;
        return Math.round((float)BASE_CHARGE_RATE / value);
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
            EnumFacing facing = this.world.getBlockState(pos).getValue(FACING);
            BlockPos tempPos = pos;
            for(int i = 0; i < 20; i++){
                if(!this.world.isAirBlock(tempPos.offset(facing))){
                    return false;
                }
                tempPos = tempPos.offset(facing);

            }
            /**
             *  Checks if the 3x3 area in front of the WindMill is air. If not, return false.
             */
            /*
            if(!this.world.isAirBlock(pos.offset(facing).offset(EnumFacing.UP))){
                return false;
            }
            if(!this.world.isAirBlock(pos.offset(facing).offset(EnumFacing.DOWN))){
                return false;
            }
            if(!this.world.isAirBlock(pos.offset(facing).offset(facing.rotateY()))){
                return false;
            }
            if(!this.world.isAirBlock(pos.offset(facing).offset(facing.rotateYCCW()))){
                return false;
            }
            if(!this.world.isAirBlock(pos.offset(facing).offset(facing.rotateY()).offset(EnumFacing.UP))){
                return false;
            }
            if(!this.world.isAirBlock(pos.offset(facing).offset(facing.rotateYCCW()).offset(EnumFacing.UP))){
                return false;
            }
            if(!this.world.isAirBlock(pos.offset(facing).offset(facing.rotateY()).offset(EnumFacing.DOWN))){
                return false;
            }
            if(!this.world.isAirBlock(pos.offset(facing).offset(facing.rotateYCCW()).offset(EnumFacing.DOWN))){
                return false;
            }
            */

            return true;
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
