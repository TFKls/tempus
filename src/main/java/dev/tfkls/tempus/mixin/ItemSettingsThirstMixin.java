package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.DrinkComponent;
import dev.tfkls.tempus.core.ThirstManager;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Item.Settings.class)
public abstract class ItemSettingsThirstMixin implements ThirstManager.MixinItemSettingsAccessor {

	@Unique
	public DrinkComponent drinkComponent = null;

	@Unique
	public Item.Settings tempus$setDrinkComponent(DrinkComponent drinkComponent) {
		this.drinkComponent = drinkComponent;
		return (Item.Settings)(Object)this;
	}

	@Unique
	public DrinkComponent tempus$getDrinkComponent() {
		return drinkComponent;
	}
}
