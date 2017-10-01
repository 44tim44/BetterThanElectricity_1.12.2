package se.sst_55t.betterthanelectricity.sound;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

/**
 * Created by Timeout on 2017-10-01.
 */
public class LoopableSound extends PositionedSound implements ITickableSound{
    protected boolean donePlaying = false;
    private TileEntity tileEntity;

    public LoopableSound(ResourceLocation path, TileEntity tileEntity, float volume, float pitch) {
        super(path, SoundCategory.NEUTRAL);
        this.repeat = true;
        this.tileEntity = tileEntity;
        this.volume = volume;
        this.pitch = pitch;
        this.xPosF = tileEntity.getPos().getX();
        this.yPosF = tileEntity.getPos().getY();
        this.zPosF = tileEntity.getPos().getZ();
        this.repeatDelay = 0;
    }

    public LoopableSound(String path, TileEntity tileEntity, float volume, float pitch) {
        this(new ResourceLocation(path), tileEntity, volume, pitch);
    }

    @Override
    public void update() {
        if (this.tileEntity.isInvalid()) {
            this.donePlaying = true;
        }
    }

    @Override
    public boolean isDonePlaying() {
        return this.donePlaying;
    }
}