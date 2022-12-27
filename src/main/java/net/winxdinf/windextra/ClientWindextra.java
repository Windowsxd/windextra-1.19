package net.winxdinf.windextra;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.data.client.Model;
import net.winxdinf.windextra.block.ModBlocks;
import net.winxdinf.windextra.block.client.CKeyDetectorBlockEntityRenderer;
import net.winxdinf.windextra.block.client.NilProjectorBlockEntityRenderer;
import net.winxdinf.windextra.block.client.PearlDetectorBlockEntityRenderer;
import net.winxdinf.windextra.block.entity.ModBlockEntity;
import net.winxdinf.windextra.entity.ModEntities;
import net.winxdinf.windextra.entity.client.ProjectileRenderer;
import net.winxdinf.windextra.entity.projectile;
import net.winxdinf.windextra.event.KeyInputHandler;
import net.winxdinf.windextra.item.ModItems;
import net.winxdinf.windextra.item.advanced.TheNilItem;
import net.winxdinf.windextra.particle.ModParticles;
import net.winxdinf.windextra.particle.ProjectileParticle;
import net.winxdinf.windextra.util.ModModelPredicateProvider;
import net.winxdinf.windextra.util.networking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

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
		//BuiltinItemRendererRegistry.DynamicItemRenderer theNilRenderer = new TheNilItemRenderer();
		networking.init();
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PEARL_DETECTOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CHARGED_KEY_DETECTOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.NIL_PROJECTOR, RenderLayer.getCutout());


		BlockEntityRendererRegistry.register(ModBlockEntity.PEARLDETECTORENTITY, PearlDetectorBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(ModBlockEntity.CKEYDETECTORENTITY, CKeyDetectorBlockEntityRenderer::new);
		BlockEntityRendererRegistry.register(ModBlockEntity.NILPROJECTORENTITY, NilProjectorBlockEntityRenderer::new);


		//BuiltinItemRendererRegistry.INSTANCE.register(ModItems.THE_NIL, theNilRenderer);

		KeyInputHandler.register();

		EntityRendererRegistry.register(ModEntities.NIL_PROJECTILE, (dispatcher) -> new ProjectileRenderer(dispatcher));

		ModModelPredicateProvider.registerModModels();

		ParticleFactoryRegistry.getInstance().register(ModParticles.PROJECTILE_PARTICLE, ProjectileParticle.Factory::new);


		LOGGER.info("Windextras client initialized");


	}
}
