package dev.tfkls.tempus.manager;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.effects.CustomStatusEffects;
import dev.tfkls.tempus.effects.PlayerStatusEffector;
import dev.tfkls.tempus.item.Enchantments;
import dev.tfkls.tempus.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

import static dev.tfkls.tempus.Tempus.LOGGER;

public class TemperatureManager {
	protected float temperature = 0;
	protected float insulation = 0;
	private int coldResistance = 0;
	private int temperatureTickTimer;
	private float cachedDelta = 0;
	private final int temperatureTickThreshold = Tempus.config.temperatureTickThreshold;
	private final int environmentUpdateThreshold = Tempus.config.environmentUpdateThreshold;
	private final int radius = Tempus.config.radius;
	private final int temperatureEffectDuration = Tempus.config.temperatureEffectDuration;
	protected PlayerStatusEffector effector = PlayerStatusEffector.of(
			(player, heat) -> player.addStatusEffect(new StatusEffectInstance(CustomStatusEffects.HEAT, temperatureEffectDuration, heat, false, false, false)),
			(player, cold) -> player.addStatusEffect(new StatusEffectInstance(CustomStatusEffects.COLD, temperatureEffectDuration, cold, false, false, false))
	);

	static HashMap<Block, Integer> temperatures = Tempus.config.blockTemperatures;

	private final int lowTemperatureHeight = Tempus.config.lowTemperatureHeight;
	private final int highTemperatureHeight = Tempus.config.highTemperatureHeight;
	private final int inWaterSourceTemperature = Tempus.config.inWaterSourceTemperature;
	private final int touchingWaterSourceTemperature = Tempus.config.touchingWaterSourceTemperature;
	private final int wetSourceTemperature = Tempus.config.wetSourceTemperature;

	public float getTemperature() {
		return temperature;
	}

	public void setColdResistance(int coldResistance) {
		this.coldResistance = coldResistance;
	}

	public void update(PlayerEntity player) {
		insulation = Enchantments.INSULATION.getInsulationLevel(player) / 4;
		temperatureTickTimer++;
		if (temperatureTickTimer % environmentUpdateThreshold == 0) {
			DeltaBuilder deltaBuilder = new DeltaBuilder(environmentUpdateThreshold);

			for (int x = (int) player.getX() - radius; x <= player.getX() + radius; x++) {
				for (int y = (int) player.getY() - radius; y <= player.getY() + radius; y++) {
					for (int z = (int) player.getZ() - radius; z <= player.getZ() + radius; z++) {
						float dist = (float) Math.sqrt(Math.pow(x - player.getX(), 2) + Math.pow(y - player.getY(), 2) + Math.pow(z - player.getZ(), 2));
						Block block = player.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
						if (temperatures.containsKey(block))
							deltaBuilder.addSource(temperatures.get(block), (float) Math.pow(1f - (dist / (radius * Math.sqrt(3))), 2) * 0.006f);
					}
				}
			}

			cachedDelta = deltaBuilder.getDelta();
		}
		if (temperatureTickTimer % temperatureTickThreshold == 0) {
			float oldTemperature = temperature;

			DeltaBuilder deltaBuilder = new DeltaBuilder(temperatureTickThreshold);

			// Player's internal heat regulation
			deltaBuilder.addUninsulatedSource(0, 0.01f);
			// Seasons come into play
			deltaBuilder.addSource(SeasonManager.getInstance().ambientTemperature(), 0.02f);
			// Time of day as well
			deltaBuilder.addSource((float) Math.sin((float) player.getWorld().getTimeOfDay() / 24000 * 2 * Math.PI), 0.01f);
			// Height in the world
			if (player.getY() >= lowTemperatureHeight)
				deltaBuilder.addSource(-2f - (float) (player.getY() - lowTemperatureHeight) / 10, 0.01f);
			if (player.getY() <= highTemperatureHeight)
				deltaBuilder.addSource(2f + (float) (highTemperatureHeight - player.getY()) / 10, 0.01f);
			// And the blocks around
			deltaBuilder.addDelta(cachedDelta);

			if (!player.isCreative() && !player.hasStatusEffect(StatusEffects.WATER_BREATHING)) {
				if (player.isSubmergedInWater()) {
					deltaBuilder.addSource(inWaterSourceTemperature, 0.05f);
				} else if (player.isTouchingWater()) {
					deltaBuilder.addSource(touchingWaterSourceTemperature, 0.05f);
				} else if (player.isWet()) {
					deltaBuilder.addSource(wetSourceTemperature, 0.01f);
				}
			}

			deltaBuilder.applyDelta();
			LOGGER.info("temperature {} => {} (Δ {})", oldTemperature, temperature, temperature - oldTemperature);
			float affectingTemperature = temperature;
			if (affectingTemperature < 0) {
				affectingTemperature /= ((float) coldResistance / 2 + 1);
			}
			effector.runEffect(player, MathUtil.roundUp(affectingTemperature));
		}
		if (temperatureTickTimer >= environmentUpdateThreshold) temperatureTickTimer = 1;

	}

	public class DeltaBuilder {
		int tickCycle;
		final int tickCycleMax = Tempus.config.tickCycleMax;
		float temperatureDelta;

		public DeltaBuilder() {
			temperatureDelta = 0;
			tickCycle = tickCycleMax;
		}

		public DeltaBuilder(int tickCycle) {
			temperatureDelta = 0;
			this.tickCycle = tickCycle;
		}

		public DeltaBuilder addSource(float sourceTemperature, float heatConductivity) {
			temperatureDelta += heatConductivity / (1 + insulation) * (sourceTemperature - temperature);
			return this;
		}

		public DeltaBuilder addUninsulatedSource(float sourceTemperature, float heatConductivity) {
			temperatureDelta += heatConductivity * (sourceTemperature - temperature);
			return this;
		}

		public void applyDelta() {
			temperature += temperatureDelta * ((float) tickCycle / tickCycleMax);
		}

		public float getDelta() {
			return temperatureDelta;
		}

		public void addDelta(float delta) {
			temperatureDelta += delta;
		}
	}

	public void applySingular(float sourceTemperature, float heatConductivity) {
		temperature += heatConductivity / (1 + insulation) * (sourceTemperature - temperature);
	}

	public void applyUninsulatedSingular(float sourceTemperature, float heatConductivity) {
		temperature += heatConductivity * (sourceTemperature - temperature);
	}

	public void readNbt(NbtCompound nbt) {
		if (nbt.contains("temperatureValue", NbtElement.NUMBER_TYPE)) {
			temperature = nbt.getFloat("temperatureValue");
			insulation = nbt.getFloat("temperatureInsulation");
			temperatureTickTimer = nbt.getInt("temperatureTickTimer");
		}
	}

	public void writeNbt(NbtCompound nbt) {
		nbt.putFloat("temperatureValue", temperature);
		nbt.putFloat("temperatureInsulation", insulation);
		nbt.putInt("temperatureTickTimer", temperatureTickTimer);
	}

	public interface MixinAccessor {
		TemperatureManager tempus$getTemperatureManager();
	}
}