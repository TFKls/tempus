package dev.tfkls.tempus.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.util.Identifier;

import static dev.tfkls.tempus.init.DrinkFermenterInitializer.RECIPE_SERIALIZER;
import static dev.tfkls.tempus.init.DrinkFermenterInitializer.RECIPE_TYPE;

public class DrinkFermenterRecipe extends AbstractCookingRecipe {
	public DrinkFermenterRecipe(Identifier id, String group, CookingRecipeCategory category, Ingredient input, ItemStack output, float experience, int cookTime) {
		super(RECIPE_TYPE, id, group, category, input, output, experience, cookTime);
	}

	/**
	 * {@return the serializer associated with this recipe}
	 */
	@Override
	public RecipeSerializer<?> getSerializer() {
		return RECIPE_SERIALIZER;
	}

	// TODO: Add icon here once we add new textures for items
	@Override
	public ItemStack createIcon() {
		return super.createIcon();
	}
}
