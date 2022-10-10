package net.winxdinf.windextra.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.winxdinf.windextra.Windextra;
import net.winxdinf.windextra.block.entity.NilProjectorBlockEntity;
import net.winxdinf.windextra.entity.projectile;

import java.util.UUID;

public class networking {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(projectile.SPAWN_PACKET, (context, b, packet, c) -> {
            double x = packet.readDouble();
            double y = packet.readDouble();
            double z = packet.readDouble();

            int entityId = packet.readInt();
            UUID entityUUID = packet.readUuid();
            context.execute(() -> {
                projectile proj = new projectile(MinecraftClient.getInstance().world, x, y, z, entityId, entityUUID);
                MinecraftClient.getInstance().world.addEntity(entityId, proj);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(projectile.EXPLODE_PACKET, (context, b, packet, c) -> {
            double x = packet.readDouble();
            double y = packet.readDouble();
            double z = packet.readDouble();
            ClientWorld world = MinecraftClient.getInstance().world;
            int entityId = packet.readInt();
            UUID entityUUID = packet.readUuid();
            context.execute(() -> {
                world.playSound(x,y,z, SoundEvents.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.NEUTRAL, 2f, 1.3f / (world.getRandom().nextFloat() * 0.4f + 0.8f), true);
                world.addParticle(ParticleTypes.SONIC_BOOM, x, y, z, 0, 1, 0);
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NilProjectorBlockEntity.PROJECTOR_PACKET, (context, b, packet, c) -> {
            Windextra.LOGGER.info("received packet");
            int charges = packet.readInt();
            BlockPos blockPos = packet.readBlockPos();
            ClientWorld world = MinecraftClient.getInstance().world;
            context.execute(() -> {
                BlockEntity blockEntity = world.getBlockEntity(blockPos);
                if (blockEntity != null) {
                    ((NilProjectorBlockEntity)blockEntity).setCharges(charges);
                }
            });
        });


    }
}
