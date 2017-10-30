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

/**
 * Created by Timeout on 2017-10-27.
 */
public class TileEntityMultiSocketOut extends TileEntity implements IGenerator, IConsumer, IElectricityStorage
{
    private int counter;

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
    public TileEntity[] getConsumerTEList(@Nullable EnumFacing ignoreSide)
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
                    else if(inputTE == getGeneratorTE())
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
                    else if(inputTEEnd == getGeneratorTE())
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
    public TileEntity getConsumerTE() {
        return null;
    }

    /**
     * The block this is getting energy FROM
     *
     * @return
     */
    @Override
    public TileEntity getGeneratorTE() {
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

        TileEntity te = getGeneratorTE();
        float totalChargeRate = 0;
        if(te != null && te instanceof IGenerator)
        {
            if (te instanceof TileEntityMultiSocketOut)
            {
                TileEntity[] consumerTEList = ((TileEntityMultiSocketOut) te).getConsumerTEList(null);
                for (TileEntity consumerTE : consumerTEList)
                {
                    if (consumerTE == this)
                    {
                        totalChargeRate = ((IGenerator) te).getChargeRate();
                    }
                }
            }
            else if (((IGenerator) te).getConsumerTE() == this)
            {
                totalChargeRate = ((IGenerator) te).getChargeRate();
            }
        }
        ///totalChargeRate = ((IGenerator) getGeneratorTE()).getChargeRate();

        if(totalChargeRate > 0)
        {
            float count = 0;
            EnumFacing outputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketOut.FACING);
            TileEntity[] consumerTEList = getConsumerTEList(outputSide);
            for (int i = 0; i < 6; i++)
            {
                if (consumerTEList[i] != null && consumerTEList[i] instanceof IConsumer)
                {
                    if (consumerTEList[i] instanceof TileEntityMultiSocketIn)
                    {
                        TileEntity[] generatorTEList = ((TileEntityMultiSocketIn) consumerTEList[i]).getGeneratorTEList(null);
                        for (TileEntity generatorTE : generatorTEList)
                        {
                            if (generatorTE == this)
                            {
                                count++;
                            }
                        }
                    }
                    else if(((IConsumer) consumerTEList[i]).getGeneratorTE() == this)
                    {
                        count++;
                    }
                }
            }
            return totalChargeRate / count;
        }
        return 0;
    }

    @Override
    public void increaseCharge() {
        int count = 0;
        int[] countedIndex = new int[6];
        EnumFacing outputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketOut.FACING);
        TileEntity[] inputTEList = getConsumerTEList(outputSide);
        for(int i = 0; i < 6; i++){
            if(inputTEList[i] != null){
                countedIndex[count] = i;
                count++;
            }
        }
        if(count > 0)
        {
            this.counter++;
            if(this.counter > 5) this.counter = 0;
            int index = counter % count;
            if (inputTEList[countedIndex[index]] instanceof IElectricityStorage)
            {
                ((IElectricityStorage) inputTEList[countedIndex[index]]).increaseCharge();
            }
        }
    }

    @Override
    public void decreaseCharge() {
        TileEntity outputTE = getGeneratorTE();
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
        TileEntity outputTE = getGeneratorTE();
        if(outputTE != null && outputTE instanceof IElectricityStorage)
        {
            if(outputTE instanceof TileEntityMultiSocketOut)
            {
                TileEntity[] inputlist = ((TileEntityMultiSocketOut)outputTE).getConsumerTEList(null);
                for(TileEntity inputTe : inputlist)
                {
                    if(inputTe == this)
                    {
                        return ((IElectricityStorage) outputTE).getCharge();
                    }
                }
            }
            else if(((IGenerator)outputTE).getConsumerTE() == this)
            {
                return ((IElectricityStorage) outputTE).getCharge();
            }
        }
        return 0;
    }

    @Override
    public int getMaxCharge()
    {
        TileEntity outputTE = getGeneratorTE();
        if(outputTE != null && outputTE instanceof IElectricityStorage)
        {
            if(outputTE instanceof TileEntityMultiSocketOut)
            {
                TileEntity[] inputlist = ((TileEntityMultiSocketOut)outputTE).getConsumerTEList(null);
                for(TileEntity inputTe : inputlist)
                {
                    if(inputTe == this)
                    {
                        return ((IElectricityStorage) outputTE).getMaxCharge();
                    }
                }
            }
            else if(((IGenerator)outputTE).getConsumerTE() == this)
            {
                return ((IElectricityStorage) outputTE).getMaxCharge();
            }
        }
        return 0;
    }
}
