package dev.tfkls.tempus.datagen;

import dev.tfkls.tempus.item.DrinkableItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.List;
import java.util.function.Consumer;

public class PurificationRecipeProvider extends FabricRecipeProvider {
    public PurificationRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        RecipeProvider.offerSmelting(exporter, List.of(Items.POTION), RecipeCategory.BREWING, DrinkableItems.PURIFIED_WATER_BOTTLE, 0.1f, 200, "tempus");
    }
}
