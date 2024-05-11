package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.DrinkComponent;
import dev.tfkls.tempus.core.ThirstManager;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
}
