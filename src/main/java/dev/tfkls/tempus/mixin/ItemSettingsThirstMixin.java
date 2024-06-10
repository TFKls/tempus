package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.misc.DrinkComponent;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.Settings.class)
public abstract class ItemSettingsThirstMixin implements DrinkComponent.MutableMixinAccessor {
    @Unique
    @Nullable
    public DrinkComponent drinkComponent = null;

    @Unique
    public DrinkComponent.MutableMixinAccessor tempus$setDrinkComponent(DrinkComponent drinkComponent) {
        this.drinkComponent = drinkComponent;
        return this;
    }

    @Unique
    public DrinkComponent tempus$getDrinkComponent() {
        return drinkComponent;
    }
}
