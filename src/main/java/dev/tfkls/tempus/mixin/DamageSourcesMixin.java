package dev.tfkls.tempus.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageSources.class)
public class DamageSourcesMixin {
	@Unique
	private DamageSource thirst;

	@Unique
	public DamageSource thirst() {
		return this.thirst;
	}

	@Inject(method = "<init>", at = @At(value = "TAIL"))
	private void onInit(CallbackInfo ci) {
		this.thirst = ((DamageSources)(Object)this).create(DamageTypes.DRY_OUT);
	}
}
