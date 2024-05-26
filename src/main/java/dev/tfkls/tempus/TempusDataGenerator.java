package dev.tfkls.tempus;

import dev.tfkls.tempus.datagen.PurificationRecipeProvider;
import dev.tfkls.tempus.datagen.VanillaNutritionTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class TempusDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(VanillaNutritionTagProvider::new);
		pack.addProvider(PurificationRecipeProvider::new);
	}
}
