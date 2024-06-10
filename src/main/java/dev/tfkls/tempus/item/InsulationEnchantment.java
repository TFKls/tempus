package dev.tfkls.tempus.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;

public class InsulationEnchantment extends Enchantment {
    public InsulationEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.ARMOR, new EquipmentSlot[] {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
        });
    }

    @Override
    public int getMinPower(int level) {
        return level * 10;
    }

    @Override
    public int getMaxPower(int level) {
        return level * 30;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    public float getInsulationLevel(PlayerEntity player) {
        return (float) EnchantmentHelper.getEquipmentLevel(this, player);
    }
}