package se.sst_55t.betterthanelectricity.block;

import se.sst_55t.betterthanelectricity.BTEMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

/**
 * Created by Timeout on 2017-08-20.
 */
public abstract class BlockContainerBase extends BlockContainer implements
        ITileEntityProvider{
    protected String name;

    public BlockContainerBase(Material materialIn, String name) {
        super(materialIn);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public void registerItemModel(Item itemBlock) {
        BTEMod.proxy.registerItemRenderer(itemBlock, 0, name);
    }
}