package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.init.DrinkFermenterInitializer;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.CookingRecipeCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * This is a fix to allow for Fermenter recipe generation, we will remove it if we change the fermenter logic considerably
 */
@Mixin(CookingRecipeJsonBuilder.class)
public abstract class CookingRecipeJsonBuilderDrinkFermenterMixin {
    @Inject(method = "getCookingRecipeCategory", at = @At(value = "HEAD"), cancellable = true)
    private static void appendFermenterCookingType(RecipeSerializer<? extends AbstractCookingRecipe> serializer, ItemConvertible output, CallbackInfoReturnable<CookingRecipeCategory> cir) {
        if (serializer == DrinkFermenterInitializer.RECIPE_SERIALIZER) {
            cir.setReturnValue(CookingRecipeCategory.FOOD);
        }
    }
}
