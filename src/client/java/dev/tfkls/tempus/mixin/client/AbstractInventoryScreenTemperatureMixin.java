package dev.tfkls.tempus.mixin.client;

import dev.tfkls.tempus.effect.CustomStatusEffects;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Collection;
import java.util.List;

@Mixin(AbstractInventoryScreen.class)
public class AbstractInventoryScreenTemperatureMixin {
    @Unique
    private final List<StatusEffect> hiddenEffects = List.of(CustomStatusEffects.THIRST);

    @ModifyVariable(method = "drawStatusEffects", at = @At("STORE"), ordinal = 0)
    private Collection<StatusEffectInstance> modifyDrawnStatusEffects(Collection<StatusEffectInstance> collection) {
        collection.removeIf((elem) -> hiddenEffects.contains(elem.getEffectType()));
        return collection;
    }
}
