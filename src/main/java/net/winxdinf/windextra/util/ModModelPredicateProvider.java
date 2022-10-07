package net.winxdinf.windextra.util;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.winxdinf.windextra.item.ModItems;

public class ModModelPredicateProvider {
    public static void registerModModels() {
        registerPearl(ModItems.NIL_PEARL);
    }

    private static void registerPearl(Item pearl) {
        ModelPredicateProviderRegistry.register(pearl, new Identifier("activated"),
                (stack, world, entity, seed) -> {
                    NbtCompound stackData = stack.getNbt();
                    if (stackData != null) {
                        if (stackData.getInt("windextra.activated") != 0) {
                            return 1.0f;
                        }
                    }
                    return 0.0f;
                });
    }
}
