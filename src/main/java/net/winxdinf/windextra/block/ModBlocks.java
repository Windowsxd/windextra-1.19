package net.winxdinf.windextra.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.winxdinf.windextra.Windextra;
import net.winxdinf.windextra.item.ModItemGroup;

public class ModBlocks {
    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(Windextra.MODID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(Windextra.MODID, name),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    public static final Block NIL_BLOCK = registerBlock("nil_block",
            new Block(FabricBlockSettings.of(Material.METAL).luminance(15).strength(-1.0f, 3600000.8f)), ModItemGroup.Windextras);
    public static final Block NIL_SCAFFOLD = registerBlock("nil_scaffold",
            new Block(FabricBlockSettings.of(Material.METAL).luminance(15).strength(-1.0f, 3600000.8f)), ModItemGroup.Windextras);

    public static final Block PEARL_DETECTOR = registerBlock("pearl_detector",
            new PearlDetectorBlock(FabricBlockSettings.of(Material.METAL).requiresTool().nonOpaque().strength(5.0f, 6.0f).sounds(BlockSoundGroup.METAL)), ModItemGroup.Windextras);
    public static final Block CHARGED_KEY_DETECTOR = registerBlock("ckey_detector",
            new CKeyDetectorBlock(FabricBlockSettings.copy(Blocks.GLASS).requiresTool().nonOpaque().strength(5.0f, 6.0f).sounds(BlockSoundGroup.METAL)), ModItemGroup.Windextras);

    public static final Block NIL_PROJECTOR = registerBlock("nil_projector",
            new NilProjectorBlock(FabricBlockSettings.copy(Blocks.GLASS).requiresTool().nonOpaque().strength(5.0f, 6.0f).sounds(BlockSoundGroup.METAL)), ModItemGroup.Windextras);


    public static void registerModBlocks() {
        //Windextra.LOGGER.info("registering items...");

    }
}
