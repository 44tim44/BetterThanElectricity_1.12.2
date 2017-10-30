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
import se.sst_55t.betterthanelectricity.item.*;

import javax.annotation.Nullable;

import static se.sst_55t.betterthanelectricity.block.windmill.BlockWindMill.FACING;

/**
 * Created by Timeout on 2017-08-22.
 */
public class TileEntityWindMill extends TileEntity implements ITickable, IGenerator {

    private static final int BASE_CHARGE_RATE = 20; // Amount of ticks required to charge 1 energy.
    private int chargeTime;
    private int totalChargeTime;
    private ItemStackHandler inventory = new ItemStackHandler(1);

    public void update ()
    {
        BlockWindMill.setState(this.isCharging(), this.world, this.pos);

        this.totalChargeTime = this.getItemChargeTime(null);
        ItemStack itemstack = inventory.getStackInSlot(0);
        TileEntity te = getConsumerTE();

        if (isCharging() && (itemstack.getItem() instanceof IBattery || itemstack.getItem() instanceof IChargeable))
        {
            ++this.chargeTime;

            if (this.chargeTime == this.totalChargeTime)
            {
                this.chargeTime = 0;
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
        else if(isCharging() && te instanceof IConsumer)
        {
            ++this.chargeTime;

            if (this.chargeTime == this.totalChargeTime)
            {
                this.chargeTime = 0;
                if (te instanceof IElectricityStorage)
                {
                    if(((IElectricityStorage) te).getCharge() < ((IElectricityStorage) te).getMaxCharge())
                    {
                        if (!this.world.isRemote)
                        {
                            ((IElectricityStorage) te).increaseCharge();
                        }
                    }
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
                    return ((TileEntityCable) inputTE).getInputTE(facing.getOpposite());
                }
            }
        }
        return null;
    }

    @Override
    public float getChargeRate()
    {
        ItemStack batteryStack = inventory.getStackInSlot(0);
        if(batteryStack.isEmpty() ||  ((IChargeable) batteryStack.getItem()).getCharge(batteryStack) == ((IChargeable) batteryStack.getItem()).getMaxCharge(batteryStack))
        {
            if (isCharging())
            {
                return (1.0F / (getItemChargeTime(null) / 20.0F));
            }
        }
        return 0;
    }
}
