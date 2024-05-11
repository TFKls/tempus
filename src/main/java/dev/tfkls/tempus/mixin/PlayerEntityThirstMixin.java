package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.ThirstManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityThirstMixin implements ThirstManager.MixinPlayerEntityAccessor {

    @Shadow public abstract PlayerAbilities getAbilities();

    @Unique
    protected ThirstManager thirstManager = new ThirstManager();

    @Unique
    public ThirstManager tempus$getThirstManager() {
        return thirstManager;
    }

    @Unique
    public ItemStack tempus$drink(World world, ItemStack stack) {
        ThirstManager.MixinItemAccessor item = (ThirstManager.MixinItemAccessor)stack.getItem();
        if (item.tempus$isDrink()) {
            if (!this.getAbilities().creativeMode) stack.decrement(1);
            thirstManager.drink(item);
        }
        return stack;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;update(Lnet/minecraft/entity/player/PlayerEntity;)V"))
    private void injectThirstUpdate(CallbackInfo ci) {
        thirstManager.update((PlayerEntity)(Object)this);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;writeNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    public void writeThirstNbt(NbtCompound nbt, CallbackInfo ci) {
        thirstManager.writeNbt(nbt);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;readNbt(Lnet/minecraft/nbt/NbtCompound;)V"))
    public void readThirstNbt(NbtCompound nbt, CallbackInfo ci) {
        thirstManager.readNbt(nbt);
    }
}
