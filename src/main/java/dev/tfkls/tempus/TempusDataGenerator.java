package dev.tfkls.tempus;

import dev.tfkls.tempus.datagen.DrinkableItemsRecipeProvider;
import dev.tfkls.tempus.datagen.VanillaNutritionTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.server.tag.vanilla.VanillaDamageTypeTagProvider;

public class TempusDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(VanillaNutritionTagProvider::new);
		pack.addProvider(DrinkableItemsRecipeProvider::new);
	}
}
