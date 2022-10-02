package net.winxdinf.windextra.world.biome;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.winxdinf.windextra.Windextra;

public class ModBiomes {
    public static final RegistryKey<Biome> NIL_BIOME_KEY = registerKey("nil_biome");
    public static final Biome NIL_BIOME = createNilBiome();

    private static Biome createNilBiome() {
        SpawnSettings.Builder spawnSettings = new SpawnSettings.Builder();
        GenerationSettings.Builder generationSettings = new GenerationSettings.Builder();

        return (new Biome.Builder()).precipitation(Biome.Precipitation.NONE).temperature(0.5f).downfall(0.0f).effects(
                (new BiomeEffects.Builder())
                        .fogColor(0x000000).skyColor(0x000000)
                        .waterColor(0x3f76e4).waterFogColor(0x050533)
                        .build())
                .spawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build())
                .build();
    }
    public static RegistryKey<Biome> registerKey(String name) {
        return RegistryKey.of(Registry.BIOME_KEY, new Identifier(Windextra.MODID, name));
    }
    public static void registerBiomes() {
        Registry.register(BuiltinRegistries.BIOME, NIL_BIOME_KEY.getValue(), NIL_BIOME);
    }
    public static void initBiomes() {

    }
}
