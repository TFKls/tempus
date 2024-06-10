package dev.tfkls.tempus.item;

import dev.tfkls.tempus.Tempus;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TempusItems {
	public static final ThermometerItem THERMOMETER = new ThermometerItem(new Item.Settings().maxCount(1).maxDamage(64));

	public static void register() {
		Registry.register(Registries.ITEM, new Identifier("tempus", "thermometer"), THERMOMETER);
		Tempus.LOGGER.info("Registered thermometer");

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
			content.add(THERMOMETER);
		});
	}
}
