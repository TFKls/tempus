package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.NutritionManager;
import dev.tfkls.tempus.core.NutritionType;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public abstract class CakeNutritionMixin {

	@Inject(method = "tryEat", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getHungerManager()Lnet/minecraft/entity/player/HungerManager;"))
	private static void injectCakeNutritionType(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<ActionResult> cir) {
		((NutritionManager.MixinAccessor) player.getHungerManager()).tempus$getNutritionManager().add(NutritionType.CARBOHYDRATE);
		((NutritionManager.MixinAccessor) player.getHungerManager()).tempus$getNutritionManager().syncNutrition(player);
	}
}
