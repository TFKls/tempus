package dev.tfkls.tempus.core;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.networking.Packets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class ThirstManager {
    private int thirstLevel = 20;
    private int thirstTickTimer;
    private final PlayerEntity player;

    public ThirstManager(PlayerEntity player) {
        this.player = player;
    }

    public void syncThirst() {
        if (player.getWorld().isClient()) return;
        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(thirstLevel);
        buffer.writeInt(thirstTickTimer);
        ServerPlayNetworking.send((ServerPlayerEntity)player, Packets.THIRST, buffer);
    }

    public int getThirst() {
        return thirstLevel;
    }

    public void add(int val) {
        this.thirstLevel = Math.min(this.thirstLevel+val, 20);
        syncThirst();
    }

    public void drink(MixinItemAccessor item) {
        if (item.tempus$isDrink()) {
            DrinkComponent drinkComponent = item.tempus$getDrinkComponent();
            this.add(drinkComponent.getThirst());
        }
    }

    public void update(PlayerEntity player) {
        thirstTickTimer++;
        if (thirstTickTimer>=80) {

            if (thirstLevel<=0) player.damage(player.getDamageSources().starve(), 1.0f);
            else {
                thirstLevel--;
                syncThirst();
                Tempus.LOGGER.info("Thirst level is {}, tick count: {}",getThirst(),thirstTickTimer);
            }

            thirstTickTimer=0;
        }
    }

    public void readNbt(NbtCompound nbt) {
        if (nbt.contains("thirstLevel", NbtElement.NUMBER_TYPE)) {
            this.thirstLevel = nbt.getInt("thirstLevel");
            this.thirstTickTimer = nbt.getInt("thirstTickTimer");
        }
    }

    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("thirstLevel", thirstLevel);
        nbt.putInt("thirstTickTimer", thirstTickTimer);
    }

    public interface MixinItemSettingsAccessor {
        Item.Settings tempus$setDrinkComponent(DrinkComponent drinkComponent);
        DrinkComponent tempus$getDrinkComponent();
    }

    public interface MixinItemAccessor {
        boolean tempus$isDrink();
        DrinkComponent tempus$getDrinkComponent();
    }

    public interface MixinPlayerEntityAccessor {
        ThirstManager tempus$getThirstManager();
    }
}
