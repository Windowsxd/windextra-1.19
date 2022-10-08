package net.winxdinf.windextra;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.data.client.Model;
import net.winxdinf.windextra.block.ModBlocks;
import net.winxdinf.windextra.block.client.CKeyDetectorBlockEntityRenderer;
import net.winxdinf.windextra.block.client.PearlDetectorBlockEntityRenderer;
import net.winxdinf.windextra.block.entity.ModBlockEntity;
import net.winxdinf.windextra.entity.ModEntities;
import net.winxdinf.windextra.util.ModModelPredicateProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Environment(EnvType.CLIENT)
public class ClientWindextra implements ClientModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "windextra";
	public static final Logger LOGGER = LoggerFactory.getLogger("windextra");

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PEARL_DETECTOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHARGED_KEY_DETECTOR, RenderLayer.getCutout());


		BlockEntityRendererRegistry.register(ModBlockEntity.PEARLDETECTORENTITY, PearlDetectorBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(ModBlockEntity.CKEYDETECTORENTITY, CKeyDetectorBlockEntityRenderer::new);


		ModModelPredicateProvider.registerModModels();

		LOGGER.info("Windextras client initialized");


	}
}
