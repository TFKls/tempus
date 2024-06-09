package dev.tfkls.tempus.effects;

import dev.tfkls.tempus.Tempus;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomStatusEffects {

	public static final StatusEffect THIRST = new ThirstStatusEffect();
	public static final StatusEffect COLD_RESISTANCE = new ColdResistanceStatusEffect();
	public static final StatusEffect HEAT = new HeatStatusEffect();
	public static final StatusEffect COLD = new ColdStatusEffect();

	public static void register() {

		Registry.register(Registries.STATUS_EFFECT, new Identifier("tempus", "thirst"), THIRST);
		Tempus.LOGGER.info("Registered Thirst Status Effect");

		Registry.register(Registries.STATUS_EFFECT, new Identifier("tempus", "cold_resistance"), COLD_RESISTANCE);
		Tempus.LOGGER.info("Registered Cold Resistance Status Effect");

		Registry.register(Registries.STATUS_EFFECT, new Identifier("tempus", "heat"), HEAT);
		Tempus.LOGGER.info("Registered Heat Status Effect");

		Registry.register(Registries.STATUS_EFFECT, new Identifier("tempus", "cold"), COLD);
		Tempus.LOGGER.info("Registered Cold Status Effect");
	}
}