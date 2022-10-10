package net.winxdinf.windextra.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.winxdinf.windextra.Windextra;

public class ModEntities {
    public static final EntityType<projectile> TEST_PROJECTILE = Registry.register(
            Registry.ENTITY_TYPE, new Identifier(Windextra.MODID, "test_projectile"), FabricEntityTypeBuilder.<projectile>create(SpawnGroup.MISC, projectile::new).dimensions(EntityDimensions.fixed(.25f,.25f)).build());

    public static void registerEntities() {
        //FabricDefaultAttributeRegistry.register(TEST_PROJECTILE, projectile.createLivingAttributes());
    }

}
