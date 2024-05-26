package dev.tfkls.tempus.item;

import dev.tfkls.tempus.core.DrinkComponent;
import dev.tfkls.tempus.core.ThirstManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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

    public DrinkableItem(Settings settings) {
        super(settings);
        this.drinkComponent = settings.drinkComponent;
    }

    public static class Settings extends Item.Settings {
        DrinkComponent drinkComponent = new DrinkComponent(3);

        public Settings() {
            this.maxCount(16);
        }

        public Settings drink(DrinkComponent drinkComponent) {
            this.drinkComponent = drinkComponent;
            return this;
        }
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
        if (!world.isClient() && !this.drinkComponent.isPurified()) {
            if (Math.random()>0.5) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 3*20));
            }
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
