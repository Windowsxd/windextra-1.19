package net.winxdinf.windextra.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.winxdinf.windextra.Windextra;
import net.winxdinf.windextra.item.advanced.ChargedNilKeyItem;
import net.winxdinf.windextra.item.advanced.NilPearlItem;
import net.winxdinf.windextra.item.advanced.NilKeyItem;

public class ModItems {
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(Windextra.MODID, name), item);
    }

    public static final Item NIL_KEY = registerItem("nil_key",
            new NilKeyItem(new FabricItemSettings().group(ModItemGroup.Windextras).maxCount(1).fireproof()));
    public static final Item CHARGED_NIL_KEY = registerItem("charged_nil_key",
            new ChargedNilKeyItem(new FabricItemSettings().group(ModItemGroup.Windextras).maxCount(1).fireproof()));
    public static final Item NIL_PEARL = registerItem("nil_pearl",
            new NilPearlItem(new FabricItemSettings().group(ModItemGroup.Windextras).maxCount(1).fireproof()));

    public static void registerModItems() {
    //Windextra.LOGGER.info("registering items...");
    }
}
