package dev.tfkls.tempus.core;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class ColdResistanceStatusEffect extends StatusEffect {

	public ColdResistanceStatusEffect() {
		super(StatusEffectCategory.BENEFICIAL, 0x98D983);
	}

	@Override
	public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if(entity instanceof PlayerEntity player) {
			((TemperatureManager.MixinAccessor)player).tempus$getTemperatureManager().setColdResistance(amplifier);
		}
	}

	@Override
	public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
		if(entity instanceof PlayerEntity player) {
			((TemperatureManager.MixinAccessor)player).tempus$getTemperatureManager().setColdResistance(0);
		}
	}
}
