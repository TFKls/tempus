package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.effect.TipsyStatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityTipsyEffectMixin implements TipsyStatusEffect.MixinAccessor {

    @Unique
    protected boolean isTipsy = false;

    @Unique
    public boolean tempus$isTipsy() {
        return this.isTipsy;
    }

    @Unique
    public void tempus$setTipsy(boolean isTipsy) {
        this.isTipsy = isTipsy;
    }
}