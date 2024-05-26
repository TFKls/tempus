package dev.tfkls.tempus.item;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.core.DrinkComponent;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DrinkableItems {
    public static final DrinkableItem PURIFIED_WATER_BOTTLE = new DrinkableItem(new DrinkableItem.Settings().drink(new DrinkComponent(3,true)));

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier("tempus", "purified_water_bottle"), PURIFIED_WATER_BOTTLE);
        Tempus.LOGGER.info("Registered Purified Water Bottle");
    }
}
