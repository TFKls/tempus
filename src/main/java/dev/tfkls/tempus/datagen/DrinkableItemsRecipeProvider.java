package dev.tfkls.tempus.datagen;

import dev.tfkls.tempus.item.DrinkableItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.item.Items.*;

public class DrinkableItemsRecipeProvider extends FabricRecipeProvider {
    public DrinkableItemsRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        RecipeProvider.offerSmelting(
                exporter,
                List.of(Items.POTION),
                RecipeCategory.FOOD,
                DrinkableItems.PURIFIED_WATER_BOTTLE,
                0.1f,
                200,
                "tempus");
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, DrinkableItems.WOODEN_MUG, 1)
                .pattern("sw")
                .pattern("sw")
                .input('s', STICK)
                .input('w', ItemTags.PLANKS)
                .criterion(FabricRecipeProvider.hasItem(STICK), FabricRecipeProvider.conditionsFromItem(STICK))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, DrinkableItems.COPPER_CAN, 1)
                .pattern("c")
                .pattern("c")
                .input('c', COPPER_INGOT)
                .criterion(
                        FabricRecipeProvider.hasItem(COPPER_INGOT),
                        FabricRecipeProvider.conditionsFromItem(COPPER_INGOT))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, DrinkableItems.WHEAT_EXTRACT, 1)
                .input(DrinkableItems.MUG_OF_WATER)
                .input(WHEAT, 2)
                .input(RED_MUSHROOM)
                .criterion(
                        RecipeProvider.hasItem(DrinkableItems.MUG_OF_WATER),
                        RecipeProvider.conditionsFromItem(DrinkableItems.MUG_OF_WATER))
                .offerTo(exporter, RecipeProvider.getItemPath(DrinkableItems.WHEAT_EXTRACT) + "_from_red_mushroom");
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, DrinkableItems.WHEAT_EXTRACT, 1)
                .input(DrinkableItems.MUG_OF_WATER)
                .input(WHEAT, 2)
                .input(BROWN_MUSHROOM)
                .criterion(
                        RecipeProvider.hasItem(DrinkableItems.MUG_OF_WATER),
                        RecipeProvider.conditionsFromItem(DrinkableItems.MUG_OF_WATER))
                .offerTo(exporter, RecipeProvider.getItemPath(DrinkableItems.WHEAT_EXTRACT) + "_from_brown_mushroom");
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, DrinkableItems.WINE_BASE, 1)
                .input(DrinkableItems.PURIFIED_WATER_BOTTLE)
                .input(SWEET_BERRIES, 8)
                .criterion(
                        RecipeProvider.hasItem(DrinkableItems.PURIFIED_WATER_BOTTLE),
                        RecipeProvider.conditionsFromItem(DrinkableItems.PURIFIED_WATER_BOTTLE))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, DrinkableItems.BEPIS_CAN, 1)
                .input(DrinkableItems.COPPER_CAN)
                .input(SUGAR)
                .input(INK_SAC)
                .input(CHORUS_FRUIT)
                .criterion(
                        RecipeProvider.hasItem(DrinkableItems.COPPER_CAN),
                        RecipeProvider.conditionsFromItem(DrinkableItems.COPPER_CAN))
                .offerTo(exporter);
        ShapelessRecipeJsonBuilder.create(RecipeCategory.FOOD, DrinkableItems.ENERGETIC_EXTRACT, 1)
                .input(DrinkableItems.COPPER_CAN)
                .input(SUGAR, 3)
                .input(COCOA_BEANS)
                .input(GLISTERING_MELON_SLICE)
                .criterion(
                        RecipeProvider.hasItem(DrinkableItems.COPPER_CAN),
                        RecipeProvider.conditionsFromItem(DrinkableItems.COPPER_CAN))
                .offerTo(exporter);
        RecipeProvider.offerFoodCookingRecipe(
                exporter,
                "campfire",
                RecipeSerializer.CAMPFIRE_COOKING,
                600,
                DrinkableItems.WHEAT_EXTRACT,
                DrinkableItems.WHEAT_BEER,
                1f);
        RecipeProvider.offerFoodCookingRecipe(
                exporter,
                "campfire",
                RecipeSerializer.CAMPFIRE_COOKING,
                1200,
                DrinkableItems.WINE_BASE,
                DrinkableItems.WINE_BOTTLE,
                1f);
        RecipeProvider.offerFoodCookingRecipe(
                exporter,
                "campfire",
                RecipeSerializer.CAMPFIRE_COOKING,
                600,
                DrinkableItems.ENERGETIC_EXTRACT,
                DrinkableItems.ENERGY_MIXTURE,
                1f);
    }
}