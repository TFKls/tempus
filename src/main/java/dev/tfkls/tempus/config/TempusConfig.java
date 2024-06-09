package dev.tfkls.tempus.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.tfkls.tempus.Tempus;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class TempusConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "tempus/config.json");

	//Nutrition constants
	public int nutritionTickTimerBound = 80;
	public int nutritionLevelMax = 10;
	public int nutritionStatusEffectDuration = 100;

	//Season constants
	public long seasonPeriod = 20 * 60 * 20 * 12; // 20min of realtime * 20 ticks * 12 months
	public float ambientTemperatureMultiplier = 3.0f;
	public float tickSpeedMultiplier = 0.8f;

	//Temperature constants
	public int temperatureEffectDuration = 50;
	public int temperatureTickThreshold = 40;
	public int environmentUpdateThreshold = 80;
	public int radius = 3;
	public int lowTemperatureHeight = 80;
	public int highTemperatureHeight = 30;
	public int inWaterSourceTemperature = -10;
	public int touchingWaterSourceTemperature = -5;
	public int wetSourceTemperature = -2;
	public int tickCycleMax = 20;
	public HashMap<Block, Integer> blockTemperatures = new HashMap<>();

	{
		blockTemperatures.put(Blocks.LAVA, 25);
		blockTemperatures.put(Blocks.END_STONE, -10);
		blockTemperatures.put(Blocks.NETHERRACK, 10);
		blockTemperatures.put(Blocks.ICE, -15);
		blockTemperatures.put(Blocks.SNOW, -10);
		blockTemperatures.put(Blocks.SNOW_BLOCK, -10);
		blockTemperatures.put(Blocks.WATER, -5);
		blockTemperatures.put(Blocks.FIRE, 15);
		blockTemperatures.put(Blocks.PACKED_ICE, -15);
		blockTemperatures.put(Blocks.FROSTED_ICE, -15);
	}

	//Thirst constants
	public int thirstLevelMax = 20;
	public int thirstTickThreshold = 80;
	public float unpurifiedDamageAmount = 4.0f;
	public int unpurifiedHungerStatusDuration = 200;
	public int unpurifiedHungerStatusAmplifier = 1;
	public float thirstDamageAmount = 2.0f;
	public int thirstStatusEffectThreshold = 100;

	public static TempusConfig load() {
		if (CONFIG_FILE.exists()) {
			try (FileReader reader = new FileReader(CONFIG_FILE)) {
				return GSON.fromJson(reader, TempusConfig.class);
			} catch (IOException e) {
				Tempus.LOGGER.info("Failed to load tempus config");
			}
		}
		return new TempusConfig();
	}

	public void save() {
		try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
			GSON.toJson(this, writer);
		} catch (IOException e) {
			Tempus.LOGGER.info("Failed to save tempus config");
		}
	}
}