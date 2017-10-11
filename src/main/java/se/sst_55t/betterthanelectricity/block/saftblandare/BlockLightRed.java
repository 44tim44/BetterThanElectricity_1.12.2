package se.sst_55t.betterthanelectricity.block.saftblandare;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import se.sst_55t.betterthanelectricity.block.BlockTileEntity;

import javax.annotation.Nullable;

/**
 * Created by Timeout on 2017-10-11.
 */
public class BlockLightRed extends BlockTileEntity<TileEntityLightRed> {
    public BlockLightRed(Material material, String name) {
        super(material, name);
        //this.setLightLevel(1.0F);
    }

    @Override
    public Class<TileEntityLightRed> getTileEntityClass() {
        return TileEntityLightRed.class;
    }

    @Nullable
    @Override
    public TileEntityLightRed createTileEntity(World world, IBlockState state) {
        return new TileEntityLightRed();
    }
}
