package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.manager.ThirstManager;
import dev.tfkls.tempus.misc.DrinkComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class PotionItemThirstMixin {

    @Inject(method = "finishUsing",
            at = @At(value = "INVOKE",
					 target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V"))
    public void injectFinishUsing(
            ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        ((ThirstManager.MixinAccessor) user).tempus$getThirstManager().drink((DrinkComponent.MixinAccessor) this);
        if (user instanceof PlayerEntity player)
            ((ThirstManager.MixinAccessor) user).tempus$getThirstManager().syncThirst(player);
        if (!world.isClient() && stack.getName().getString().equals("Water Bottle")) {
            // Unpurified edge-case
            ((ThirstManager.MixinAccessor) user).tempus$getThirstManager().unpurifiedRollEffects();
        }
    }
}
