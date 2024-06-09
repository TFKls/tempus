package dev.tfkls.tempus.effects;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.manager.ThirstManager;
import dev.tfkls.tempus.misc.CustomDamageSources;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class ThirstStatusEffect extends StatusEffect {
	private int tickCounter = 0;
	private final int threshold = Tempus.config.thirstStatusEffectThreshold;

	public ThirstStatusEffect() {
		super(StatusEffectCategory.HARMFUL, 0x98D982);
	}

	@Override
	public boolean canApplyUpdateEffect(int duration, int amplifier) {
		tickCounter++;
		if (tickCounter * amplifier >= threshold) {
			tickCounter = 0;
			return true;
		}
		return false;
	}

	@Override
	public void applyUpdateEffect(LivingEntity entity, int amplifier) {
		if (entity instanceof PlayerEntity player) {
			ThirstManager thirstManager = ((ThirstManager.MixinAccessor) player).tempus$getThirstManager();
			if (thirstManager.getThirst() > 0)
				thirstManager.add(-1);
			else
				player.damage(CustomDamageSources.of(player.getWorld(), CustomDamageSources.THIRST), 1.0f);
		}
	}
}
