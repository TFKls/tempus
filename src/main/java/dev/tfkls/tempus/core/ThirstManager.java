package dev.tfkls.tempus.core;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.networking.Packets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class ThirstManager {
    private int thirstLevel = 20;
    private int thirstTickTimer;
    private int thirstTickThreshold = 80;
    private final PlayerEntity player;
    private boolean sync = true;

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

    public void setThirstTickThreshold(int threshold) {
        this.thirstTickThreshold = threshold;
    }

    public void add(int val) {
        if (player.getWorld().isClient()) return;
        this.thirstLevel = Math.min(this.thirstLevel+val, 20);
        sync = true;
    }

    public void drink(DrinkComponent.MixinAccessor item) {
        drink(item.tempus$getDrinkComponent());
    }
    public void drink(DrinkComponent drinkComponent) {
        if (drinkComponent != null) {
            this.add(drinkComponent.getThirst());
        }
    }

    public void update(PlayerEntity player) {
        if (sync) {
            syncThirst();
            sync = false;
        }
        thirstTickTimer++;
        if (thirstTickTimer>=thirstTickThreshold) {

            if (thirstLevel<=0)
                player.damage(ThirstDamageSource.of(player.getWorld(),ThirstDamageSource.THIRST), 1.0f);
            else {
                thirstLevel--;
                sync = true;
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

    public interface MixinAccessor {
        ThirstManager tempus$getThirstManager();
    }
}
