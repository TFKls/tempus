package dev.tfkls.tempus.recipe;

import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static dev.tfkls.tempus.init.DrinkFermenterInitializer.*;

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
