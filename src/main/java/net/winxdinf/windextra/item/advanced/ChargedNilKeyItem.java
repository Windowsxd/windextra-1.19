package net.winxdinf.windextra.item.advanced;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
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

public class ChargedNilKeyItem extends Item {
    public ChargedNilKeyItem(Settings settings) {
        super(settings);
    }


    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
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
            IEntityDataSaver CKeyUser = (IEntityDataSaver) entity;
            NbtCompound CKeyData = CKeyUser.getPersistentData();
            int CKeyUsage = CKeyData.getInt("CKeyUsage");
            if (CKeyUsage > 0) { //the charged nil key is asking for an entity
                int CKeyPosition = CKeyData.getInt("CKeyPos");
                MinecraftServer serverWorld = world.getServer();
                serverWorld.getWorlds().forEach((value) -> {
                    if (value.getRegistryKey().getValue().toString().equals("windextra:personal_dim")) {
                        List<Entity> entities = value.getEntitiesByClass(Entity.class, new Box((CKeyPosition * 256) - 17, 60, -17, (CKeyPosition * 256) + 16, 95, 16), target -> !target.equals(entity));
                        if (!entities.isEmpty()) {
                            var Target = entities.get(0);
                            world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 0.5f, 1f / (entity.world.getRandom().nextFloat() * 0.4f + 0.8f));
                            CKeyData.putInt("CKeyUsage", 0);
                            Entity tpedTarget = FabricDimensions.teleport(Target, (ServerWorld) world, new TeleportTarget(new Vec3d(CKeyData.getDouble("CKeyX"), CKeyData.getDouble("CKeyY"), CKeyData.getDouble("CKeyZ")), new Vec3d(0, 0, 0), 0f, 0f));

                        } else {
                            CKeyData.putInt("CKeyUsage", CKeyUsage - 1);
                        }
                    }
                });
            }
        }
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (user.isSneaking() && !user.getItemCooldownManager().isCoolingDown(this)) {
            user.getItemCooldownManager().set(this, 60);

            entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1f, 1f / (user.world.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!user.world.isClient) {
                user.world.getProfiler().push("nil_key_use");
                MinecraftServer serverWorld = user.world.getServer();

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
                        FileWriter myWriter = new FileWriter(NilKeyUsersPath);
                        if (LineNumber == 0) {
                            myWriter.write(NilKeyUsersContent + user.getUuidAsString());
                            LineUUIDWasFound = 1;

                        } else {
                            myWriter.write(NilKeyUsersContent + user.getUuidAsString() + "\n");
                            LineUUIDWasFound = LineNumber + 1;
                        }
                        myWriter.close();
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
                        if (isNewToNilKey) {
                            for (int blockY = 0; blockY <= 33; blockY++) {
                                for (int blockZ = -17; blockZ <= 16; blockZ++) {
                                    for (int blockX = -17; blockX <= 16; blockX++) {
                                        if (blockY == 0 || blockY == 33) {
                                            value.setBlockState(new BlockPos((FLineUUIDWasFound * 256) + blockX, 60 + blockY, blockZ), NIL_BLOCK.getDefaultState());
                                        } else if (blockX == -17 || blockZ == -17 || blockX == 16 || blockZ == 16) {
                                            value.setBlockState(new BlockPos((FLineUUIDWasFound * 256) + blockX, 60 + blockY, blockZ), NIL_BLOCK.getDefaultState());
                                        }
                                    }
                                }
                            }
                        }
                        FabricDimensions.teleport(entity, value, new TeleportTarget(new Vec3d(Nx, Ny, Nz), new Vec3d(0, 0, 0), 0f, 0f));

                    }
                });
                user.world.getProfiler().pop();
            }
        }
        return ActionResult.success(user.world.isClient);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.getItemCooldownManager().set(this, 60);
        BlockHitResult hitResult = ChargedNilKeyItem.raycast(world, user, RaycastContext.FluidHandling.ANY);
        if (user.isSneaking() && hitResult.getType() == HitResult.Type.BLOCK) {
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.NEUTRAL, 1f, 1f / (user.world.getRandom().nextFloat() * 0.4f + 0.8f));

        }
        if (!world.isClient()) {
            world.getProfiler().push("nil_key_use");
            IEntityDataSaver nilKeyUser = (IEntityDataSaver) user;
            NbtCompound KeyData = nilKeyUser.getPersistentData();
            //user.sendMessage(Text.of("used item"));
            //user.damage(DamageSource.MAGIC, 10);
            //RegistryKey<World> registryKey
            MinecraftServer serverWorld = world.getServer();
            String last_dimension = KeyData.getString("Dimension");
            double x = KeyData.getDouble("X");
            double y = KeyData.getDouble("Y");
            double z = KeyData.getDouble("Z");

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
                    FileWriter myWriter = new FileWriter(NilKeyUsersPath);
                    if (LineNumber == 0) {
                        myWriter.write(NilKeyUsersContent + user.getUuidAsString());
                        LineUUIDWasFound = 1;

                    } else {
                        myWriter.write(NilKeyUsersContent + user.getUuidAsString() + "\n");
                        LineUUIDWasFound = LineNumber + 1;
                    }
                    myWriter.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (user.isSneaking() && hitResult.getType() == HitResult.Type.BLOCK){
                last_dimension = "windextra:personal_dim";
                x = LineUUIDWasFound*256;
                y = 60 + user.getHeight();
                z = 0;
            } else if (last_dimension == "") {
                last_dimension = "windextra:personal_dim";
                x = LineUUIDWasFound*256;
                y = 60 + user.getHeight();
                z = 0;
                NilKeyData.setPosition(nilKeyUser, user.getPos(), world.getRegistryKey().getValue().toString());

            } else if (last_dimension != "") {
                NilKeyData.setPosition(nilKeyUser, new Vec3d(LineUUIDWasFound*256, 61, 0), "");
            }

            final double Nx = x;
            final double Ny = y;
            final double Nz = z;
            final boolean isNewToNilKey = isNew;
            final int FLineUUIDWasFound = LineUUIDWasFound;
            String fLast_Dimension = last_dimension;

            //user.sendMessage(Text.of("UUID was found on line: "+LineUUIDWasFound));
            serverWorld.getWorlds().forEach((value) -> {
                if (isNewToNilKey) {
                    if (value.getRegistryKey().getValue().toString().equals("windextra:personal_dim")) {
                        for (int blockY = 0; blockY <= 33; blockY++) {
                            for (int blockZ = -17; blockZ <= 16; blockZ++) {
                                for (int blockX = -17; blockX <= 16; blockX++) {
                                    if (blockY == 0 || blockY == 33) {
                                        value.setBlockState(new BlockPos((FLineUUIDWasFound*256) + blockX, 60 + blockY, blockZ), NIL_BLOCK.getDefaultState());
                                    } else if (blockX == -17 || blockZ == -17 || blockX == 16 || blockZ == 16) {
                                        value.setBlockState(new BlockPos((FLineUUIDWasFound*256) + blockX, 60 + blockY, blockZ), NIL_BLOCK.getDefaultState());
                                    }
                                }
                            }
                        }
                    }
                }

                if (value.getRegistryKey().getValue().toString().equals("windextra:personal_dim")) {
                    Chunk chink = value.getChunk(new BlockPos(Nx-3, 60, Nz-3));
                    Chunk chink2 = value.getChunk(new BlockPos(Nx+3, 60, Nz-3));
                    Chunk chink3 = value.getChunk(new BlockPos(Nx+3, 60, Nz+3));
                    Chunk chink4 = value.getChunk(new BlockPos(Nx-3, 60, Nz+3));

                    value.setChunkForced(chink.getPos().x, chink.getPos().z, true);
                    value.setChunkForced(chink2.getPos().x, chink2.getPos().z, true);
                    value.setChunkForced(chink3.getPos().x, chink3.getPos().z, true);
                    value.setChunkForced(chink4.getPos().x, chink4.getPos().z, true);
                }

                if (value.getRegistryKey().getValue().toString().equals(fLast_Dimension)) {
                    if (user.isSneaking() && hitResult.getType() == HitResult.Type.BLOCK) {

                        for (int blockY = 0; blockY <= 33; blockY++) {
                            for (int blockZ = -17; blockZ <= 16; blockZ++) {
                                for (int blockX = -17; blockX <= 16; blockX++) {
                                    BlockPos blockPos = new BlockPos((FLineUUIDWasFound*256) + blockX, 60 + blockY, blockZ);
                                    if (value.getBlockState(blockPos) == CHARGED_KEY_DETECTOR.getDefaultState()) {
                                        value.setBlockState(new BlockPos((FLineUUIDWasFound*256) + blockX, 60 + blockY, blockZ), CHARGED_KEY_DETECTOR.getDefaultState().with(TeleportDetectorBlock.POWERED, true));
                                        value.playSound(null, blockPos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, 0.6f);
                                        value.createAndScheduleBlockTick(blockPos, value.getBlockState(blockPos).getBlock(), 8);
                                    }
                                }
                            }
                        }

                        IEntityDataSaver CKeyUser = (IEntityDataSaver) user;
                        NbtCompound CKeyData = CKeyUser.getPersistentData();
                        CKeyData.putInt("CKeyPos", FLineUUIDWasFound);
                        CKeyData.putDouble("CKeyX", hitResult.getPos().x);
                        CKeyData.putDouble("CKeyY", hitResult.getPos().y);
                        CKeyData.putDouble("CKeyZ", hitResult.getPos().z);

                        CKeyData.putInt("CKeyUsage", 10);
                        //user.sendMessage(Text.of(Target.getName().getString()));
                    } else {
                        FabricDimensions.teleport(user, value, new TeleportTarget(new Vec3d(Nx, Ny, Nz), new Vec3d(0, 0, 0), 0f, 0f));
                    }
                }
            });
            world.getProfiler().pop();
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
