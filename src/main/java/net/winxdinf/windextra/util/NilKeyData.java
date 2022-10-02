package net.winxdinf.windextra.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class NilKeyData {
    public static NbtCompound setPosition(IEntityDataSaver player, Vec3d position, String world) {
        NbtCompound nbt = player.getPersistentData();
        double posX = nbt.getDouble("X");
        double posY = nbt.getDouble("Y");
        double posZ = nbt.getDouble("Z");
        String Dimension = nbt.getString("Dimension");
        nbt.putDouble("X", position.x);
        nbt.putDouble("Y", position.y);
        nbt.putDouble("Z", position.z);
        nbt.putString("Dimension", world);

        return nbt;
    }
}
