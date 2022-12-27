package net.winxdinf.windextra.item.advanced;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.world.ClientWorld;
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
import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.util.*;
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
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static net.winxdinf.windextra.block.ModBlocks.CHARGED_KEY_DETECTOR;
import static net.winxdinf.windextra.item.ModItems.*;
import static net.winxdinf.windextra.item.ModItems.CKEY_TAGGER;

public class TheNilItem extends Item {
    Item[] theNilStates = {CHARGED_NIL_KEY, NIL_PEARL, PEARL_TAGGER, CKEY_TAGGER};
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
    public boolean isFireproof() {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        int state = getState(stack);
        Item StateItem = theNilStates[state-1];
        StateItem.inventoryTick(stack, world, entity, slot, selected);
        if (world.isClient()) {
            PlayerEntity player = (PlayerEntity)entity;
            if (player.getItemCooldownManager().isCoolingDown(StateItem)) {
                player.getItemCooldownManager().set(this, 3000);
            } else {
                player.getItemCooldownManager().remove(this);
            }
        }

    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(theNilStates[getState(stack)-1].getName().copy().formatted(Formatting.LIGHT_PURPLE));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        int state = getState(stack);
        Item StateItem = theNilStates[state-1];
        if (!user.getItemCooldownManager().isCoolingDown(StateItem)) {
            return StateItem.useOnEntity(stack, user, entity, hand);
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
        Item StateItem = theNilStates[state-1];
        if (!user.getItemCooldownManager().isCoolingDown(StateItem)) {
            return StateItem.use(world, user, hand);
        }

        return TypedActionResult.pass(stack);
    }
}
