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
import se.sst_55t.betterthanelectricity.block.ICable;
import se.sst_55t.betterthanelectricity.block.IConsumer;
import se.sst_55t.betterthanelectricity.block.IElectricityStorage;
import se.sst_55t.betterthanelectricity.block.IGenerator;
import se.sst_55t.betterthanelectricity.block.cable.TileEntityCable;
import se.sst_55t.betterthanelectricity.block.multiSocket.TileEntityMultiSocketIn;
import se.sst_55t.betterthanelectricity.item.*;

import javax.annotation.Nullable;

import static se.sst_55t.betterthanelectricity.block.windmill.BlockWindMill.FACING;

/**
 * Created by Timeout on 2017-08-22.
 */
public class TileEntityWindMill extends TileEntity implements ITickable, IGenerator, IElectricityStorage {

    private static final int BASE_CHARGE_RATE = 20; // Amount of ticks required to charge 1 energy.
    private int chargeTime;
    private int totalChargeTime;
    private ItemStackHandler inventory = new ItemStackHandler(1);
    private static final int maxCharge = 1;
    private int currentCharge;

    public void update ()
    {
        BlockWindMill.setState(this.isCharging(), this.world, this.pos);

        this.totalChargeTime = this.getItemChargeTime();
        ItemStack itemstack = inventory.getStackInSlot(0);

        if (this.currentCharge > 0)
        {
            if ((itemstack.getItem() instanceof IBattery ))
            {
                ((IBattery) itemstack.getItem()).increaseCharge(itemstack);
                if(!this.world.isRemote) this.decreaseCharge();
            }
            else if((itemstack.getItem() instanceof IChargeable))
            {
                ((IChargeable) itemstack.getItem()).increaseCharge(itemstack);
                if(!this.world.isRemote) this.decreaseCharge();
            }
        }

        if (isCharging() && this.currentCharge < maxCharge)
        {
            ++this.chargeTime;

            if (this.chargeTime == this.totalChargeTime)
            {
                this.chargeTime = 0;
                if(!this.world.isRemote) this.increaseCharge();
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
    public int getItemChargeTime()
    {
        int height = this.pos.getY();
        // Normalizes height to 0.0F-2.0F = y:62-y:140
        float value = ((float)(height - 62) / (float)(140 - 62)) * 2.0F;
        if(value < 0.1F) value = 0.1F;
        if(value > 2.0F) value = 2.0F;
        return Math.round((float)BASE_CHARGE_RATE / value);
    }

    @Override
    public void increaseCharge() {
        if(this.currentCharge < maxCharge) this.currentCharge++;
        this.markDirty();
    }

    @Override
    public void decreaseCharge() {
        if(this.currentCharge > 0) this.currentCharge--;
        this.markDirty();
    }

    @Override
    public void setCharge(int value) {
        if(value > maxCharge)
        {
            this.currentCharge = maxCharge;
            this.markDirty();
        }
        else if(value < 0)
        {
            this.currentCharge = 0;
            this.markDirty();
        }
        else
        {
            this.currentCharge = value;
            this.markDirty();
        }
    }

    public int getCharge()
    {
        return this.currentCharge;
    }

    @Override
    public int getMaxCharge() {
        return maxCharge;
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

    public TileEntity getConnectedBlockTE(EnumFacing facing)
    {
        if(( world.getTileEntity(this.pos.offset(facing)) instanceof ICable) && isConnected())
        {
            return world.getTileEntity(this.pos.offset(facing));
        }
        return null;
    }

    public boolean isConnected()
    {
        int amountOfConnections = 0;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            if( world.getTileEntity(this.pos.offset(facing)) instanceof ICable )
            {
                amountOfConnections++;
            }
        }
        return amountOfConnections == 1;
    }

    @Override
    public TileEntity getConsumerTE() {
        TileEntity inputTE;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            inputTE = getConnectedBlockTE(facing);

            if (inputTE != null)
            {
                if(inputTE instanceof TileEntityCable)
                {
                    return ((TileEntityCable) inputTE).getConsumerTE(facing.getOpposite());
                }
            }
        }
        return null;
    }

    @Override
    public float getChargeRate()
    {
        if (isCharging())
        {
            return (1.0F / (getItemChargeTime() / 20.0F));
        }
        return 0;
    }

    /**
     * Returns true if a battery is currently being charged, or if a consumer is given energy by this block.
     * Used for GUI.
     *
     */
    public boolean isGivingCharge()
    {
        if(!isCharging())
        {
            return false;
        }

        ItemStack batteryStack = inventory.getStackInSlot(0);
        if (batteryStack.getItem() instanceof IBattery)
        {
            if(((IBattery)batteryStack.getItem()).getCharge(batteryStack) < ((IBattery)batteryStack.getItem()).getMaxCharge(batteryStack))
            {
                return true;
            }
        }
        else if (batteryStack.getItem() instanceof IChargeable)
        {
            if(((IChargeable)batteryStack.getItem()).getCharge(batteryStack) < ((IChargeable)batteryStack.getItem()).getMaxCharge(batteryStack))
            {
                return true;
            }
        }

        TileEntity consumerTE = getConsumerTE();
        if(consumerTE != null)
        {
            if (consumerTE instanceof TileEntityMultiSocketIn)
            {
                TileEntity[] generatorTEList = ((TileEntityMultiSocketIn) consumerTE).getGeneratorTEList(null);
                for (TileEntity generatorTE : generatorTEList)
                {
                    if (generatorTE == this) {
                        return true;
                    }
                }
            }
            else if (((IConsumer) consumerTE).getGeneratorTE() == this)
            {
                return true;
            }
        }

        return false;
    }
}
