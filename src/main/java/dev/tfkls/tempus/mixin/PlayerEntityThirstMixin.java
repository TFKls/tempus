package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.ThirstManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityThirstMixin implements ThirstManager.MixinAccessor {

    @Unique
    protected ThirstManager thirstManager = new ThirstManager();

    @Unique
    public ThirstManager tempus$getThirstManager() {
        return thirstManager;
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
