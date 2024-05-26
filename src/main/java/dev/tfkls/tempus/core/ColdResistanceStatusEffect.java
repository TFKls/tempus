package dev.tfkls.tempus.core;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class ColdResistanceStatusEffect extends StatusEffect {

	public ColdResistanceStatusEffect() {
		super(StatusEffectCategory.BENEFICIAL, 0x98D983);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if(entity instanceof PlayerEntity player) {
			((TemperatureManager.MixinAccessor)player).tempus$getTemperatureManager().setColdResistance(amplifier);
		}
	}
}
