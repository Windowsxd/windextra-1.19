package net.winxdinf.windextra.item.advanced;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.winxdinf.windextra.block.TeleportDetectorBlock;
import net.winxdinf.windextra.util.IEntityDataSaver;
import net.winxdinf.windextra.util.NilKeyData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static net.winxdinf.windextra.block.ModBlocks.*;

public class NilPearlItem extends Item {
    public NilPearlItem(Settings settings) {
        super(settings);
    }


    public void setVelocity(Entity entity, double x, double y, double z, float speed, float divergence) {
        Vec3d vec3d = new Vec3d(x, y, z).normalize().add(entity.world.random.nextTriangular(0.0, 0.0172275 * (double)divergence), entity.world.random.nextTriangular(0.0, 0.0172275 * (double)divergence), entity.world.random.nextTriangular(0.0, 0.0172275 * (double)divergence)).multiply(speed);
        entity.setVelocity(vec3d);
        double d = vec3d.horizontalLength();
        entity.setYaw((float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875));
        entity.setPitch((float)(MathHelper.atan2(vec3d.y, d) * 57.2957763671875));
        entity.prevYaw = entity.getYaw();
        entity.prevPitch = entity.getPitch();
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient()) {
            IEntityDataSaver NilPearlUser = (IEntityDataSaver) entity;
            NbtCompound PearlData = NilPearlUser.getPersistentData();
            int NilPearlUsage = PearlData.getInt("NilPearlUsage");
            if (NilPearlUsage > 0) { //the pearl is asking for an entity
                int NilPearlPosition = PearlData.getInt("NilPearlPos");
                MinecraftServer serverWorld = world.getServer();
                serverWorld.getWorlds().forEach((value) -> {
                    if (value.getRegistryKey().getValue().toString().equals("windextra:personal_dim")) {
                        List<Entity> entities = value.getEntitiesByClass(Entity.class, new Box((NilPearlPosition * 256) - 17, 60, -17, (NilPearlPosition * 256) + 16, 95, 16), target -> !target.equals(entity));
                        if (!entities.isEmpty()) {
                            var Target = entities.get(0);
                            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.5f, 1f / (entity.world.getRandom().nextFloat() * 0.4f + 0.8f));
                            PearlData.putInt("NilPearlUsage", 0);
                            Entity tpedTarget = FabricDimensions.teleport(Target, (ServerWorld) world, new TeleportTarget(entity.getEyePos(), new Vec3d(0, 0, 0), 0f, 0f));
                            if (ProjectileEntity.class.isAssignableFrom(Target.getClass())) {
                                ((ProjectileEntity)tpedTarget).setVelocity(entity, entity.getPitch(), entity.getYaw(), 0.0f, 1.5f, 1.0f);
                            } else {
                                if (ItemEntity.class.isAssignableFrom(Target.getClass())) {
                                    ((ItemEntity)Target).setToDefaultPickupDelay();
                                }
                                float pitch = entity.getPitch();
                                float yaw = entity.getYaw();
                                float roll = 0.0f;
                                float speed = 1.5f;
                                float divergence = .0f;
                                float f = -MathHelper.sin(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));
                                float g = -MathHelper.sin((pitch + roll) * ((float) Math.PI / 180));
                                float h = MathHelper.cos(yaw * ((float) Math.PI / 180)) * MathHelper.cos(pitch * ((float) Math.PI / 180));
                                setVelocity(tpedTarget, f, g, h, speed, divergence);
                                Vec3d vec3d = entity.getVelocity();
                                tpedTarget.setVelocity(tpedTarget.getVelocity().add(vec3d.x, entity.isOnGround() ? 0.0 : vec3d.y, vec3d.z));
                            }
                        } else {
                            PearlData.putInt("NilPearlUsage", NilPearlUsage - 1);
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
/*
ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        user.getItemCooldownManager().set(this, 20);
        if (!world.isClient) {
            EnderPearlEntity enderPearlEntity = new EnderPearlEntity(world, user);
            enderPearlEntity.setItem(itemStack);
            enderPearlEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
            world.spawnEntity(enderPearlEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
 */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.getItemCooldownManager().set(this, 10);

        BlockHitResult hitResult = ChargedNilKeyItem.raycast(world, user, RaycastContext.FluidHandling.ANY);
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (user.world.getRandom().nextFloat() * 0.4f + 0.8f));

        if (!world.isClient()) {
            world.getProfiler().push("nil_key_use");
            MinecraftServer serverWorld = world.getServer();

            final String NilKeyUsersPath = serverWorld.getSavePath(WorldSavePath.ROOT).toAbsolutePath() + "NilKeyUsers.txt";
            int LineUUIDWasFound = 0;
            boolean isNew = false;
            try {

                File NilKeyUsers = new File(NilKeyUsersPath);
                if (!NilKeyUsers.exists()) {
                    NilKeyUsers.createNewFile();
                }
                Scanner myReader = new Scanner(NilKeyUsers);
                String NilKeyUsersContent = "";
                int LineNumber = 0;

                while (myReader.hasNextLine()) {
                    LineNumber = LineNumber + 1;
                    String data = myReader.nextLine();
                    if (data.equals(user.getUuidAsString())) {
                        LineUUIDWasFound = LineNumber;
                    }
                    NilKeyUsersContent = NilKeyUsersContent + data + "\n";
                }

                myReader.close();

                if (LineUUIDWasFound == 0) {
                    isNew = true;
                    if (LineNumber == 0) {
                        LineUUIDWasFound = 1;

                    } else {
                        LineUUIDWasFound = LineNumber + 1;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            final double Nx = LineUUIDWasFound * 256;
            final double Ny = 61;
            final double Nz = 0;
            final boolean isNewToNilKey = isNew;
            final int FLineUUIDWasFound = LineUUIDWasFound;

            //user.sendMessage(Text.of("UUID was found on line: "+LineUUIDWasFound));
            serverWorld.getWorlds().forEach((value) -> {
                if (value.getRegistryKey().getValue().toString().equals("windextra:personal_dim")) {
                    Chunk chink = value.getChunk(new BlockPos(Nx-3, 60, Nz-3));
                    Chunk chink2 = value.getChunk(new BlockPos(Nx+3, 60, Nz-3));
                    Chunk chink3 = value.getChunk(new BlockPos(Nx+3, 60, Nz+3));
                    Chunk chink4 = value.getChunk(new BlockPos(Nx-3, 60, Nz+3));

                    value.setChunkForced(chink.getPos().x, chink.getPos().z, true);
                    value.setChunkForced(chink2.getPos().x, chink2.getPos().z, true);
                    value.setChunkForced(chink3.getPos().x, chink3.getPos().z, true);
                    value.setChunkForced(chink4.getPos().x, chink4.getPos().z, true);

                    for (int blockY = 0; blockY <= 33; blockY++) {
                        for (int blockZ = -17; blockZ <= 16; blockZ++) {
                            for (int blockX = -17; blockX <= 16; blockX++) {
                                BlockPos blockPos = new BlockPos((FLineUUIDWasFound*256) + blockX, 60 + blockY, blockZ);
                                if (value.getBlockState(blockPos) == PEARL_DETECTOR.getDefaultState()) {
                                    value.setBlockState(new BlockPos((FLineUUIDWasFound*256) + blockX, 60 + blockY, blockZ), PEARL_DETECTOR.getDefaultState().with(TeleportDetectorBlock.POWERED, true));
                                    value.playSound(null, blockPos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, 0.6f);
                                    value.createAndScheduleBlockTick(blockPos, value.getBlockState(blockPos).getBlock(), 8);
                                }
                            }
                        }
                    }

                    IEntityDataSaver NilPearlUser = (IEntityDataSaver) user;
                    NbtCompound PearlData = NilPearlUser.getPersistentData();
                    PearlData.putInt("NilPearlPos", FLineUUIDWasFound);
                    PearlData.putInt("NilPearlUsage", 10);
                }
            });
            world.getProfiler().pop();
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
