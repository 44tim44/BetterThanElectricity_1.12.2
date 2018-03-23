package se.sst_55t.betterthanelectricity.block.cable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Optional;
import se.sst_55t.betterthanelectricity.block.ICable;
import se.sst_55t.betterthanelectricity.block.IConsumer;
import se.sst_55t.betterthanelectricity.block.IGenerator;

/**
 * Created by Timeout on 2017-10-27.
 */
public class TileEntityCable extends TileEntity implements ICable {

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
            if (    world.getTileEntity(this.pos.offset(facing)) instanceof IConsumer ||
                    world.getTileEntity(this.pos.offset(facing)) instanceof IGenerator ||
                    world.getTileEntity(this.pos.offset(facing)) instanceof ICable && ((BlockCable)world.getBlockState(this.pos.offset(facing)).getBlock()).color == ((BlockCable)world.getBlockState(this.pos).getBlock()).color )
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
            if(     world.getTileEntity(this.pos.offset(facing)) instanceof IConsumer ||
                    world.getTileEntity(this.pos.offset(facing)) instanceof IGenerator ||
                    world.getTileEntity(this.pos.offset(facing)) instanceof ICable && ((BlockCable)world.getBlockState(this.pos.offset(facing)).getBlock()).color == ((BlockCable)world.getBlockState(this.pos).getBlock()).color )
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
    public TileEntity getGeneratorTE(EnumFacing ignoreSide)
    {
        TileEntity generatorTE;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            generatorTE = null;
            if(facing != ignoreSide)
            {
                generatorTE = getConnectedBlockTE(facing);
            }

            if (generatorTE != null)
            {
                if(generatorTE instanceof TileEntityCable)
                {
                    return ((TileEntityCable) generatorTE).getGeneratorTE(facing.getOpposite());
                }
                else if(generatorTE instanceof IGenerator)
                {
                    if(((IGenerator) generatorTE).isConnected()) return generatorTE;
                }
            }
        }
        return null;
    }

    /**
     * The block this is giving energy TO
     *
     * @return
     */
    public TileEntity getConsumerTE(EnumFacing ignoreSide)
    {
        TileEntity consumerTE;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            consumerTE = null;
            if(facing != ignoreSide) {
                consumerTE = getConnectedBlockTE(facing);
            }

            if (consumerTE != null)
            {
                if(consumerTE instanceof TileEntityCable)
                {
                    return ((TileEntityCable) consumerTE).getConsumerTE(facing.getOpposite());
                }
                else if(consumerTE instanceof IConsumer)
                {
                    if(((IConsumer) consumerTE).isConnected()) return consumerTE;
                }
            }
        }
        return null;
    }

}
