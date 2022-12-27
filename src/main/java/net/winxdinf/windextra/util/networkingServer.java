package net.winxdinf.windextra.util;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.winxdinf.windextra.Windextra;
import net.winxdinf.windextra.block.entity.NilProjectorBlockEntity;
import net.winxdinf.windextra.entity.projectile;
import net.winxdinf.windextra.item.ModItems;
import net.winxdinf.windextra.item.advanced.NilKeyItem;

import java.lang.reflect.Array;
import java.util.List;
import java.util.UUID;

import static net.winxdinf.windextra.item.ModItems.*;

public class networkingServer {
    public static final Identifier NITEMSPACKET = new Identifier(Windextra.MODID, "nitemscroller");

    public static void init() {

        ServerPlayNetworking.registerGlobalReceiver(NITEMSPACKET, (context, sender, serverPlayNetworkHandler, packet, packetSender) -> {
            //Windextra.LOGGER.info("received NITEMSPACKET");
            Item[] theNilStates = {CHARGED_NIL_KEY, NIL_PEARL, PEARL_TAGGER, CKEY_TAGGER};
            String request = packet.readString();
            context.execute(() -> {
                if (request.equals("next")) {
                    if (sender.getMainHandStack().getItem() == ModItems.THE_NIL) {
                        ItemStack stack = sender.getMainHandStack();
                        NbtCompound nbt = stack.getNbt();
                        if (nbt == null) { nbt = new NbtCompound();}
                        int state = nbt.getInt("windextra.state");
                        if (state < 4) {
                            nbt.putInt("windextra.state", state + 1);
                            sender.sendMessage(theNilStates[state].getName(), true);
                        }
                        stack.setNbt(nbt);
                    }
                    if (sender.getOffHandStack().getItem() == ModItems.THE_NIL) {
                        ItemStack stack = sender.getOffHandStack();
                        NbtCompound nbt = stack.getNbt();
                        if (nbt == null) { nbt = new NbtCompound();}
                        int state = nbt.getInt("windextra.state");
                        if (state < 4) {
                            nbt.putInt("windextra.state", state + 1);
                            sender.sendMessage(theNilStates[state].getName(), true);
                        }
                        stack.setNbt(nbt);
                    }

                } else if(request.equals("prev")) {
                    if (sender.getMainHandStack().getItem() == ModItems.THE_NIL) {
                        ItemStack stack = sender.getMainHandStack();
                        NbtCompound nbt = stack.getNbt();
                        if (nbt == null) { nbt = new NbtCompound();}
                        int state = nbt.getInt("windextra.state");
                        if (state > 1) {
                            nbt.putInt("windextra.state", state - 1);
                            sender.sendMessage(theNilStates[state-2].getName(), true);
                        }
                        stack.setNbt(nbt);
                    }
                    if (sender.getOffHandStack().getItem() == ModItems.THE_NIL) {
                        ItemStack stack = sender.getOffHandStack();
                        NbtCompound nbt = stack.getNbt();
                        if (nbt == null) { nbt = new NbtCompound();}
                        int state = nbt.getInt("windextra.state");
                        if (state > 1) {
                            nbt.putInt("windextra.state", state - 1);
                            sender.sendMessage(theNilStates[state-2].getName(), true);
                        }
                        stack.setNbt(nbt);
                    }
                }
            });
        });



    }
}
