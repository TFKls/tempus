package dev.tfkls.tempus.managers;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.misc.CustomDamageSources;
import dev.tfkls.tempus.misc.DrinkComponent;
import dev.tfkls.tempus.networking.ServerEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class ThirstManager {
	private int thirstLevel = Tempus.config.thirstLevelMax;
	private final int thirstLevelMax = Tempus.config.thirstLevelMax;
	private int thirstTickTimer;
	private final int thirstTickThreshold = Tempus.config.thirstTickThreshold;
	private boolean unpurifiedQueue = false;

	public void syncThirst(PlayerEntity pl) {
		if (pl instanceof ServerPlayerEntity player) {
			PacketByteBuf buffer = PacketByteBufs.create();
			NbtCompound nbt = new NbtCompound();
			writeNbt(nbt);
			buffer.writeNbt(nbt);
			ServerPlayNetworking.send(player, ServerEvents.THIRST, buffer);
		}
	}

	public int getThirst() {
		return thirstLevel;
	}

	public void add(int val) {
		this.thirstLevel = Math.max(Math.min(this.thirstLevel + val, thirstLevelMax), 0);
	}

	public void drink(DrinkComponent.MixinAccessor item) {
		drink(item.tempus$getDrinkComponent());
	}

	public void drink(DrinkComponent drinkComponent) {
		if (drinkComponent != null) {
			this.add(drinkComponent.getThirst());
			if (!drinkComponent.isPurified()) {
				this.unpurifiedRollEffects();
			}
		}
	}

	public boolean canDrink(PlayerEntity player) {
		return player.getAbilities().invulnerable || thirstLevel < thirstLevelMax;
	}

	public void unpurifiedRollEffects() {
		unpurifiedQueue = true;
	}


	public void update(PlayerEntity player) {
		if (unpurifiedQueue) {
			if (Math.random() > 0.5) {
				player.damage(CustomDamageSources.of(player.getWorld(), CustomDamageSources.THIRST), Tempus.config.unpurifiedDamageAmount);
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, Tempus.config.unpurifiedHungerStatusDuration, Tempus.config.unpurifiedHungerStatusAmplifier));
			}
			unpurifiedQueue = false;
		}
		thirstTickTimer++;
		if (thirstTickTimer >= thirstTickThreshold) {

			if (thirstLevel <= 0)
				player.damage(CustomDamageSources.of(player.getWorld(), CustomDamageSources.THIRST), Tempus.config.thirstDamageAmount);
			else {
				thirstLevel--;
				Tempus.LOGGER.info("Thirst level is {}, tick count: {}", getThirst(), thirstTickTimer);
			}
			syncThirst(player);

			thirstTickTimer = 0;
		}
	}

	public void readNbt(NbtCompound nbt) {
		if (nbt.contains("thirstLevel", NbtElement.NUMBER_TYPE)) {
			this.thirstLevel = nbt.getInt("thirstLevel");
			this.thirstTickTimer = nbt.getInt("thirstTickTimer");
		}
	}

	public void writeNbt(NbtCompound nbt) {
		nbt.putInt("thirstLevel", thirstLevel);
		nbt.putInt("thirstTickTimer", thirstTickTimer);
	}

	public interface MixinAccessor {
		ThirstManager tempus$getThirstManager();
	}
}
