package dev.tfkls.tempus.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class TipsyStatusEffect extends StatusEffect {

	public TipsyStatusEffect() {
		super(StatusEffectCategory.HARMFUL, 0xffffff);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof PlayerEntity player) {
			if (player.hasStatusEffect(CustomStatusEffects.TIPSY))
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * amplifier));
		}
	}
}