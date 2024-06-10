package dev.tfkls.tempus.manager;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.effect.PlayerStatusEffector;
import dev.tfkls.tempus.misc.NutritionType;
import dev.tfkls.tempus.networking.ServerEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;

import static dev.tfkls.tempus.Tempus.LOGGER;

public class NutritionManager {
    private final int nutritionLevelMax = Tempus.config.nutritionLevelMax;
    private final int tickTimerBound = Tempus.config.nutritionTickTimerBound;
    private NutritionType nutritionType = NutritionType.NONE;
    private int nutritionLevel = 0;
    private int nutritionTickTimer = 0;
    private PlayerStatusEffector cachedEffector = PlayerStatusEffector.NONE;

    public int getNutritionLevel() {
        return nutritionLevel;
    }

    public NutritionType getNutritionType() {
        return nutritionType;
    }

    public void syncNutrition(PlayerEntity pl) {
        if (pl instanceof ServerPlayerEntity player) {
            PacketByteBuf buffer = PacketByteBufs.create();
            NbtCompound nbt = new NbtCompound();
            writeNbt(nbt);
            buffer.writeNbt(nbt);
            ServerPlayNetworking.send(player, ServerEvents.NUTRITION, buffer);
        }
    }

    public void update(PlayerEntity player) {
        nutritionTickTimer++;
        if (nutritionTickTimer >= tickTimerBound) {
            nutritionTickTimer = 0;
            int currentFoodLevel = player.getHungerManager().getFoodLevel();
            cachedEffector.runEffect(player, Math.min(currentFoodLevel, nutritionLevel));
            cachedEffector.runEffect(player, -nutritionLevel);
        }
    }

    public void add(NutritionType newType) {
        LOGGER.info("Current nutrition: {} of {}; Δ {}", nutritionType, nutritionLevel, newType);
        if (nutritionType == NutritionType.NONE) {
            nutritionType = newType;
            nutritionLevel = (newType == NutritionType.NONE ? 0 : 1);
            cachedEffector = newType.effector;
        } else if (nutritionType == newType) {
            if (nutritionLevel < nutritionLevelMax) nutritionLevel++;
        } else {
            nutritionLevel--;
            if (nutritionLevel <= 0) {
                nutritionLevel = 0;
                nutritionType = NutritionType.NONE;
                cachedEffector = PlayerStatusEffector.NONE;
            }
        }
        LOGGER.info("New nutrition is: {} of {}", nutritionType, nutritionLevel);
        nutritionTickTimer = tickTimerBound;
    }

    public void add(Item foodItem) {
        RegistryEntry<Item> registryEntry = Registries.ITEM.getEntry(foodItem);
        TagKey<Item> tag;
        for (var type : NutritionType.values()) {
            tag = type.tag;
            if (tag != null && registryEntry.isIn(tag)) {
                add(type);
                return;
            }
        }
        add(NutritionType.NONE);
    }

    public void setState(NutritionType type, int level) {
        nutritionType = (level == 0) ? NutritionType.NONE : type;
        nutritionLevel = (type == NutritionType.NONE) ? 0 : level;
        nutritionTickTimer = tickTimerBound;
        cachedEffector = nutritionType.effector;
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("nutritionLevel", NbtElement.NUMBER_TYPE)) {
            this.nutritionLevel = nbt.getInt("nutritionLevel");
            this.nutritionType = NutritionType.values()[nbt.getInt("nutritionType")];
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