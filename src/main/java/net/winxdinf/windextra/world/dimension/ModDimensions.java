package net.winxdinf.windextra.world.dimension;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.winxdinf.windextra.Windextra;

public class ModDimensions {
    public static final RegistryKey<World> NilDIM_key = RegistryKey.of(Registry.WORLD_KEY,
            new Identifier(Windextra.MODID, "personal_dim"));
    public static final RegistryKey<DimensionType> NilDIM_Type_Key = RegistryKey.of(Registry.DIMENSION_TYPE_KEY,
            NilDIM_key.getValue());

    public static void register() {

    }
}
