package dev.tfkls.tempus.networking.packet.client;

import dev.tfkls.tempus.core.ThirstManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class ThirstS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf buf, PacketSender sender) {
        ThirstManager.MixinAccessor player = (ThirstManager.MixinAccessor) client.player;
        if (player == null) return;
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("thirstLevel", buf.readInt());
        nbt.putInt("thirstTickTimer", buf.readInt());
        player.tempus$getThirstManager().readNbt(nbt);
    }
}
