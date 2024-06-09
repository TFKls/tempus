package dev.tfkls.tempus.effects;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.misc.CustomDamageSources;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class ColdStatusEffect extends StatusEffect {
	private final int effectDuration = Tempus.config.temperatureEffectDuration;

	public ColdStatusEffect() {
		super(StatusEffectCategory.HARMFUL, 0x98D977);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof PlayerEntity player) {
			if (amplifier >= 20)
				player.damage(CustomDamageSources.of(player.getWorld(), CustomDamageSources.EXTREME_COLD), 20.0f);
			if (amplifier >= 15)
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, effectDuration, (amplifier - 15) / 2, false, false, false));
			if (amplifier >= 10)
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, effectDuration, (amplifier - 10) / 5, false, false, false));
			if (amplifier >= 5)
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, effectDuration, (amplifier - 15) / 5, false, false, false));;
		}
	}
}
