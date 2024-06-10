package dev.tfkls.tempus.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public class TipsyStatusEffect extends StatusEffect {
	private boolean isFirst = true;

	public TipsyStatusEffect() {
		super(StatusEffectCategory.HARMFUL, 0xffffff);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof PlayerEntity player) {
			if (!isFirst)
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * amplifier));
			isFirst = false;
		}
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		isFirst = true;
	}
}