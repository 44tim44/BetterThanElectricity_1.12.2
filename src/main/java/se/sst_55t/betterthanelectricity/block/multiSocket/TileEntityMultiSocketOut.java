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
public class TileEntityMultiSocketOut extends TileEntity implements IGenerator, IConsumer, IElectricityStorage
{

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
        EnumFacing outputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketOut.FACING);
        return (amountOfConnections >= 2) && world.getTileEntity(this.pos.offset(outputSide)) instanceof ICable;
    }

    /**
     * The blocks this is is giving energy TO
     *
     * @return
     */
    public TileEntity[] getInputTEList(@Nullable EnumFacing ignoreSide)
    {
        TileEntity inputTE = null;
        int index = 0;
        TileEntity[] inputTEList = new TileEntity[6];
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            inputTE = null;
            if(facing != ignoreSide)
            {
                inputTE = getConnectedBlockTE(facing);
                if(inputTE != null)
                {
                    if(inputTE == this)
                    {
                        inputTE = null;
                    }
                    else if(inputTE == getOutputTE())
                    {
                        inputTE = null;
                    }
                }
            }

            if (inputTE != null)
            {
                if(inputTE instanceof TileEntityCable)
                {
                    TileEntity inputTEEnd = ((TileEntityCable)inputTE).getInputTE(facing.getOpposite());
                    if(inputTEEnd == this)
                    {
                        inputTEEnd = null;
                    }
                    else if(inputTEEnd == getOutputTE())
                    {
                        inputTEEnd = null;
                    }
                    inputTEList[index] = inputTEEnd;
                }
            }
            index++;
        }
        return inputTEList;
    }

    @Override
    public TileEntity getInputTE() {
        return null;
    }

    /**
     * The block this is getting energy FROM
     *
     * @return
     */
    @Override
    public TileEntity getOutputTE() {
        EnumFacing outputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketOut.FACING);
        TileEntity outputTE = getConnectedBlockTE(outputSide);
        if (outputTE != null)
        {
            if(outputTE instanceof TileEntityCable)
            {
                return ((TileEntityCable) outputTE).getOutputTE(outputSide.getOpposite());
            }
        }
        return null;
    }

    @Override
    public float getConsumeRate() {
        return 0; //((IConsumer)getInputTE()).getConsumeRate();
    }

    @Override
    public float getChargeRate() {

        if(getOutputTE() == null)
        {
            return 0;
        }
        float totalChargeRate = ((IGenerator)getOutputTE()).getChargeRate();
        EnumFacing outputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketOut.FACING);

        float count = 0;
        TileEntity[] inputTEList = getInputTEList(outputSide);
        for(int i = 0; i < 6; i++)
        {
            if(inputTEList[i] != null){
                count ++;
            }
        }
        System.out.println("TotalCharge: " + totalChargeRate);
        System.out.println("Count: " + count);
        return totalChargeRate/count;
    }

    @Override
    public void increaseCharge() {
        int count = 0;
        int[] countedIndex = new int[6];
        EnumFacing outputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketOut.FACING);
        TileEntity[] inputTEList = getInputTEList(outputSide);
        for(int i = 0; i < 6; i++){
            if(inputTEList[i] != null){
                countedIndex[count] = i;
                count++;
            }
        }
        if(count > 0)
        {
            Random rand = new Random();
            int randomNumber = rand.nextInt(count);
            if (inputTEList[countedIndex[randomNumber]] instanceof IElectricityStorage)
            {
                ((IElectricityStorage) inputTEList[countedIndex[randomNumber]]).increaseCharge();
            }
        }
    }

    @Override
    public void decreaseCharge() {
        TileEntity outputTE = getOutputTE();
        if(outputTE != null && outputTE instanceof IElectricityStorage)
        {
            ((IElectricityStorage) outputTE).decreaseCharge();
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
