package dev.tfkls.tempus.core;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.item.Enchantments;
import dev.tfkls.tempus.util.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
    private final int temperatureTickThreshold = 40;
    private final int environmentUpdateThreshold = 80;
    private final int radius = 5;
    protected PlayerStatusEffector effector = PlayerStatusEffector.of(
            (player, heat) -> {
                if (heat>=6) player.addStatusEffect(new StatusEffectInstance(CustomStatusEffects.THIRST, temperatureTickThreshold+10, (heat-6)/6, false, false, false));
                if (heat>=5) player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, temperatureTickThreshold+10, (heat-5)/5, false, false, false));
            },
            (player, cold) -> {
                if (cold>=6) player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, temperatureTickThreshold+10, (cold-6)/6, false, false, false));
                if (cold>=5) player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, temperatureTickThreshold+10, (cold-5)/5, false, false, false));
            }
    );

    static HashMap<Block,Integer> temperatures = new HashMap<>();
    static {
        temperatures.put(Blocks.LAVA,15);
        temperatures.put(Blocks.END_STONE,-10);
        temperatures.put(Blocks.NETHERRACK,10);
    }

    public float getTemperature() {
        return temperature;
    }

    public void setColdResistance(int coldResistance) {
        this.coldResistance = coldResistance;
    }

    public void update(PlayerEntity player) {
        insulation = Enchantments.INSULATION.getInsulationLevel(player)/4;
        temperatureTickTimer++;
        if (temperatureTickTimer >= temperatureTickThreshold) {
            temperatureTickTimer = 0;
            float oldTemperature = temperature;

            DeltaBuilder deltaBuilder = new DeltaBuilder(temperatureTickThreshold);

            // Player's internal heat regulation
            deltaBuilder.addUninsulatedSource(0, 0.01f);
            // Seasons come into play
            deltaBuilder.addSource(SeasonManager.getInstance().ambientTemperature(), 0.02f);
            // Time of day as well
            deltaBuilder.addSource((float) Math.sin((float) player.getWorld().getTimeOfDay() / 24000 * 2 * Math.PI), 0.01f);

            if (!player.isCreative() && !player.hasStatusEffect(StatusEffects.WATER_BREATHING)) {
                if (player.isSubmergedInWater()) {
                    deltaBuilder.addSource(-10, 0.05f);
                } else if (player.isTouchingWater()) {
                    deltaBuilder.addSource(-5, 0.05f);
                } else if (player.isWet()) {
                    deltaBuilder.addSource(-2, 0.01f);
                }
            }

            for (int x = (int)player.getX()-radius; x <= player.getX()+radius; x++) {
                for (int y = (int)player.getY()-radius; y <= player.getY()+radius; y++) {
                    for (int z = (int)player.getZ()-radius; z <= player.getZ()+radius; z++) {
                        float dist = (float)Math.sqrt(Math.pow(x-player.getX(),2)+Math.pow(y-player.getY(),2)+Math.pow(z-player.getZ(),2));
                        Block block = player.getWorld().getBlockState(new BlockPos(x,y,z)).getBlock();
                        if (temperatures.containsKey(block)) deltaBuilder.addSource(temperatures.get(block) * (float) Math.max(1f - dist / (radius * Math.sqrt(3)), 0), 0.03f);
                    }

                }
            }

            deltaBuilder.applyDelta();
            LOGGER.info("temperature {} => {} (Δ {})", oldTemperature, temperature, temperature-oldTemperature);
            float affectingTemperature = temperature;
            if(affectingTemperature < 0) {
                affectingTemperature /= ((float)coldResistance/2 + 1);
            }
            effector.runEffect(player, MathUtil.roundUp(affectingTemperature));
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
        temperature += heatConductivity / (1+insulation) * (sourceTemperature - temperature);
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
        public TemperatureManager tempus$getTemperatureManager();
    }
}
