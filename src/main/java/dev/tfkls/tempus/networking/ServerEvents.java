package dev.tfkls.tempus.networking;

import dev.tfkls.tempus.manager.NutritionManager;
import dev.tfkls.tempus.manager.TemperatureManager;
import dev.tfkls.tempus.manager.ThirstManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.util.Identifier;

public class ServerEvents {

	public static final Identifier THIRST = new Identifier("tempus", "thirst");
	public static final Identifier NUTRITION = new Identifier("tempus", "nutrition");
	public static final Identifier TEMPERATURE = new Identifier("tempus", "temperature");

	public static void registerServerEvents() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ThirstManager thirstManager = ((ThirstManager.MixinAccessor) handler.getPlayer()).tempus$getThirstManager();
			NutritionManager nutritionManager = ((NutritionManager.MixinAccessor) handler.getPlayer().getHungerManager()).tempus$getNutritionManager();
			TemperatureManager temperatureManager = ((TemperatureManager.MixinAccessor) handler.getPlayer()).tempus$getTemperatureManager();

			server.execute(() -> {
				thirstManager.syncThirst(handler.getPlayer());
				nutritionManager.syncNutrition(handler.getPlayer());
				temperatureManager.syncTemperature(handler.getPlayer());
			});
		});
	}
}
