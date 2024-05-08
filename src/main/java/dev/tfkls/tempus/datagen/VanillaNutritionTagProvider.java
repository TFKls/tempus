package dev.tfkls.tempus.datagen;

import dev.tfkls.tempus.core.Nutrition;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class VanillaNutritionTagProvider extends FabricTagProvider.ItemTagProvider {
    public VanillaNutritionTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(Nutrition.Tags.CARBOHYDRATE)
                .add(Items.APPLE)
                .add(Items.BAKED_POTATO)
                .add(Items.BEETROOT)
                .add(Items.BEETROOT_SOUP);

        getOrCreateTagBuilder(Nutrition.Tags.FAT)
                .add(Items.COOKIE)
                .add(Items.MUSHROOM_STEW)
                .add(Items.PUMPKIN_PIE)
                .add(Items.BEEF);

        getOrCreateTagBuilder(Nutrition.Tags.PROTEIN)
                .add(Items.COOKED_CHICKEN)
                .add(Items.COOKED_COD)
                .add(Items.COOKED_MUTTON)
                .add(Items.COOKED_PORKCHOP);
    }
}
