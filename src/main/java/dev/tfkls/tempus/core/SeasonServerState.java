package dev.tfkls.tempus.core;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;


/**
 * Keeps the data related to the SeasonManager and implements methods to save it to the currently running server.
 * <p> A SeasonManager operates on direct values due to performance reasons (one less layer of indirection).
 * We might move it to a static class of SeasonManager with no data of its own in the future
 */
public class SeasonServerState extends PersistentState {
    long seasonOffset = 0;
    boolean seasonCycle = true;
    long seasonPeriod = 20 * 60 * 20 * 12; // 20min of realtime * 20 ticks * 12 months

    SeasonServerState() {}

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putLong("seasonOffset", seasonOffset);
        nbt.putBoolean("seasonCycle", seasonCycle);
        nbt.putLong("seasonPeriod", seasonPeriod);
        return nbt;
    }


    public static SeasonServerState createFromNbt(NbtCompound nbt) {
        SeasonServerState state = new SeasonServerState();
        state.seasonOffset = nbt.getLong("seasonOffset");
        state.seasonCycle = nbt.getBoolean("seasonCycle");
        state.seasonPeriod = nbt.getLong("seasonPeriod");
        return state;
    }

    public static SeasonServerState getServerState(MinecraftServer server) {
        PersistentStateManager stateManager = server.getOverworld().getPersistentStateManager();
        SeasonServerState state = stateManager.getOrCreate(SeasonServerState::createFromNbt,
                SeasonServerState::new,
                "tempus:season");
        state.markDirty();
        return state;
    }

    public void loadSeasonManager(SeasonManager seasonManager) {
        seasonOffset = seasonManager.seasonOffset;
        seasonCycle = seasonManager.seasonCycle;
        seasonPeriod = seasonManager.seasonPeriod;
    }
    public void writeSeasonManager(SeasonManager seasonManager) {
        seasonManager.seasonOffset = seasonOffset;
        seasonManager.seasonCycle = seasonCycle;
        seasonManager.seasonPeriod = seasonPeriod;
    }
}
