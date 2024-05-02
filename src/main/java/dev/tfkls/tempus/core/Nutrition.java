package dev.tfkls.tempus.core;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class Nutrition {
    public enum Type {
        NONE,
        CARBOHYDRATE,
        FAT,
        PROTEIN;

        public TagKey<Item> toTag() {
            switch (this) {
                case NONE -> {
                    return null;
                }
                case CARBOHYDRATE -> {
                    return Tags.CARBOHYDRATE;
                }
                case FAT -> {
                    return Tags.FAT;
                }
                case PROTEIN -> {
                    return Tags.PROTEIN;
                }
            }
            return null;
        }

        public PlayerStatusEffector toEffector() {
            switch (this) {
                case CARBOHYDRATE -> {
                    return Effects.CARBOHYDRATE;
                }
                case FAT -> {
                    return Effects.FAT;
                }
                case PROTEIN -> {
                    return Effects.PROTEIN;
                }
            }
            return PlayerStatusEffector.NONE;
        }
    }

    public static class Tags {
        public static final TagKey<Item> CARBOHYDRATE = TagKey.of(RegistryKeys.ITEM, new Identifier("tempus", "nutrition_carbohydrate"));
        public static final TagKey<Item> FAT = TagKey.of(RegistryKeys.ITEM, new Identifier("tempus", "nutrition_fat"));
        public static final TagKey<Item> PROTEIN = TagKey.of(RegistryKeys.ITEM, new Identifier("tempus", "nutrition_protein"));
    }

    public static class Effects {
        public static PlayerStatusEffector CARBOHYDRATE = PlayerStatusEffector.of(
                (player, grade) -> {

                },
                (player, grade) -> {

                }
        );
        public static PlayerStatusEffector FAT = PlayerStatusEffector.of(
                (player, grade) -> {

                },
                (player, grade) -> {

                }
        );
        public static PlayerStatusEffector PROTEIN = PlayerStatusEffector.of(
                (player, grade) -> {

                },
                (player, grade) -> {

                }
        );
    }
}
