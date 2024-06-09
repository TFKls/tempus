package dev.tfkls.tempus.networking.client;

import dev.tfkls.tempus.networking.ServerEvents;
import dev.tfkls.tempus.networking.packet.client.ThirstS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class PacketProvider {
    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(ServerEvents.THIRST, ThirstS2CPacket::receive);
    }
}
