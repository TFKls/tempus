package dev.tfkls.tempus.mixin;

import dev.tfkls.tempus.core.DrinkComponent;
import dev.tfkls.tempus.core.ThirstManager;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Items.class)
public abstract class ItemsThirstMixin {

    @Redirect(slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=potion", ordinal = 0)),
            method = "<clinit>", at = @At(value = "NEW", target = "Lnet/minecraft/item/PotionItem;"))
    private static PotionItem overridePotionItem(Item.Settings settings) {
        Item.Settings newSettings = new Item.Settings().maxCount(1);
        ((DrinkComponent.MutableMixinAccessor)newSettings).tempus$setDrinkComponent(new DrinkComponent(3));
        return new PotionItem(newSettings);
    }

}
