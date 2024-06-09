package dev.tfkls.tempus.init;

import dev.tfkls.tempus.block.DrinkFermenter;
import dev.tfkls.tempus.block.entity.DrinkFermenterEntity;
import dev.tfkls.tempus.recipe.DrinkFermenterRecipe;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import static dev.tfkls.tempus.Tempus.LOGGER;

/**
 * TODO: We may want to change this to not be like a furnace and accept multiple inputs.
 *  This will require more work, so for now we make it a furnace clone. However, this
 *  class allows us to change it more easily if we ever choose to (easier to refactor)
 * WARNING: THIS DOES NOT CURRENTLY WORK (it registers correctly but does not craft or keep inventory)
 */

public class DrinkFermenterInitializer {
	public static Block BLOCK;
	public static Item ITEM;
	public static BlockEntityType<DrinkFermenterEntity> BLOCK_ENTITY_TYPE;
	public static RecipeType<DrinkFermenterRecipe> RECIPE_TYPE;
	public static RecipeSerializer<DrinkFermenterRecipe> RECIPE_SERIALIZER;

	private static Identifier getIdentifier() {
		return new Identifier("tempus", "drink_fermenter");
	}

	public static void register() {
		if (BLOCK != null) {
			LOGGER.warn("Already registered the Drink Fermenter...");
			return;
		}
		LOGGER.info("Registering the Drink Fermenter...");
		BLOCK = Registry.register(Registries.BLOCK, getIdentifier(), new DrinkFermenter(FabricBlockSettings.create()));
		ITEM = Registry.register(Registries.ITEM, getIdentifier(), new BlockItem(BLOCK, new Item.Settings()));
		BLOCK_ENTITY_TYPE = Registry.register(Registries.BLOCK_ENTITY_TYPE, getIdentifier(), BlockEntityType.Builder.create(DrinkFermenterEntity::new, BLOCK).build(null));

		RECIPE_TYPE = Registry.register(Registries.RECIPE_TYPE, getIdentifier(), new RecipeType<DrinkFermenterRecipe>() {
			@Override
			public String toString() {
				return "drink_fermenter";
			}
		});
		RECIPE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER, getIdentifier(), new CookingRecipeSerializer<>(DrinkFermenterRecipe::new, 400));
		LOGGER.info("Registered the Drink Fermenter");
	}

	/**
	 * Currently, this method calls Campfire Cooking as the Fermenter is not yet fully functional (and catalyst is currently ignored).
	 * This will change when we fully implement the Fermenter, however it is sadly not functional yet
	 */
	public static void offerRecipe(Consumer<RecipeJsonProvider> exporter, ItemConvertible ingredient, ItemConvertible catalyst, ItemConvertible output) {
		RecipeProvider.offerFoodCookingRecipe(exporter, "drink_fermenter", RecipeSerializer.CAMPFIRE_COOKING, 600, ingredient, output, 1f);
	}
}
