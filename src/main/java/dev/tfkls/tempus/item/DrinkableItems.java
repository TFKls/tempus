package dev.tfkls.tempus.item;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.effects.CustomStatusEffects;
import dev.tfkls.tempus.misc.DrinkComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DrinkableItems {
	public static final DrinkableItem PURIFIED_WATER_BOTTLE = new DrinkableItem(new DrinkableItem.Settings().drink(new DrinkComponent(3, true)));
	public static final DrinkContainerItem WOODEN_MUG;
	public static final DrinkableItem MUG_OF_WATER;
	static {
		var v = DrinkContainerItem.createItemPair(new DrinkableItem.Settings().drink(new DrinkComponent(6, false)).maxCount(1));
		WOODEN_MUG = v.getLeft();
		MUG_OF_WATER = v.getRight();
	}
	public static final DrinkableItem WHEAT_EXTRACT = new DrinkableItem(new DrinkableItem.Settings().drink(new DrinkComponent(3, false)).drinkRemainder(WOODEN_MUG).maxCount(2));
	public static final DrinkableItem WHEAT_BEER = new DrinkableItem(
			new DrinkableItem.Settings().drink(new DrinkComponent(8, true)).addEffect(new StatusEffectInstance(CustomStatusEffects.TIPSY, 60*20, 0)).drinkRemainder(WOODEN_MUG).maxCount(2));

	public static void register() {
		Registry.register(Registries.ITEM, new Identifier("tempus", "purified_water_bottle"), PURIFIED_WATER_BOTTLE);
		Tempus.LOGGER.info("Registered Purified Water Bottle");
		Registry.register(Registries.ITEM, new Identifier("tempus", "wheat_extract"), WHEAT_EXTRACT);
		Tempus.LOGGER.info("Registered Wheat Extract");
		Registry.register(Registries.ITEM, new Identifier("tempus", "wheat_beer"), WHEAT_BEER);
		Tempus.LOGGER.info("Registered Wheat Beer");

		Registry.register(Registries.ITEM, new Identifier("tempus", "wooden_mug"), WOODEN_MUG);
		Registry.register(Registries.ITEM, new Identifier("tempus", "mug_of_water"), MUG_OF_WATER);
	}
}
