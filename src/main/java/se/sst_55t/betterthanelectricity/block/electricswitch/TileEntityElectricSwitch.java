package se.sst_55t.betterthanelectricity.block.electricswitch;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import se.sst_55t.betterthanelectricity.block.ICable;
import se.sst_55t.betterthanelectricity.block.IConsumer;
import se.sst_55t.betterthanelectricity.block.IElectricityStorage;
import se.sst_55t.betterthanelectricity.block.IGenerator;
import se.sst_55t.betterthanelectricity.block.cable.TileEntityCable;
import se.sst_55t.betterthanelectricity.block.multiSocket.TileEntityMultiSocketOut;

/**
 * Created by Timeout on 2017-10-27.
 */
public class TileEntityElectricSwitch extends TileEntity implements IConsumer, IGenerator, IElectricityStorage {

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
            if (world.getTileEntity(this.pos.offset(facing)) instanceof ICable)
            {
                return world.getTileEntity(this.pos.offset(facing));
            }
        }
        return null;
    }

    public boolean isConnected()
    {
        EnumFacing consumerSide = this.world.getBlockState(this.pos).getValue(BlockElectricSwitch.FACING);
        EnumFacing generatorSide = consumerSide.getOpposite();
        TileEntity consumerSideTE = world.getTileEntity(this.pos.offset(consumerSide));
        TileEntity generatorSideTE = world.getTileEntity(this.pos.offset(generatorSide));

        return (consumerSideTE instanceof ICable && generatorSideTE instanceof ICable) && world.getBlockState(this.pos).getValue(BlockElectricSwitch.POWERED);
    }

    /**
     * The block this is getting energy FROM
     *
     * @return
     */
    @Override
    public TileEntity getGeneratorTE()
    {
        EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockElectricSwitch.FACING);
        facing = facing.getOpposite();
        TileEntity generatorTE;
        //for (EnumFacing facing : EnumFacing.VALUES)
        //{
            generatorTE = getConnectedBlockTE(facing);

            if (generatorTE != null && generatorTE instanceof TileEntityCable)
            {
                if(((TileEntityCable) generatorTE).getGeneratorTE(facing.getOpposite()) instanceof IGenerator) {
                    return ((TileEntityCable) generatorTE).getGeneratorTE(facing.getOpposite());
                }
            }
        //}
        return null;
    }

    @Override
    public float getConsumeRate() {
        return 0;
    }

    /**
     * The block this is giving energy TO
     *
     * @return
     */
    @Override
    public TileEntity getConsumerTE() {
        EnumFacing facing = this.world.getBlockState(this.pos).getValue(BlockElectricSwitch.FACING);
        TileEntity consumerTE;
        //for (EnumFacing facing : EnumFacing.VALUES)
        //{
            consumerTE = getConnectedBlockTE(facing);

            if (consumerTE != null && consumerTE instanceof TileEntityCable) {
                if(((TileEntityCable) consumerTE).getConsumerTE(facing.getOpposite()) instanceof IConsumer)
                {
                    return ((TileEntityCable) consumerTE).getConsumerTE(facing.getOpposite());
                }
            }
        //}
        return null;
    }

    @Override
    public float getChargeRate()
    {
        TileEntity te = getGeneratorTE();
        if(te != null && te instanceof IGenerator)
        {
            if (te instanceof TileEntityMultiSocketOut)
            {
                TileEntity[] consumerTEList = ((TileEntityMultiSocketOut) te).getConsumerTEList(null);
                for (TileEntity consumerTE : consumerTEList)
                {
                    if (consumerTE == this)
                    {
                        return ((IGenerator) te).getChargeRate();
                    }
                }
            }
            else if (((IGenerator) te).getConsumerTE() == this)
            {
                return ((IGenerator) te).getChargeRate();
            }
        }
        return 0;
    }

    @Override
    public void increaseCharge()
    {
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
        TileEntity generatorTE = getGeneratorTE();
        if(generatorTE != null && generatorTE instanceof IElectricityStorage)
        {
            if(generatorTE instanceof TileEntityMultiSocketOut)
            {
                TileEntity[] consumerTEList = ((TileEntityMultiSocketOut)generatorTE).getConsumerTEList(null);
                for(TileEntity consumerTE : consumerTEList)
                {
                    if(consumerTE == this)
                    {
                        return ((IElectricityStorage) generatorTE).getCharge();
                    }
                }
            }
            else if(((IGenerator)generatorTE).getConsumerTE() == this)
            {
                return ((IElectricityStorage) generatorTE).getCharge();
            }
        }
        return 0;
    }

    @Override
    public int getMaxCharge() {
        TileEntity generatorTE = getGeneratorTE();
        if(generatorTE != null && generatorTE instanceof IElectricityStorage)
        {
            if(generatorTE instanceof TileEntityMultiSocketOut)
            {
                TileEntity[] consumerTEList = ((TileEntityMultiSocketOut)generatorTE).getConsumerTEList(null);
                for(TileEntity consumerTE : consumerTEList)
                {
                    if(consumerTE == this)
                    {
                        return ((IElectricityStorage) generatorTE).getMaxCharge();
                    }
                }
            }
            else if(((IGenerator)generatorTE).getConsumerTE() == this)
            {
                return ((IElectricityStorage) generatorTE).getMaxCharge();
            }
        }
        return 0;
    }
}
