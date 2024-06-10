package dev.tfkls.tempus.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;

public class DrinkContainerItem extends Item {
	@Unique
	protected Item waterItem;

	public DrinkContainerItem(Settings settings, Item waterItem) {
		super(settings);
		this.waterItem = waterItem;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		BlockHitResult blockHitResult = DrinkContainerItem.raycast(world, user, RaycastContext.FluidHandling.WATER);
		if (blockHitResult.getType() == HitResult.Type.BLOCK) {
			ItemStack replacement = new ItemStack(waterItem, itemStack.getCount());
			ItemStack result = ItemUsage.exchangeStack(itemStack, user, replacement);
			return TypedActionResult.success(result);
		}
		return TypedActionResult.pass(itemStack);
	}

	public static Pair<DrinkContainerItem, DrinkableItem> createItemPair(DrinkableItem.Settings settings) {
		DrinkContainerItem containerItem = new DrinkContainerItem(settings, null);
		DrinkableItem waterItem = new DrinkableItem(settings.drinkRemainder(containerItem));
		containerItem.waterItem = waterItem;
		return new Pair<>(containerItem, waterItem);
	}
}
