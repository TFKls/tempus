package dev.tfkls.tempus.core;

import dev.tfkls.tempus.Tempus;
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
	private long seasonOffset = 0;
	private boolean seasonCycle = true;
	private long seasonPeriod = Tempus.config.seasonPeriod;

	SeasonServerState() {
	}

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
		seasonOffset = seasonManager.getSeasonOffset();
		seasonCycle = seasonManager.getSeasonCycle();
		seasonPeriod = seasonManager.getSeasonPeriod();
	}

	public void writeSeasonManager(SeasonManager seasonManager) {
		seasonManager.setSeasonOffset(seasonOffset);
		seasonManager.setSeasonCycle(seasonCycle);
		seasonManager.setSeasonPeriod(seasonPeriod);
	}
}
