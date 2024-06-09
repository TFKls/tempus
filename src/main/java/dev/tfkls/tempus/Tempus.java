package dev.tfkls.tempus;

import dev.tfkls.tempus.command.NutritionCommand;
import dev.tfkls.tempus.command.SeasonCommand;
import dev.tfkls.tempus.command.TemperatureCommand;
import dev.tfkls.tempus.config.TempusConfig;
import dev.tfkls.tempus.core.CustomStatusEffects;
import dev.tfkls.tempus.core.SeasonManager;
import dev.tfkls.tempus.init.DrinkFermenterInitializer;
import dev.tfkls.tempus.item.DrinkableItems;
import dev.tfkls.tempus.item.Enchantments;
import dev.tfkls.tempus.item.TempusItems;
import dev.tfkls.tempus.networking.ServerEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tempus implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("tempus");
	public static TempusConfig config;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Fabric world!");

		LOGGER.info("Loading config...");
		config = TempusConfig.load();

		LOGGER.info("Registering commands...");
		NutritionCommand.register();
		SeasonCommand.register();
		TemperatureCommand.register();

		LOGGER.info("Registering enchantments...");
		Enchantments.register();

		LOGGER.info("Registering custom items...");
		DrinkFermenterInitializer.register();
		DrinkableItems.register();
		TempusItems.register();

		LOGGER.info("Registering status effects...");
		CustomStatusEffects.register();

		LOGGER.info("Saving config...");
		config.save();

		LOGGER.info("Registering server events...");
		ServerEvents.registerServerEvents();

		LOGGER.info("Registering gamerules...");
		GameRuleRegistry.register("doSeasonCycle", GameRules.Category.UPDATES,
				GameRuleFactory.createBooleanRule(true, (minecraftServer, value) -> {
					SeasonManager.getInstance().updateSeasonCycle(value.get());
				}));
	}
}
