package dev.tfkls.tempus.networking;

import dev.tfkls.tempus.core.ThirstManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.Identifier;

public class ServerEvents {

	public static final Identifier THIRST = new Identifier("tempus", "thirst");

	public static void registerServerEvents() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ThirstManager manager = ((ThirstManager.MixinAccessor) handler.getPlayer()).tempus$getThirstManager();

			server.execute(() -> {
				manager.syncThirst(handler.getPlayer());
			});
		});
	}
}
