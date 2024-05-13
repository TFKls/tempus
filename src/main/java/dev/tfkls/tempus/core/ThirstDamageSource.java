package dev.tfkls.tempus.core;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ThirstDamageSource {
		public static final RegistryKey<DamageType> THIRST = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("tempus", "thirst_damage_type"));

		public static DamageSource of(World world, RegistryKey<DamageType> key) {
			return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
		}
}
