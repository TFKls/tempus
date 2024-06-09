package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.NutritionManager;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public abstract class HungerManagerNutritionMixin implements NutritionManager.MixinAccessor {
	@Unique
	protected NutritionManager nutritionManager = new NutritionManager();

	@Unique
	public NutritionManager tempus$getNutritionManager() {
		return nutritionManager;
	}

	@Inject(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V"))
	private void injectFoodNutritionType(Item item, ItemStack stack, CallbackInfo ci) {
		nutritionManager.add(item);
	}

	@Inject(method = "readNbt", at = @At(value = "TAIL"))
	private void readNutritionNbt(NbtCompound nbt, CallbackInfo ci) {
		nutritionManager.readNbt(nbt);
	}

	@Inject(method = "writeNbt", at = @At(value = "TAIL"))
	private void writeNutritionNbt(NbtCompound nbt, CallbackInfo ci) {
		nutritionManager.writeNbt(nbt);
	}

	@Inject(method = "update", at = @At(value = "TAIL"))
	private void runNutritionEffects(PlayerEntity player, CallbackInfo ci) {
		nutritionManager.update(player);
	}
}
