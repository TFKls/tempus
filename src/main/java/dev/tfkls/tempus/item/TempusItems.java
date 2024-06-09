package dev.tfkls.tempus.item;

import dev.tfkls.tempus.Tempus;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TempusItems {
	public static final ThermometerItem THERMOMETER = new ThermometerItem(new Item.Settings().maxCount(1).maxDamage(64));

	public static void register() {
		Registry.register(Registries.ITEM, new Identifier("tempus", "thermometer"), THERMOMETER);
		Tempus.LOGGER.info("Registered thermometer");
	}
}
