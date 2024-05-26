package dev.tfkls.tempus.core;

import dev.tfkls.tempus.Tempus;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public enum NutritionType {
    NONE(null, PlayerStatusEffector.NONE),
    CARBOHYDRATE(Tags.CARBOHYDRATE, Effects.CARBOHYDRATE),
    FAT(Tags.FAT, Effects.FAT),
    PROTEIN(Tags.PROTEIN, Effects.PROTEIN);

    @Nullable
    public final TagKey<Item> tag;
    public final PlayerStatusEffector effector;
    NutritionType(@Nullable TagKey<Item> tag, PlayerStatusEffector effector) {
        this.tag = tag;
        this.effector = effector;
    }

    static class Tags {
        public static final TagKey<Item> CARBOHYDRATE = TagKey.of(RegistryKeys.ITEM, new Identifier("tempus", "nutrition_carbohydrate"));
        public static final TagKey<Item> FAT = TagKey.of(RegistryKeys.ITEM, new Identifier("tempus", "nutrition_fat"));
        public static final TagKey<Item> PROTEIN = TagKey.of(RegistryKeys.ITEM, new Identifier("tempus", "nutrition_protein"));
    }

    static class Effects {

        private static void addNutritionStatusEffect(PlayerEntity player, StatusEffect effect, int amplifier) {
            if (amplifier > 0) {
                player.addStatusEffect(new StatusEffectInstance(effect, 5*20, amplifier-1, true, false, true));
            }
        }

        public static PlayerStatusEffector CARBOHYDRATE = PlayerStatusEffector.of(
                (player, grade) -> {
                    addNutritionStatusEffect(player,StatusEffects.SPEED, (grade-1)/3);
                    addNutritionStatusEffect(player,StatusEffects.JUMP_BOOST, Math.max(grade-3, 0)/3);
                },
                (player, grade) -> {
                    addNutritionStatusEffect(player,StatusEffects.WEAKNESS, Math.max(grade-2, 0)/3);
                    addNutritionStatusEffect(player,StatusEffects.MINING_FATIGUE, Math.max(grade-4, 0)/3);
                }
        );
        public static PlayerStatusEffector FAT = PlayerStatusEffector.of(
                (player, grade) -> {
                    addNutritionStatusEffect(player,StatusEffects.SATURATION, (grade-1)/3);
                    addNutritionStatusEffect(player,StatusEffects.SLOW_FALLING, Math.max(grade-3, 0)/3);
                },
                (player, grade) -> {
                    addNutritionStatusEffect(player,StatusEffects.SLOWNESS, Math.max(grade-2, 0)/3);
                    addNutritionStatusEffect(player, Tempus.THIRST, Math.max(grade-4, 0)/3);
                }
        );
        public static PlayerStatusEffector PROTEIN = PlayerStatusEffector.of(
                (player, grade) -> {
                    addNutritionStatusEffect(player,StatusEffects.STRENGTH, (grade-1)/3);
                    addNutritionStatusEffect(player,StatusEffects.DOLPHINS_GRACE, Math.max(grade-3, 0)/3);
                },
                (player, grade) -> {
                    addNutritionStatusEffect(player,StatusEffects.HUNGER, Math.max(grade-2, 0)/3);
                    addNutritionStatusEffect(player,StatusEffects.NAUSEA, Math.max(grade-4, 0)/3);
                }
        );
    }
}
