package se.sst_55t.betterthanelectricity.block.multiSocket;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import se.sst_55t.betterthanelectricity.block.ICable;
import se.sst_55t.betterthanelectricity.block.IConsumer;
import se.sst_55t.betterthanelectricity.block.IElectricityStorage;
import se.sst_55t.betterthanelectricity.block.IGenerator;
import se.sst_55t.betterthanelectricity.block.cable.TileEntityCable;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by Timeout on 2017-10-27.
 */
public class TileEntityMultiSocketIn extends TileEntity implements IGenerator, IConsumer, IElectricityStorage {

    private static final int maxCharge = 10;
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
        EnumFacing inputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketIn.FACING);
        return (amountOfConnections >= 2) && world.getTileEntity(this.pos.offset(inputSide)) instanceof ICable;
    }

    /**
     * The blocks this is is getting energy FROM
     *
     * @return
     */
    public TileEntity[] getOutputTEList(@Nullable EnumFacing ignoreSide)
    {
        TileEntity outputTE = null;
        int index = 0;
        TileEntity[] outputTEList = new TileEntity[6];
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            outputTE = null;
            if(facing != ignoreSide)
            {
                outputTE = getConnectedBlockTE(facing);
                if(outputTE != null)
                {
                    if(outputTE == this)
                    {
                        outputTE = null;
                    }
                    else if(outputTE == getInputTE())
                    {
                        outputTE = null;
                    }
                }
            }

            if (outputTE != null)
            {
                if(outputTE instanceof TileEntityCable)
                {
                    TileEntity outputTEEnd = ((TileEntityCable)outputTE).getOutputTE(facing.getOpposite());
                    if(outputTEEnd == this)
                    {
                        outputTEEnd = null;
                    }
                    else if(outputTEEnd == getInputTE())
                    {
                        outputTEEnd = null;
                    }
                    outputTEList[index] = outputTEEnd;
                }
            }
            index++;
        }
        return outputTEList;
    }

    @Override
    public TileEntity getOutputTE() {
        return null;
    }

    /**
     * The block this is giving energy TO
     *
     * @return
     */
    @Override
    public TileEntity getInputTE() {
        EnumFacing inputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketIn.FACING);
        TileEntity inputTE = getConnectedBlockTE(inputSide);
        if (inputTE != null)
        {
            if(inputTE instanceof TileEntityCable)
            {
                return ((TileEntityCable) inputTE).getInputTE(inputSide.getOpposite());
            }
        }
        return null;
    }

    @Override
    public float getConsumeRate() {
        return ((IConsumer)getInputTE()).getConsumeRate();
    }

    @Override
    public float getChargeRate() {
        float total = 0;
        EnumFacing inputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketIn.FACING);
        TileEntity[] outputTEList = getOutputTEList(inputSide);
        for(int i = 0; i < 6; i++){
            if(outputTEList[i] != null){
                total += ((IGenerator)outputTEList[i]).getChargeRate();
            }
        }
        return total;
    }

    @Override
    public void increaseCharge() {
        TileEntity inputTE = getInputTE();
        if(inputTE != null && inputTE instanceof IElectricityStorage)
        {
            ((IElectricityStorage) inputTE).increaseCharge();
        }
    }

    @Override
    public void decreaseCharge() {
        int count = 0;
        int[] countedIndex = new int[6];
        EnumFacing inputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketIn.FACING);
        TileEntity[] outputTEList = getOutputTEList(inputSide);
        for(int i = 0; i < 6; i++){
            if(outputTEList[i] != null){
                countedIndex[count] = i;
                count++;
            }
        }
        if(count > 0)
        {
            Random rand = new Random();
            int randomNumber = rand.nextInt(count);
            if (outputTEList[countedIndex[randomNumber]] instanceof IElectricityStorage)
            {
                ((IElectricityStorage) outputTEList[countedIndex[randomNumber]]).decreaseCharge();
            }
        }
    }

    @Override
    public void setCharge(int value) {

    }

    @Override
    public int getCharge() {
        return 1;
    }

    @Override
    public int getMaxCharge() {
        return 2;
    }
}
