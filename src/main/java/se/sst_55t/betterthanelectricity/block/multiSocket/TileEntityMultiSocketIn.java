package se.sst_55t.betterthanelectricity.block.multiSocket;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import se.sst_55t.betterthanelectricity.block.ICable;
import se.sst_55t.betterthanelectricity.block.IConsumer;
import se.sst_55t.betterthanelectricity.block.IElectricityStorage;
import se.sst_55t.betterthanelectricity.block.IGenerator;
import se.sst_55t.betterthanelectricity.block.cable.TileEntityCable;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * Created by Timeout on 2017-10-27.
 */
public class TileEntityMultiSocketIn extends TileEntity implements IGenerator, IConsumer {

    private int currentChargeRate;

    private static final int maxCharge = 6400;
    private int currentCharge;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
    }

    public TileEntity getConnectedBlockTE(EnumFacing facing)
    {
        if(isConnected()) {
            if ( world.getTileEntity(this.pos.offset(facing)) instanceof ICable )
            {
                return world.getTileEntity(this.pos.offset(facing));
            }
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
        return amountOfConnections == 2;
    }

    /**
     * The block this is is getting energy FROM
     *
     * @return
     */
    public TileEntity getOutputTE(@Nullable EnumFacing ignoreSide)
    {
        TileEntity outputTE = null;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            if(facing != ignoreSide)
            {
                outputTE = getConnectedBlockTE(facing);
            }

            if (outputTE != null)
            {
                if(outputTE instanceof TileEntityCable)
                {
                    return ((TileEntityCable) outputTE).getOutputTE(facing.getOpposite());
                }
            }
        }
        return null;
    }

    public ArrayList<TileEntity> getOutputTEList(@Nullable EnumFacing ignoreSide)
    {
        TileEntity outputTE = null;
        int index = 0;
        ArrayList<TileEntity> outputTEList = new ArrayList<>();
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            if(facing != ignoreSide)
            {
                outputTE = getConnectedBlockTE(facing);
            }

            if (outputTE != null)
            {
                if(outputTE instanceof TileEntityCable)
                {
                    outputTEList.add(index,((TileEntityCable)outputTE).getOutputTE(facing.getOpposite()));
                }
            }
            index++;
        }
        return outputTEList;
    }

    /**
     * The block this is giving energy TO
     *
     * @return
     */
    public TileEntity getInputTE(@Nullable EnumFacing ignoreSide)
    {
        TileEntity inputTE = null;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            if(facing != ignoreSide)
            {
                inputTE = getConnectedBlockTE(facing);
            }

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
    public TileEntity getOutputTE() {
        return getOutputTE(null);
    }

    @Override
    public TileEntity getInputTE() {
        EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockMultiSocketIn.FACING);
        TileEntity inputTE = getConnectedBlockTE(facing);
        if (inputTE != null)
        {
            if(inputTE instanceof TileEntityCable)
            {
                return ((TileEntityCable) inputTE).getInputTE(facing.getOpposite());
            }
        }
        return null;
    }

    @Override
    public int getConsumeRate() {
        return ((IConsumer)getInputTE()).getConsumeRate();
    }

    @Override
    public int getChargeRate() {
        int total = 0;
        ArrayList<TileEntity> outputTEList = getOutputTEList(null);
        for(int i = 0; i < 6; i++){
            if(outputTEList.get(i) != null){
                total += ((IGenerator)outputTEList.get(i)).getChargeRate();
            }
        }
        return total;
    }

    //@Override
    public void decreaseCharge()
    {
        currentCharge--;
        this.markDirty();
    }

    //@Override
    public void increaseCharge()
    {
        currentCharge++;
        this.markDirty();
    }

    //@Override
    public void setCharge(int value)
    {
        if(value > this.maxCharge)
        {
            currentCharge = maxCharge;
            this.markDirty();
        }
        else if(value < 0)
        {
            currentCharge = 0;
            this.markDirty();
        }
        else
        {
            currentCharge = value;
            this.markDirty();
        }
    }

    //@Override
    public int getCharge(){
        return currentCharge;
    }

    //@Override
    public int getMaxCharge(){
        return maxCharge;
    }
}
