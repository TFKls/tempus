package dev.tfkls.tempus.core;

import dev.tfkls.tempus.util.MathUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

import static dev.tfkls.tempus.Tempus.LOGGER;

public class TemperatureManager {
    private float temperature = 0;
    private float insulation = 0;
    private int temperatureTickTimer;
    private final int temperatureTickThreshold = 40;
    PlayerStatusEffector effector = PlayerStatusEffector.of(
            (player, temp) -> {

            },
            (player, temp) -> {

            }
    );
    public void update(PlayerEntity player) {
        temperatureTickTimer++;
        if (temperatureTickTimer >= temperatureTickThreshold) {
            temperatureTickTimer = 0;
            float oldTemperature = temperature;
            insulation = computeInsulation(player);

            // Internal heat stabilization
            applyInternalModifier(0, 1+insulation);

            // Direct environmental effects
            if (!player.isCreative()) {
                float modifier = player.hasStatusEffect(StatusEffects.WATER_BREATHING) && (temperature >= 5) ? 0 : 1;
                if (player.isTouchingWater()) {
                    applyInternalModifier(-7, 1f*modifier);
                } else if (player.isWet()) {
                    applyInternalModifier(-5, 0.5f*modifier);
                }
                if (player.isSubmergedInWater()) {
                    applyInternalModifier(-10, 2f*modifier);
                }
            }

            LOGGER.info("temperature {} => {} (Δ {})", oldTemperature, temperature, temperature-oldTemperature);
            effector.runEffect(player, MathUtil.roundUp(temperature));
        }
    }

    private static float computeInsulation(PlayerEntity player) {
        return 0;
    }
    static float computeTemperatureDelta(float objectTemperature, float externalTemperature, float externalHeatConductivity) {
        return externalHeatConductivity * (externalTemperature - objectTemperature);
    }
    private void applyInternalModifier(float temperature, float heatConductivity) {
        applyModifier(temperature, heatConductivity, temperatureTickThreshold);
    }
    public void applyModifier(float temperature, float heatConductivity) {
        this.temperature += computeTemperatureDelta(this.temperature, temperature, ((heatConductivity)/(1+insulation)));
    }
    public void applyModifier(float temperature, float heatConductivity, int tickCycle) {
        this.temperature += computeTemperatureDelta(this.temperature, temperature, ((heatConductivity)/(1+insulation)) * ((float) tickCycle /20));
    }

    public float getTemperature() {
        return temperature;
    }
    public float getInsulation() {
        return insulation;
    }
}
