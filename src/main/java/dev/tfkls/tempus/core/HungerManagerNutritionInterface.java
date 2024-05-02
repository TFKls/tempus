package dev.tfkls.tempus.core;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Unique;

public interface HungerManagerNutritionInterface {
    @Unique
    Nutrition.Type tempus$getNutritionType();

    @Unique
    int tempus$getNutritionLevel();
    @Unique
    void tempus$updateNutrition(Nutrition.Type type);
    @Unique
    void tempus$updateNutrition(Item foodItem);
}
