package se.sst_55t.betterthanelectricity.block;

/**
 * Created by Timeout on 2017-08-20.
 */
import se.sst_55t.betterthanelectricity.item.ModItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;

public class BlockCropCorn extends BlockCrops {

    public BlockCropCorn() {
        setUnlocalizedName("crop_corn");
        setRegistryName("crop_corn");
    }

    @Override
    protected Item getSeed() {
        return ModItems.cornSeed;
    }

    @Override
    protected Item getCrop() {
        return ModItems.corn;
    }

}