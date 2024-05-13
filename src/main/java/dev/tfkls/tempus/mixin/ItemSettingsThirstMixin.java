package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.DrinkComponent;
import dev.tfkls.tempus.core.ThirstManager;
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
	public void tempus$setDrinkComponent(DrinkComponent drinkComponent) {
		this.drinkComponent = drinkComponent;
	}

	@Unique
	public DrinkComponent tempus$getDrinkComponent() {
		return drinkComponent;
	}
}
