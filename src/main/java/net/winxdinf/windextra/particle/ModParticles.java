package net.winxdinf.windextra.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.winxdinf.windextra.Windextra;

public class ModParticles {
    public static final DefaultParticleType PROJECTILE_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(Windextra.MODID, "projectile_particle"), PROJECTILE_PARTICLE);
    }
}
