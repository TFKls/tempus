package dev.tfkls.tempus.effect;

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
			if (((MixinAccessor) player).tempus$isTipsy())
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 20 * amplifier));
			((MixinAccessor) player).tempus$setTipsy(true);
		}
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if (entity instanceof PlayerEntity player)
			((MixinAccessor) player).tempus$setTipsy(false);
	}

	public interface MixinAccessor {
		boolean tempus$isTipsy();

		void tempus$setTipsy(boolean value);
	}
}