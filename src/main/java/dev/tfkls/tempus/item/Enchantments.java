package dev.tfkls.tempus.item;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static dev.tfkls.tempus.Tempus.LOGGER;

public class Enchantments {
    public static InsulationEnchantment INSULATION = new InsulationEnchantment();
    public static void register() {
        Registry.register(Registries.ENCHANTMENT, new Identifier("tempus", "insulation"), INSULATION);
        LOGGER.info("Registered 'Insulation' enchantment");
    }
}
