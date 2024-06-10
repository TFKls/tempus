package dev.tfkls.tempus.datagen;

import dev.tfkls.tempus.item.DrinkableItems;
import dev.tfkls.tempus.item.TempusItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class CustomItemModelProvider extends FabricModelProvider {
    public CustomItemModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(TempusItems.THERMOMETER, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.PURIFIED_WATER_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.WOODEN_MUG, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.MUG_OF_WATER, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.WHEAT_EXTRACT, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.WHEAT_BEER, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.WINE_BASE, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.WINE_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.COPPER_CAN, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.CAN_OF_WATER, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.ENERGETIC_EXTRACT, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.ENERGY_MIXTURE, Models.GENERATED);
        itemModelGenerator.register(DrinkableItems.BEPIS_CAN, Models.GENERATED);
    }
}
