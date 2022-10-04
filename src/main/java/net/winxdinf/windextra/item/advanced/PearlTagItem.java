package net.winxdinf.windextra.item.advanced;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.winxdinf.windextra.util.IEntityDataSaver;
import net.winxdinf.windextra.util.NilKeyData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static net.winxdinf.windextra.block.ModBlocks.NIL_BLOCK;

public class PearlTagItem extends Item {
    public PearlTagItem(Settings settings) {
        super(settings);
    }

    @Override

    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        //entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.CHIME, SoundCategory.NEUTRAL, 1f, 1f / (user.world.getRandom().nextFloat() * 0.4f + 0.8f));
        NbtCompound data = ((IEntityDataSaver)entity).getPersistentData();
        boolean isExempt = data.getBoolean("PearlTPExempt");
        if (!user.world.isClient) {
            if (isExempt) {
                data.putBoolean("PearlTPExempt", false);
                user.sendMessage(Text.of("Pearl Acknowledges "+entity.getName().getString()),true);
            } else {
                data.putBoolean("PearlTPExempt", true);
                user.sendMessage(Text.of("Pearl Ignores "+entity.getName().getString()),true);
            }
        }
        return ActionResult.success(user.world.isClient);
    }
}
