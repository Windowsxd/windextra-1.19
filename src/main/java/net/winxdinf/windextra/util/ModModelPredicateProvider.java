package net.winxdinf.windextra.util;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.winxdinf.windextra.Windextra;
import net.winxdinf.windextra.item.ModItems;

public class ModModelPredicateProvider {
    public static void registerModModels() {
        registerPearl(ModItems.NIL_PEARL);
        registerNilItem(ModItems.THE_NIL);
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

    private static void registerNilItem(Item NilItem) {
        ModelPredicateProviderRegistry.register(NilItem, new Identifier("state"),
                (stack, world, entity, seed) -> {
                    NbtCompound stackData = stack.getNbt();
                    if (stackData != null) {
                        if (stackData.getInt("windextra.state") != 0) {
                            //Windextra.LOGGER.info("state: "+stackData.getInt("windextra.state"));
                            return ((float)stackData.getInt("windextra.state"))/5f;
                        }
                    }
                    //Windextra.LOGGER.info("state: 1 (did not find)");
                    return 0.2f;
                });
        ModelPredicateProviderRegistry.register(NilItem, new Identifier("activated"),
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
