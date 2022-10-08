package net.winxdinf.windextra.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.winxdinf.windextra.Windextra;

public class ModEntities {
    //public static final EntityType<LiteNingEntity> LITENING = Registry.register(
            //Registry.ENTITY_TYPE, new Identifier(Windextra.MODID, "litening"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, LiteNingEntity::new).build());

    public static void registerEntities() {
        //FabricDefaultAttributeRegistry.register(LITENING, LiteNingEntity.createLivingAttributes());
    }

}
