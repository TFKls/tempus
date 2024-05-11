package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.ThirstManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PotionItem.class)
public abstract class PotionItemThirstMixin implements ThirstManager.MixinItemAccessor {

    @Inject(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;incrementStat(Lnet/minecraft/stat/Stat;)V"))
    public void injectFinishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        ((ThirstManager.MixinPlayerEntityAccessor)user).tempus$getThirstManager().drink((ThirstManager.MixinItemAccessor)this);
    }
}
