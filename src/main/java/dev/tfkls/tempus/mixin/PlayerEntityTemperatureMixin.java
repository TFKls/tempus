package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.manager.TemperatureManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityTemperatureMixin implements TemperatureManager.MixinAccessor {
	@Unique
	protected TemperatureManager temperatureManager = new TemperatureManager();

	@Override
	public TemperatureManager tempus$getTemperatureManager() {
		return temperatureManager;
	}

	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;update(Lnet/minecraft/entity/player/PlayerEntity;)V"))
	public void tickTemperature(CallbackInfo ci) {
		temperatureManager.update((PlayerEntity) (Object) this); // sadly need to double cast to pass this
	}

	@Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"))
	public void damageUpdateTemperature(DamageSource source, float amount, CallbackInfo ci) {
		switch (source.getType().msgId()) {
			case "inFire" -> temperatureManager.applySingular(20, 0.05f);
			case "onFire" -> temperatureManager.applySingular(20, 0.02f);
			case "lava" -> temperatureManager.applySingular(30, 0.1f);
			case "hotFloor" -> temperatureManager.applySingular(15, 0.2f);
			case "lightningBolt" -> temperatureManager.applySingular(50, 0.2f);
			default -> {
			}
		}
	}

	@Inject(method = "writeCustomDataToNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;writeNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	public void writeTemperatureNbt(NbtCompound nbt, CallbackInfo ci) {
		temperatureManager.writeNbt(nbt);
	}

	@Inject(method = "readCustomDataFromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
	public void readTemperatureNbt(NbtCompound nbt, CallbackInfo ci) {
		temperatureManager.readNbt(nbt);
	}
}
