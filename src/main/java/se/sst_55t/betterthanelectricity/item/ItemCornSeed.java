package se.sst_55t.betterthanelectricity.item;

/**
 * Created by Timeout on 2017-08-20.
 */
import se.sst_55t.betterthanelectricity.BTEMod;
import se.sst_55t.betterthanelectricity.block.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;

public class ItemCornSeed extends ItemSeeds {

    public ItemCornSeed() {
        super(ModBlocks.cropCorn, Blocks.FARMLAND);
        setUnlocalizedName("corn_seed");
        setRegistryName("corn_seed");
    }

    public void registerItemModel() {
        BTEMod.proxy.registerItemRenderer(this, 0, "corn_seed");
    }

}