package dev.tfkls.tempus.networking.packet.client;

import dev.tfkls.tempus.managers.NutritionManager;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public class NutritionS2CPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf buf, PacketSender sender) {
        NbtCompound nbt = buf.readNbt();

        client.execute(() -> {
            if (client.player == null) return;
            NutritionManager.MixinAccessor hungerManager = (NutritionManager.MixinAccessor) client.player.getHungerManager();
            if (hungerManager == null || nbt == null) return;
            hungerManager.tempus$getNutritionManager().readNbt(nbt);
        });
    }
}
