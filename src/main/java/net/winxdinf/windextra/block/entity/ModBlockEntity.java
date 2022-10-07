package net.winxdinf.windextra.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.winxdinf.windextra.Windextra;
import net.winxdinf.windextra.block.ModBlocks;

public class ModBlockEntity {
    public static BlockEntityType<PearlDetectorBlockEntity> PEARLDETECTORENTITY;
    public static BlockEntityType<CKeyDetectorBlockEntity> CKEYDETECTORENTITY;


    public static void registerBlockEntities() {
        PEARLDETECTORENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(Windextra.MODID, "pearl_detector"),
                FabricBlockEntityTypeBuilder.create(PearlDetectorBlockEntity::new,
                        ModBlocks.PEARL_DETECTOR).build(null));
        CKEYDETECTORENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(Windextra.MODID, "ckey_detector"),
                FabricBlockEntityTypeBuilder.create(CKeyDetectorBlockEntity::new,
                        ModBlocks.CHARGED_KEY_DETECTOR).build(null));

    }
}
