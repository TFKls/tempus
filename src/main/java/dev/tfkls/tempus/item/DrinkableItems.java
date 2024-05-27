package dev.tfkls.tempus.item;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.core.DrinkComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DrinkableItems {
    public static final DrinkableItem PURIFIED_WATER_BOTTLE = new DrinkableItem(new DrinkableItem.Settings().drink(new DrinkComponent(3,true)));
    public static final DrinkableItem WHEAT_EXTRACT = new DrinkableItem(new DrinkableItem.Settings().drink(new DrinkComponent(3, false)));
    public static final DrinkableItem WHEAT_BEER = new DrinkableItem(new DrinkableItem.Settings().drink(new DrinkComponent(8, true)));
    public static void register() {
        Registry.register(Registries.ITEM, new Identifier("tempus", "purified_water_bottle"), PURIFIED_WATER_BOTTLE);
        Tempus.LOGGER.info("Registered Purified Water Bottle");
        Registry.register(Registries.ITEM, new Identifier("tempus", "wheat_extract"), WHEAT_EXTRACT);
        Tempus.LOGGER.info("Registered Wheat Extract");
        Registry.register(Registries.ITEM, new Identifier("tempus", "wheat_beer"), WHEAT_BEER);
        Tempus.LOGGER.info("Registered Wheat Beer");
    }
}
