package dev.tfkls.tempus.networking.client;

import dev.tfkls.tempus.networking.ServerEvents;
import dev.tfkls.tempus.networking.packet.client.NutritionS2CPacket;
import dev.tfkls.tempus.networking.packet.client.TemperatureS2CPacket;
import dev.tfkls.tempus.networking.packet.client.ThirstS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PacketProvider {
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ServerEvents.THIRST, ThirstS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ServerEvents.NUTRITION, NutritionS2CPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(ServerEvents.TEMPERATURE, TemperatureS2CPacket::receive);
    }
}
