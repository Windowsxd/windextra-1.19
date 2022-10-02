package net.winxdinf.windextra.item.advanced;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.winxdinf.windextra.util.IEntityDataSaver;
import net.winxdinf.windextra.util.NilKeyData;
import org.apache.commons.compress.compressors.lz77support.LZ77Compressor;

import java.io.*;
import java.net.URI;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

import static net.winxdinf.windextra.block.ModBlocks.NIL_BLOCK;

public class NilKeyItem extends Item {
    public NilKeyItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.getItemCooldownManager().set(this, 60);
        if (!world.isClient()) {
            world.getProfiler().push("nil_key_use");
            IEntityDataSaver nilKeyUser = (IEntityDataSaver)user;
            NbtCompound KeyData = nilKeyUser.getPersistentData();
            //user.sendMessage(Text.of("used item"));
            //user.damage(DamageSource.MAGIC, 10);
            //RegistryKey<World> registryKey
            MinecraftServer serverWorld = world.getServer();
            String last_dimension = KeyData.getString("Dimension");
            double x = KeyData.getDouble("X");
            double y = KeyData.getDouble("Y");
            double z = KeyData.getDouble("Z");

            final String NilKeyUsersPath = serverWorld.getSavePath(WorldSavePath.ROOT).toAbsolutePath()+"NilKeyUsers.txt";
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
                        myWriter.write(NilKeyUsersContent + user.getUuidAsString()+ "\n");
                        LineUUIDWasFound = LineNumber + 1;
                    }
                    myWriter.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (last_dimension == "") {
                last_dimension = "windextra:personal_dim";
                x = LineUUIDWasFound*256;
                y = 61;
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
                    FabricDimensions.teleport(user, value, new TeleportTarget(new Vec3d(Nx,Ny,Nz), new Vec3d(0, 0, 0), 0f, 0f));
                }
            });
            world.getProfiler().pop();
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }
}
