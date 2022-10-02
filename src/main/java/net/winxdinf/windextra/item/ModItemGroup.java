package net.winxdinf.windextra.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.winxdinf.windextra.Windextra;

public class ModItemGroup {
    public static final ItemGroup Windextras = FabricItemGroupBuilder.build(new Identifier(Windextra.MODID, "windextras"),
            () -> new ItemStack(ModItems.NIL_KEY));
}
