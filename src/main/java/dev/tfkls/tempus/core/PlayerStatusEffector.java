package dev.tfkls.tempus.core;

import net.minecraft.entity.player.PlayerEntity;

public interface PlayerStatusEffector {
	void runEffect(PlayerEntity player, int grade);

	static PlayerStatusEffector of(PlayerStatusEffector positiveEffector, PlayerStatusEffector negativeEffector) {
		return (player, grade) -> {
			if (grade > 0) positiveEffector.runEffect(player, grade);
			if (grade < 0) negativeEffector.runEffect(player, -grade);
		};
	}

	PlayerStatusEffector NONE = (player, grade) -> {
	};
}
