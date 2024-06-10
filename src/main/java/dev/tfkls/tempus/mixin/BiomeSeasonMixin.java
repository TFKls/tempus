package dev.tfkls.tempus.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import dev.tfkls.tempus.manager.SeasonManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeSeasonMixin {
    /// The method we override is @Deprecated, but still actively used within the class (???)
    /// This simplifies logic by a lot, since we only change one method instead of three;
    @Inject(method = "getTemperature(Lnet/minecraft/util/math/BlockPos;)F",
            at = @At(value = "RETURN", ordinal = 0),
            cancellable = true)
    public void modifyCachedTemperature(
            BlockPos blockPos, CallbackInfoReturnable<Float> cir, @Local(name = "f") float temp) {
        cir.setReturnValue(temp + SeasonManager.getInstance().biomeTemperatureDelta());
    }

    @Inject(method = "getTemperature(Lnet/minecraft/util/math/BlockPos;)F",
            at = @At(value = "RETURN", ordinal = 1),
            cancellable = true)
    public void modifyFreshTemperature(
            BlockPos blockPos, CallbackInfoReturnable<Float> cir, @Local(name = "g") float temp) {
        cir.setReturnValue(temp + SeasonManager.getInstance().biomeTemperatureDelta());
    }
}
