package net.winxdinf.windextra.event;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.winxdinf.windextra.util.networkingServer;
import org.apache.logging.log4j.core.jmx.Server;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY = "key.category.windextra.keybinds";
    public static final String NIL_ITEM_PREV = "key.category.nil_previous";
    public static final String NIL_ITEM_NEXT = "key.category.nil_next";

    public static KeyBinding NilItemPreviousKey;
    public static KeyBinding NilItemNextKey;

    public static PacketByteBuf RequestNextPacket() {
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
        packet.writeString("next");

        return packet;
    }

    public static PacketByteBuf RequestPrevPacket() {
        PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
        packet.writeString("prev");

        return packet;
    }

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (NilItemPreviousKey.wasPressed()) {
                ClientPlayNetworking.send(networkingServer.NITEMSPACKET, RequestPrevPacket());
                //client.player.sendMessage(Text.of("prev key pressed"));
            } else if (NilItemNextKey.wasPressed()) {
                ClientPlayNetworking.send(networkingServer.NITEMSPACKET, RequestNextPacket());
                //client.player.sendMessage(Text.of("next key pressed"));
            }
        });
    }

    public static void register() {
        NilItemPreviousKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                NIL_ITEM_PREV,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                KEY_CATEGORY
        ));

        NilItemNextKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                NIL_ITEM_NEXT,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_X,
                KEY_CATEGORY
        ));


        registerKeyInputs();
    }

}
