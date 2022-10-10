package net.winxdinf.windextra.entity;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.winxdinf.windextra.Windextra;

import java.util.List;
import java.util.UUID;

public class projectile extends ThrownEntity {
    public static final Identifier SPAWN_PACKET = new Identifier(Windextra.MODID, "projector");
    public static final Identifier EXPLODE_PACKET = new Identifier(Windextra.MODID, "projectsplode");


    public projectile(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }
    @Environment(EnvType.CLIENT)
    public projectile(World world, double x, double y, double z, int id, UUID uuid) {
        super(ModEntities.NIL_PROJECTILE, world);
        updatePosition(x, y, z);
        updateTrackedPosition(x,y,z);
        setId(id);
    }
    @Override
    protected void initDataTracker() {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
        packet.writeDouble(getX());
        packet.writeDouble(getY());
        packet.writeDouble(getZ());
        packet.writeInt(getId());
        packet.writeUuid(getUuid());

        return ServerPlayNetworking.createS2CPacket(SPAWN_PACKET, packet);
    }


    public PacketByteBuf createBoomPacket() {
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
        packet.writeDouble(getX());
        packet.writeDouble(getY());
        packet.writeDouble(getZ());
        packet.writeInt(getId());
        packet.writeUuid(getUuid());

        return packet;
    }

    public void tick() {
        if (this.world.isClient == false && this.age > 60) {
            this.setNoGravity(false);
        } else if (this.world.isClient == false && this.age < 60) {
            this.setNoGravity(true);
        }
        super.tick();
        if (this.world.isClient && this.age % 2 < 2) {
            this.world.addParticle(ParticleTypes.FIREWORK, this.getX(), this.getY()+0.125, this.getZ(), this.random.nextGaussian() * 0.05, -this.getVelocity().y * 0.5, this.random.nextGaussian() * 0.05);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!this.world.isClient()) {
            for (ServerPlayerEntity player : PlayerLookup.world((ServerWorld) world)) {
                ServerPlayNetworking.send(player, EXPLODE_PACKET, createBoomPacket());
            }


            Vec3d vec3d = this.getPos();
            List<LivingEntity> list = this.world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(5.0));
            for (LivingEntity livingEntity : list) {
                if (livingEntity == this.getOwner() || this.squaredDistanceTo(livingEntity) > 25.0) continue;
                boolean bl = false;
                for (int i = 0; i < 2; ++i) {
                    Vec3d vec3d2 = new Vec3d(livingEntity.getX(), livingEntity.getBodyY(0.5 * (double)i), livingEntity.getZ());
                    BlockHitResult hitResult1 = this.world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this));
                    if (((HitResult)hitResult1).getType() != HitResult.Type.MISS) continue;
                    bl = true;
                    break;
                }
                if (!bl) continue;
                float g = 7 * (float)Math.sqrt((5.0 - (double)this.distanceTo(livingEntity)) / 5.0);
                livingEntity.damage(DamageSource.magic(this, this.getOwner()), g);
            }


            this.discard();
        }
        super.onCollision(hitResult);
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return DefaultedList.ofSize(4, ItemStack.EMPTY);
    }

    @Override
    public void equipStack(EquipmentSlot slot, ItemStack stack) {

    }
}
