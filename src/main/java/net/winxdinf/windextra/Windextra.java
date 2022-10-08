package net.winxdinf.windextra;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.winxdinf.windextra.block.ModBlocks;
import net.winxdinf.windextra.block.entity.ModBlockEntity;
import net.winxdinf.windextra.entity.ModEntities;
import net.winxdinf.windextra.item.ModItems;
import net.winxdinf.windextra.world.biome.ModBiomes;
import net.winxdinf.windextra.world.dimension.ModDimensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Windextra implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "windextra";
	public static final Logger LOGGER = LoggerFactory.getLogger("windextra");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModBlockEntity.registerBlockEntities();
		ModDimensions.register();
		ModBiomes.initBiomes();
		ModBiomes.registerBiomes();

		ModEntities.registerEntities();

		LOGGER.info("Windextras initialized");
	}
}
