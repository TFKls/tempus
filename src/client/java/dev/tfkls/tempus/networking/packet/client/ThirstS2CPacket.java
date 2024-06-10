package dev.tfkls.tempus.networking.packet.client;

import dev.tfkls.tempus.manager.ThirstManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class ThirstS2CPacket {

    public static void receive(
            MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf buf, PacketSender sender) {
        NbtCompound nbt = buf.readNbt();

        client.execute(() -> {
            ThirstManager.MixinAccessor player = (ThirstManager.MixinAccessor) client.player;
            if (player == null || nbt == null) return;
            player.tempus$getThirstManager().readNbt(nbt);
        });
    }
}
