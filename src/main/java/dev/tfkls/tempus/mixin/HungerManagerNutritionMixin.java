package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.HungerManagerNutritionInterface;
import dev.tfkls.tempus.core.Nutrition;
import dev.tfkls.tempus.core.PlayerStatusEffector;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.tfkls.tempus.Tempus.LOGGER;

@Mixin(HungerManager.class)
public abstract class HungerManagerNutritionMixin implements HungerManagerNutritionInterface {
    @Shadow public abstract int getFoodLevel();

    @Unique
    private Nutrition.Type nutritionType = Nutrition.Type.NONE;
    @Unique
    private int nutritionLevel = 0;
    @Unique
    private int nutritionTickTimer = 0;
    @Override
    public Nutrition.Type tempus$getNutritionType() {
        return nutritionType;
    }
    @Override
    public int tempus$getNutritionLevel() {
        return nutritionLevel;
    }
    @Override
    public void tempus$updateNutrition(Nutrition.Type newType) {
        LOGGER.info("Current nutrition: {} of {}; delta is {}", nutritionType, nutritionLevel, newType);
        if (nutritionType == Nutrition.Type.NONE) {
            nutritionType = newType;
            nutritionLevel = (newType == Nutrition.Type.NONE ? 0 : 1);
        } else if (nutritionType == newType) {
            if (nutritionLevel<10) nutritionLevel++;
        } else {
            nutritionLevel--;
            if (nutritionLevel <= 0) {
                nutritionLevel = 0;
                nutritionType = Nutrition.Type.NONE;
            }
        }
        LOGGER.info("New nutrition is: {} of {}", nutritionType, nutritionLevel);
        nutritionTickTimer = 80;
    }
    @Override
    public void tempus$updateNutrition(Item foodItem) {
        RegistryEntry<Item> registryEntry = Registries.ITEM.getEntry(foodItem);
        TagKey<Item> tag;
        for (var type : Nutrition.Type.values()) {
            tag = type.toTag();
            if (tag != null && registryEntry.isIn(tag)) {
                tempus$updateNutrition(type);
                return;
            }
        }
        tempus$updateNutrition(Nutrition.Type.NONE);
    }

    @Inject(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;add(IF)V"))
    private void injectFoodNutritionType(Item item, ItemStack stack, CallbackInfo ci) {
        this.tempus$updateNutrition(item);
    }

    @Inject(method = "readNbt", at = @At(value = "TAIL"))
    private void readNutritionNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("nutritionLevel", NbtElement.NUMBER_TYPE)) {
            this.nutritionLevel = nbt.getInt("nutritionLevel");
            this.nutritionType = Nutrition.Type.values()[nbt.getInt("nutritionType")];
            this.nutritionTickTimer = nbt.getInt("nutritionTickTimer");
        }
    }

    @Inject(method = "writeNbt", at = @At(value = "TAIL"))
    private void writeNutritionNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("nutritionLevel", nutritionLevel);
        nbt.putInt("nutritionType", nutritionType.ordinal());
        nbt.putInt("nutritionTickTimer", nutritionTickTimer);
    }

    @Inject(method = "update", at = @At(value = "TAIL"))
    private void runNutritionEffects(PlayerEntity player, CallbackInfo ci) {
        if (nutritionTickTimer >= 80) {
            int currentFoodLevel = getFoodLevel() / 2;
            PlayerStatusEffector currentEffector = tempus$getNutritionType().toEffector();
            currentEffector.runEffect(player, Math.min(currentFoodLevel, nutritionLevel));
            currentEffector.runEffect(player, -nutritionLevel);
            nutritionTickTimer = 0;
        } else {
            nutritionTickTimer++;
        }
    }
}
