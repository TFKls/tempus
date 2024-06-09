package dev.tfkls.tempus.effects;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.managers.TemperatureManager;
import dev.tfkls.tempus.misc.CustomDamageSources;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class HeatStatusEffect extends StatusEffect {
	private final int effectDuration = Tempus.config.temperatureEffectDuration;

	public HeatStatusEffect() {
		super(StatusEffectCategory.HARMFUL, 0x98D988);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof PlayerEntity player) {
			if (amplifier >= 20)
				player.damage(CustomDamageSources.of(player.getWorld(), CustomDamageSources.EXTREME_HEAT), 20.0f);
			if (amplifier >= 15)
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, (amplifier - 15) / 2, false, false, false));
			if (amplifier >= 10)
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, effectDuration, (amplifier - 10) / 5, false, false, false));
			if (amplifier >= 5)
				player.addStatusEffect(new StatusEffectInstance(CustomStatusEffects.THIRST, effectDuration, (amplifier - 5) / 5, false, false, false));
		}
	}
}
