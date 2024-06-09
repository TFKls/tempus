package dev.tfkls.tempus.item;

import dev.tfkls.tempus.managers.TemperatureManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ThermometerItem extends Item {
	public ThermometerItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient) {
			user.sendMessage(Text.of("Your temperature is " + ((TemperatureManager.MixinAccessor) user).tempus$getTemperatureManager().getTemperature()));
			user.getStackInHand(hand).damage(1, user, (e) -> {
			});
		}
		return TypedActionResult.pass(user.getStackInHand(hand));
	}
}
