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
public class TileEntityMultiSocketIn extends TileEntity implements IGenerator, IConsumer, IElectricityStorage {

    private static final int maxCharge = 10;
    private int currentCharge;

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
        EnumFacing inputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketIn.FACING);
        return (amountOfConnections >= 2) && world.getTileEntity(this.pos.offset(inputSide)) instanceof ICable;
    }

    /**
     * The blocks this is is getting energy FROM
     *
     * @return
     */
    public TileEntity[] getGeneratorTEList(@Nullable EnumFacing ignoreSide)
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
                    else if(outputTE == getConsumerTE())
                    {
                        outputTE = null;
                    }
                }
            }

            if (outputTE != null)
            {
                if(outputTE instanceof TileEntityCable)
                {
                    TileEntity outputTEEnd = ((TileEntityCable)outputTE).getGeneratorTE(facing.getOpposite());
                    if(outputTEEnd == this)
                    {
                        outputTEEnd = null;
                    }
                    else if(outputTEEnd == getConsumerTE())
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
    public TileEntity getGeneratorTE() {
        return null;
    }

    /**
     * The block this is giving energy TO
     *
     * @return
     */
    @Override
    public TileEntity getConsumerTE() {
        EnumFacing inputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketIn.FACING);
        TileEntity inputTE = getConnectedBlockTE(inputSide);
        if (inputTE != null)
        {
            if(inputTE instanceof TileEntityCable)
            {
                return ((TileEntityCable) inputTE).getConsumerTE(inputSide.getOpposite());
            }
        }
        return null;
    }

    @Override
    public float getConsumeRate() {
        return ((IConsumer) getConsumerTE()).getConsumeRate();
    }

    @Override
    public float getChargeRate()
    {
        float total = 0;
        EnumFacing inputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketIn.FACING);
        TileEntity[] generatorTEList = getGeneratorTEList(inputSide);
        for(int i = 0; i < 6; i++)
        {
            if(generatorTEList[i] != null && generatorTEList[i] instanceof IGenerator)
            {
                if(generatorTEList[i] instanceof TileEntityMultiSocketOut)
                {
                    TileEntity[] consumerTEList = ((TileEntityMultiSocketOut) generatorTEList[i]).getConsumerTEList(null);
                    for(TileEntity consumerTE : consumerTEList)
                    {
                        if(consumerTE == this)
                        {
                            total += ((IGenerator) generatorTEList[i]).getChargeRate();
                        }
                    }
                }
                else if(((IGenerator)generatorTEList[i]).getConsumerTE() == this)
                {
                    total += ((IGenerator) generatorTEList[i]).getChargeRate();
                }
            }
        }
        return total;
    }

    @Override
    public void increaseCharge() {
        TileEntity inputTE = getConsumerTE();
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
        TileEntity[] outputTEList = getGeneratorTEList(inputSide);
        for(int i = 0; i < 6; i++){
            if(outputTEList[i] != null && outputTEList[i] instanceof IElectricityStorage){
                countedIndex[count] = i;
                count++;
            }
        }
        if(count > 0)
        {
            this.counter++;
            if(this.counter > 5) this.counter = 0;
            int index = counter % count;

            for(int i = 0; i < count; i++)
            {
                if(!(((IElectricityStorage) outputTEList[countedIndex[index]]).getCharge() > 0))
                {
                    this.counter++;
                    if (this.counter > 5) this.counter = 0;
                    index = counter % count;
                }
            }
            if(((IElectricityStorage) outputTEList[countedIndex[index]]).getCharge() > 0)
            {
                ((IElectricityStorage) outputTEList[countedIndex[index]]).decreaseCharge();
            }
        }
    }

    @Override
    public void setCharge(int value) {
    }

    @Override
    public int getCharge() {
        int total = 0;
        EnumFacing inputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketIn.FACING);
        TileEntity[] generatorTEList = getGeneratorTEList(inputSide);
        for(int i = 0; i < 6; i++)
        {
            if(generatorTEList[i] != null && generatorTEList[i] instanceof IElectricityStorage)
            {
                if(generatorTEList[i] instanceof TileEntityMultiSocketOut)
                {
                    TileEntity[] consumerTEList = ((TileEntityMultiSocketOut) generatorTEList[i]).getConsumerTEList(null);
                    for(TileEntity consumerTE : consumerTEList)
                    {
                        if(consumerTE == this)
                        {
                            total += ((IElectricityStorage) generatorTEList[i]).getCharge();
                        }
                    }
                }
                else if(((IGenerator)generatorTEList[i]).getConsumerTE() == this)
                {
                    total += ((IElectricityStorage) generatorTEList[i]).getCharge();
                }
            }
        }
        return total;
    }

    @Override
    public int getMaxCharge() {
        int total = 0;
        EnumFacing inputSide = this.world.getBlockState(this.pos).getValue(BlockMultiSocketIn.FACING);
        TileEntity[] generatorTEList = getGeneratorTEList(inputSide);
        for(int i = 0; i < 6; i++)
        {
            if(generatorTEList[i] != null && generatorTEList[i] instanceof IElectricityStorage)
            {
                if(generatorTEList[i] instanceof TileEntityMultiSocketOut)
                {
                    TileEntity[] consumerTEList = ((TileEntityMultiSocketOut) generatorTEList[i]).getConsumerTEList(null);
                    for(TileEntity consumerTE : consumerTEList)
                    {
                        if(consumerTE == this)
                        {
                            total += ((IElectricityStorage) generatorTEList[i]).getMaxCharge();
                        }
                    }
                }
                else if(((IGenerator)generatorTEList[i]).getConsumerTE() == this)
                {
                    total += ((IElectricityStorage) generatorTEList[i]).getMaxCharge();
                }
            }
        }
        return total;
    }
}
