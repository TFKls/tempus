package dev.tfkls.tempus.datagen;

import dev.tfkls.tempus.block.DrinkFermenter;
import dev.tfkls.tempus.init.DrinkFermenterInitializer;
import dev.tfkls.tempus.item.DrinkableItem;
import dev.tfkls.tempus.item.DrinkableItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.item.Items.*;

public class DrinkableItemsRecipeProvider extends FabricRecipeProvider {
    public DrinkableItemsRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        RecipeProvider.offerSmelting(exporter, List.of(Items.POTION), RecipeCategory.FOOD, DrinkableItems.PURIFIED_WATER_BOTTLE, 0.1f, 200, "tempus");
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, DrinkableItems.WHEAT_EXTRACT, 1).input(DrinkableItems.PURIFIED_WATER_BOTTLE).input(WHEAT, 2).input(RED_MUSHROOM).criterion(RecipeProvider.hasItem(DrinkableItems.PURIFIED_WATER_BOTTLE), RecipeProvider.conditionsFromItem(DrinkableItems.PURIFIED_WATER_BOTTLE)).offerTo(exporter, RecipeProvider.getItemPath(DrinkableItems.WHEAT_EXTRACT) + "_from_red_mushroom");
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, DrinkableItems.WHEAT_EXTRACT, 1).input(DrinkableItems.PURIFIED_WATER_BOTTLE).input(WHEAT, 2).input(BROWN_MUSHROOM).criterion(hasItem(DrinkableItems.PURIFIED_WATER_BOTTLE), RecipeProvider.conditionsFromItem(DrinkableItems.PURIFIED_WATER_BOTTLE)).offerTo(exporter, RecipeProvider.getItemPath(DrinkableItems.WHEAT_EXTRACT) + "_from_brown_mushroom");
        DrinkFermenterInitializer.offerRecipe(exporter, DrinkableItems.WHEAT_EXTRACT, Items.COAL, DrinkableItems.WHEAT_BEER);
    }
}
