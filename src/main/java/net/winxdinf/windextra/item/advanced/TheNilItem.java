package net.winxdinf.windextra.item.advanced;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
import net.winxdinf.windextra.block.CKeyDetectorBlock;
import net.winxdinf.windextra.item.ModItems;
import net.winxdinf.windextra.util.BoxGenner;
import net.winxdinf.windextra.util.IEntityDataSaver;
import net.winxdinf.windextra.util.NilKeyData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static net.winxdinf.windextra.block.ModBlocks.CHARGED_KEY_DETECTOR;

public class TheNilItem extends Item {
    public TheNilItem(Settings settings) {
        super(settings);
    }

    public int getState(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) {return 1;}
        if (nbt.getInt("windextra.state") == 0) {
            return 1;
        }
        return nbt.getInt("windextra.state");
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        int state = getState(stack);
        NbtCompound stackData = stack.getNbt();
        if (state == 1) {
            return true;
        } else if (state == 2) {
            if (stackData != null) {
                if (stackData.getInt("windextra.activated") != 0) {
                    return true;
                }
            }
            return false;
        } else if (state == 3) {
            return false;
        } else if (state == 4) {
            return true;
        }
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        int state = getState(stack);
        if (state == 1) {
            ModItems.CHARGED_NIL_KEY.inventoryTick(stack, world, entity, slot, selected);
        } else if (state == 2) {
            ModItems.NIL_PEARL.inventoryTick(stack, world, entity, slot, selected);
        } else if (state == 3) {
            ModItems.PEARL_TAGGER.inventoryTick(stack, world, entity, slot, selected);
        } else if (state == 4) {
            ModItems.CKEY_TAGGER.inventoryTick(stack, world, entity, slot, selected);
        }
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        int state = getState(stack);

        if (state == 1) {
            if (!user.getItemCooldownManager().isCoolingDown(ModItems.CHARGED_NIL_KEY)) {
                return ModItems.CHARGED_NIL_KEY.useOnEntity(stack, user, entity, hand);
            }
        } else if (state == 2) {
            if (!user.getItemCooldownManager().isCoolingDown(ModItems.NIL_PEARL)) {
                return ModItems.NIL_PEARL.useOnEntity(stack, user, entity, hand);
            }
        } else if (state == 3) {
            if (!user.getItemCooldownManager().isCoolingDown(ModItems.PEARL_TAGGER)) {
                return ModItems.PEARL_TAGGER.useOnEntity(stack, user, entity, hand);
            }
        } else if (state == 4) {
            if (!user.getItemCooldownManager().isCoolingDown(ModItems.CKEY_TAGGER)) {
                return ModItems.CKEY_TAGGER.useOnEntity(stack, user, entity, hand);
            }
        }
        return ActionResult.success(user.world.isClient);
    }

    /* state guide
    1 = Charged Nil Key
    2 = Nil Pearl
    3 = Pearl Tag
    4 = Key Tag
     */

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        int state = getState(stack);

        if (state == 1) {
            if (!user.getItemCooldownManager().isCoolingDown(ModItems.CHARGED_NIL_KEY)) {
                return ModItems.CHARGED_NIL_KEY.use(world, user, hand);
            }
        } else if (state == 2) {
            if (!user.getItemCooldownManager().isCoolingDown(ModItems.NIL_PEARL)) {
                return ModItems.NIL_PEARL.use(world, user, hand);
            }
        } else if (state == 3) {
            if (!user.getItemCooldownManager().isCoolingDown(ModItems.PEARL_TAGGER)) {
                return ModItems.PEARL_TAGGER.use(world, user, hand);
            }
        } else if (state == 4) {
            if (!user.getItemCooldownManager().isCoolingDown(ModItems.CKEY_TAGGER)) {
                return ModItems.CKEY_TAGGER.use(world, user, hand);
            }
        }

        return TypedActionResult.pass(stack);
    }
}
