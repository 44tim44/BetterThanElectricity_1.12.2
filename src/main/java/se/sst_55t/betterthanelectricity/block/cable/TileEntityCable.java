package se.sst_55t.betterthanelectricity.block.cable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
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
                    world.getTileEntity(this.pos.offset(facing)) instanceof ICable )
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
                    world.getTileEntity(this.pos.offset(facing)) instanceof ICable )
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
    public TileEntity getOutputTE(EnumFacing ignoreSide)
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
                    /*
                    if (((TileEntityCable) outputTE).getOutputTE() == this)
                    {
                        outputTE = null;
                    }
                    else
                    {
                        return ((TileEntityCable) outputTE).getOutputTE();
                    }
                    */
                    return ((TileEntityCable) outputTE).getOutputTE(facing.getOpposite());
                }
                else if(outputTE instanceof IGenerator)
                {
                    return outputTE;
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
    public TileEntity getInputTE(EnumFacing ignoreSide)
    {
        TileEntity inputTE = null;
        for (EnumFacing facing : EnumFacing.VALUES)
        {
            if(facing != ignoreSide) {
                inputTE = getConnectedBlockTE(facing);
            }

            if (inputTE != null)
            {
                if(inputTE instanceof TileEntityCable)
                {
                    /*
                    if (((TileEntityCable) inputTE).getInputTE() == this)
                    {
                        inputTE = null;
                    }
                    else
                    {
                        return ((TileEntityCable) inputTE).getInputTE();
                    }
                    */
                    return ((TileEntityCable) inputTE).getInputTE(facing.getOpposite());
                }
                else if(inputTE instanceof IConsumer)
                {
                    return inputTE;
                }
            }
        }
        return null;
    }
}