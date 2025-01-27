package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.manager.ThirstManager;
import dev.tfkls.tempus.misc.DrinkComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemThirstMixin implements DrinkComponent.MixinAccessor {

    @Unique
    protected DrinkComponent drinkComponent = null;

    @Shadow
    public abstract boolean isFood();

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    public void onInit(Item.Settings settings, CallbackInfo ci) {
        this.drinkComponent = ((DrinkComponent.MixinAccessor) settings).tempus$getDrinkComponent();
    }

    @Unique
    public DrinkComponent tempus$getDrinkComponent() {
        return drinkComponent;
    }

    @Inject(method = "finishUsing", at = @At(value = "HEAD"))
    public void injectFinishUsing(
            ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (this.tempus$isDrinkable() && user instanceof PlayerEntity player) {
            ((ThirstManager.MixinAccessor) user).tempus$getThirstManager().drink(tempus$getDrinkComponent());
            ((ThirstManager.MixinAccessor) user).tempus$getThirstManager().syncThirst(player);
            if (!this.isFood() && !player.getAbilities().creativeMode) stack.decrement(1);
        }
    }
}
