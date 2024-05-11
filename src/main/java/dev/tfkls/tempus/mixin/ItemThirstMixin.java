package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.DrinkComponent;
import dev.tfkls.tempus.core.ThirstManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemThirstMixin implements ThirstManager.MixinItemAccessor {

    @Unique
    protected DrinkComponent drinkComponent = null;

    @Unique
    public boolean tempus$isDrink() {
        return this.tempus$getDrinkComponent()!=null;
    }

    @Unique
    public DrinkComponent tempus$getDrinkComponent() {
        return drinkComponent;
    }

    @Inject(method = "finishUsing", at = @At(value = "RETURN"), cancellable = true)
    public void injectFinishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (this.tempus$isDrink() && user instanceof PlayerEntity) {
            cir.setReturnValue(((ThirstManager.MixinPlayerEntityAccessor)user).tempus$drink(world, stack));
        }
    }
}
