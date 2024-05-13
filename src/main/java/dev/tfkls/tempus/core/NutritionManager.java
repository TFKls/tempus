package dev.tfkls.tempus.core;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

import static dev.tfkls.tempus.Tempus.LOGGER;

public class NutritionManager {
    private Nutrition.Type nutritionType = Nutrition.Type.NONE;
    private int nutritionLevel = 0;
    private int nutritionTickTimer = 0;
    private PlayerStatusEffector cachedEffector = PlayerStatusEffector.NONE;

    public int getNutritionLevel() {
        return nutritionLevel;
    }

    public Nutrition.Type getNutritionType() {
        return nutritionType;
    }

    public void update(PlayerEntity player) {
        nutritionTickTimer++;
        if (nutritionTickTimer >= 80) {
            nutritionTickTimer = 0;
            int currentFoodLevel = player.getHungerManager().getFoodLevel();
            cachedEffector.runEffect(player, Math.min(currentFoodLevel, nutritionLevel));
            cachedEffector.runEffect(player, -nutritionLevel);
        }
    }

    public void add(Nutrition.Type newType) {
        LOGGER.info("Current nutrition: {} of {}; Δ {}", nutritionType, nutritionLevel, newType);
        if (nutritionType == Nutrition.Type.NONE) {
            nutritionType = newType;
            nutritionLevel = (newType == Nutrition.Type.NONE ? 0 : 1);
            cachedEffector = newType.toEffector();
        } else if (nutritionType == newType) {
            if (nutritionLevel < 10) nutritionLevel++;
        } else {
            nutritionLevel--;
            if (nutritionLevel <= 0) {
                nutritionLevel = 0;
                nutritionType = Nutrition.Type.NONE;
                cachedEffector = PlayerStatusEffector.NONE;
            }
        }
        LOGGER.info("New nutrition is: {} of {}", nutritionType, nutritionLevel);
        nutritionTickTimer = 80;
    }

    public void add(Item foodItem) {
        RegistryEntry<Item> registryEntry = Registries.ITEM.getEntry(foodItem);
        TagKey<Item> tag;
        for (var type : Nutrition.Type.values()) {
            tag = type.toTag();
            if (tag != null && registryEntry.isIn(tag)) {
                add(type);
                return;
            }
        }
        add(Nutrition.Type.NONE);
    }

    public void setState(Nutrition.Type type, int level) {
        nutritionType = (level == 0) ? Nutrition.Type.NONE : type;
        nutritionLevel = (type == Nutrition.Type.NONE) ? 0 : level;
        nutritionTickTimer = 80;
        cachedEffector = nutritionType.toEffector();
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("nutritionLevel", NbtElement.NUMBER_TYPE)) {
            this.nutritionLevel = nbt.getInt("nutritionLevel");
            this.nutritionType = Nutrition.Type.values()[nbt.getInt("nutritionType")];
            this.nutritionTickTimer = nbt.getInt("nutritionTickTimer");
        }
    }
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("nutritionLevel", nutritionLevel);
        nbt.putInt("nutritionType", nutritionType.ordinal());
        nbt.putInt("nutritionTickTimer", nutritionTickTimer);
    }

    public interface MixinAccessor {
        NutritionManager tempus$getNutritionManager();
    }
}
