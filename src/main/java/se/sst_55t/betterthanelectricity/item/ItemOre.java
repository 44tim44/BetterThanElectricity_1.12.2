package se.sst_55t.betterthanelectricity.item;

/**
 * Created by Timmy on 2016-11-26.
 */
import net.minecraftforge.oredict.OreDictionary;

public class ItemOre extends ItemBase implements ItemOreDict {

    private String oreName;

    public ItemOre(String name, String oreName) {
        super(name);

        this.oreName = oreName;
    }

    @Override
    public void initOreDict() {
        OreDictionary.registerOre(oreName, this);
    }

}