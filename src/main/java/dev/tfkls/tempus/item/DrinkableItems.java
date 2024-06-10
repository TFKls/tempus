package dev.tfkls.tempus.item;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.effects.CustomStatusEffects;
import dev.tfkls.tempus.misc.DrinkComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
	public static final DrinkableItem WHEAT_EXTRACT = new DrinkableItem(new DrinkableItem.Settings().drink(new DrinkComponent(3, false)).drinkRemainder(WOODEN_MUG).maxCount(1));
	public static final DrinkableItem WHEAT_BEER = new DrinkableItem(
			new DrinkableItem.Settings().drink(new DrinkComponent(10, true)).addEffect(new StatusEffectInstance(CustomStatusEffects.TIPSY, 60*20, 0)).drinkRemainder(WOODEN_MUG).maxCount(1));
	public static final DrinkableItem WINE_BASE = new DrinkableItem(
			new DrinkableItem.Settings().drink(new DrinkComponent(2, false)).maxCount(2)
	);
	public static final DrinkableItem WINE_BOTTLE = new DrinkableItem(
			new DrinkableItem.Settings().drink(new DrinkComponent(8, false)).maxCount(2).addEffect(new StatusEffectInstance(CustomStatusEffects.TIPSY, 60*20, 1)));
	public static final DrinkContainerItem COPPER_CAN;
	public static final DrinkableItem CAN_OF_WATER;
	static {
        var v = DrinkContainerItem.createItemPair(new DrinkableItem.Settings().drink(new DrinkComponent(4, false)).maxCount(4));
		COPPER_CAN = v.getLeft();
		CAN_OF_WATER = v.getRight();
	}

	public static final DrinkableItem ENERGETIC_EXTRACT = new DrinkableItem(
			new DrinkableItem.Settings().drink(new DrinkComponent(3, true)).addEffect(new StatusEffectInstance(StatusEffects.POISON, 5*20, 0)).maxCount(4).drinkRemainder(COPPER_CAN)
	);
    public static final DrinkableItem ENERGY_MIXTURE = new DrinkableItem(new DrinkableItem.Settings()
            .drink(new DrinkComponent(6, true))
            .addEffect(new StatusEffectInstance(StatusEffects.SPEED, 60*20, 0, false, false))
            .addEffect(new StatusEffectInstance(StatusEffects.HASTE, 60*20, 0, false, false))
            .addEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 60*20, 0, false, false))
			.addEffect(new StatusEffectInstance(CustomStatusEffects.TIPSY, 120*20, 0, true, true))
            .maxCount(4).drinkRemainder(COPPER_CAN));
	public static final DrinkableItem BEPIS_CAN = new DrinkableItem(new DrinkableItem.Settings()
			.drink(new DrinkComponent(8, true))
			.maxCount(4).drinkRemainder(COPPER_CAN));

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
