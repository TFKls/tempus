package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.TemperatureManager;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityTemperatureMixin {
    @Unique
    protected TemperatureManager temperatureManager = new TemperatureManager();
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;update(Lnet/minecraft/entity/player/PlayerEntity;)V"))
    public void tickTemperature(CallbackInfo ci) {
        temperatureManager.update((PlayerEntity)(Object) this); // sadly need to double cast to pass this
    }

    @Inject(method = "applyDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"))
    public void damageUpdateTemperature(DamageSource source, float amount, CallbackInfo ci) {
        switch (source.getType().msgId()) {
            case "inFire":
                temperatureManager.applyModifier(20, 0.4f);
                break;
            case "onFire":
                temperatureManager.applyModifier(20, 0.1f);
                break;
            case "lava":
                temperatureManager.applyModifier(30, 0.8f);
                break;
            case "hotFloor":
                temperatureManager.applyModifier(20, 0.2f);
                break;
            case "lightningBolt":
                temperatureManager.applyModifier(50, 0.1f);
                break;
        }
    }
}
