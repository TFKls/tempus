package dev.tfkls.tempus.core;

import dev.tfkls.tempus.util.MathUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import static dev.tfkls.tempus.Tempus.LOGGER;

public class TemperatureManager {
    protected float temperature = 0;
    protected float insulation = 0;
    private int temperatureTickTimer;
    private final int temperatureTickThreshold = 40;
    protected PlayerStatusEffector effector = PlayerStatusEffector.of(
            (player, temp) -> {

            },
            (player, temp) -> {

            }
    );

    public float getTemperature() {
        return temperature;
    }
    public float getInsulation() {
        return insulation;
    }

    public void update(PlayerEntity player) {
        temperatureTickTimer++;
        if (temperatureTickTimer >= temperatureTickThreshold) {
            temperatureTickTimer = 0;
            float oldTemperature = temperature;

            DeltaBuilder deltaBuilder = new DeltaBuilder(temperatureTickThreshold);
            deltaBuilder.addUninsulatedSource(0, 0.01f);

            if (!player.isCreative() && !player.hasStatusEffect(StatusEffects.WATER_BREATHING)) {
                if (player.isSubmergedInWater()) {
                    deltaBuilder.addSource(-10, 0.05f);
                } else if (player.isTouchingWater()) {
                    deltaBuilder.addSource(-5, 0.05f);
                } else if (player.isWet()) {
                    deltaBuilder.addSource(-2, 0.01f);
                }
            }

            deltaBuilder.applyDelta();
            LOGGER.info("temperature {} => {} (Δ {})", oldTemperature, temperature, temperature-oldTemperature);
            effector.runEffect(player, MathUtil.roundUp(temperature));
        }
    }

    public class DeltaBuilder {
        int tickCycle;
        float temperatureDelta;

        public DeltaBuilder() {
            temperatureDelta = 0;
            tickCycle = 20;
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
            temperature += temperatureDelta*((float) tickCycle/20);
        }
    }

    public void applySingular(float sourceTemperature, float heatConductivity) {
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
        public TemperatureManager tempus$getTemperatureManager();
    }
}
