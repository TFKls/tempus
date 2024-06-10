package dev.tfkls.tempus.item;

import dev.tfkls.tempus.manager.ThirstManager;
import dev.tfkls.tempus.misc.DrinkComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DrinkableItem extends Item {

    @Unique
    protected DrinkComponent drinkComponent;

    @Unique
    protected Item drinkRemainder;

    @Unique
    protected List<StatusEffectInstance> effects;

    public DrinkableItem(Settings settings) {
        super(settings);
        this.drinkComponent = settings.drinkComponent;
        this.drinkRemainder = settings.drinkRemainder;
        this.effects = settings.effects;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        boolean decremented = false;
        if (this.isFood()) {
            user.eatFood(world, stack);
            decremented = true;
        }

        PlayerEntity player = (user instanceof PlayerEntity ? (PlayerEntity) user : null);
        if (player == null) return stack;

        ThirstManager manager = ((ThirstManager.MixinAccessor) player).tempus$getThirstManager();
        manager.drink(drinkComponent);
        manager.syncThirst(player);
        effects.forEach(player::addStatusEffect);
        if (!player.getAbilities().creativeMode) {
            if (!decremented) stack.decrement(1);
            player.getInventory().offerOrDrop(new ItemStack(drinkRemainder));
        }
        if (!world.isClient() && !this.drinkComponent.isPurified()) {
            manager.unpurifiedRollEffects();
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
        // Objects.requireNonNull() used to suppress a warning
        if ((!this.isFood()
                        || !user.canConsume(
                                Objects.requireNonNull(this.getFoodComponent()).isAlwaysEdible()))
                && !((ThirstManager.MixinAccessor) user)
                        .tempus$getThirstManager()
                        .canDrink(user)) {
            ItemStack itemStack = user.getStackInHand(hand);
            return TypedActionResult.fail(itemStack);
        }
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    public static class Settings extends Item.Settings {
        DrinkComponent drinkComponent = new DrinkComponent(3);
        Item drinkRemainder = Items.GLASS_BOTTLE;
        List<StatusEffectInstance> effects = new ArrayList<>();

        public Settings() {
            this.maxCount(8);
        }

        public Settings drink(DrinkComponent drinkComponent) {
            this.drinkComponent = drinkComponent;
            return this;
        }

        @Override
        public Settings maxCount(int maxCount) {
            super.maxCount(maxCount);
            return this;
        }

        public Settings drinkRemainder(Item drinkRemainder) {
            this.drinkRemainder = drinkRemainder;
            return this;
        }

        public Settings addEffect(StatusEffectInstance effect) {
            effects.add(effect);
            return this;
        }
    }
}
