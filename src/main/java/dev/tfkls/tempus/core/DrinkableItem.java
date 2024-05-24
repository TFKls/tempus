package dev.tfkls.tempus.core;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;

public class DrinkableItem extends Item {

    @Unique
    protected DrinkComponent drinkComponent;

    public DrinkableItem(Settings settings, DrinkComponent drinkComponent) {
        super(settings);
        this.drinkComponent = drinkComponent;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        PlayerEntity player = (user instanceof PlayerEntity ? (PlayerEntity) user : null);
        if (player == null) return  stack;

        ((ThirstManager.MixinAccessor)player).tempus$getThirstManager().drink(drinkComponent);
        if (!player.getAbilities().creativeMode) {
            stack.decrement(1);
            player.getInventory().insertStack(new ItemStack(Items.GLASS_BOTTLE));
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
