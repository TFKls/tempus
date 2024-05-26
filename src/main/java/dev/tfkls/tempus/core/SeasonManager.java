package dev.tfkls.tempus.core;

import dev.tfkls.tempus.util.MathUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static dev.tfkls.tempus.Tempus.LOGGER;

/* TODO:
    - Think about day/night cycle, whether we want to break it a bit in favour of more realism (probably lot of work)
    - Make Biome mixin actually work and snow in the winter
 */


/**
 * This class is a singleton which manages the seasons for the currently loaded world.
 * Sadly, this behaviour is required, as the Biome classes are not aware of the currenly loaded world,
 * which makes it hard to implement changes to the Biome temperature (allowing for snow and similar) otherwise.
 */

public class SeasonManager {
    long seasonOffset = 0;
    boolean seasonCycle = true;
    long seasonPeriod = 20 * 60 * 20 * 12; // 20min of realtime * 20 ticks * 12 months

    MinecraftServer currentServer = null;
    private static final SeasonManager THIS = new SeasonManager();

    public static SeasonManager getInstance() {
        return THIS;
    }

    public long getSeasonOffset() {
        return seasonOffset;
    }
    public void setSeasonOffset(long seasonOffset) {
        this.seasonOffset = (seasonOffset + seasonPeriod) % seasonPeriod;
    }
    public long getSeasonPeriod() {
        return seasonPeriod;
    }
    private World getOverworld() {
        assert currentServer != null;
        return currentServer.getOverworld();
    }

    /** We assume only one server instance is loaded at the same time here
     * This is a safer assumption than the one before (which assumed only one world is loaded, which is false as
     *  dimensions are separate worlds)
     */
    public void loadServer(MinecraftServer server) {
        LOGGER.info("Loading server {} into SeasonManager...", server);
        this.currentServer = server;
        SeasonServerState.getServerState(server).writeSeasonManager(this);
    }

    public void updateSeasonCycle(boolean seasonCycle) {
        if (seasonCycle && !this.seasonCycle) {
            this.seasonCycle = true;
            setSeasonOffset(seasonOffset - (currentSeason() - seasonOffset));
        } else if (!seasonCycle && this.seasonCycle) {
            seasonOffset = currentSeason();
            this.seasonCycle = false;
        }
    }

    public long currentSeason() {
        return seasonCycle ? (getOverworld().getTime() + seasonOffset) % seasonPeriod : seasonOffset;
    }

    private float sineMultiplier() {
        return (float) Math.sin((float) currentSeason() / seasonPeriod * (2*Math.PI));
    }

    public float ambientTemperature() {
        return 3.0f*sineMultiplier();
    }

    public float biomeTemperatureDelta() {
        return (currentServer == null) ? 0f : Math.min(0.5f, sineMultiplier());
    }

    public int modifyTickSpeed(int currentTickSpeed) {
        return currentTickSpeed + MathUtil.roundDown(0.8f * sineMultiplier() * currentTickSpeed);
    }
}
