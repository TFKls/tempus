package dev.tfkls.tempus.manager;

import dev.tfkls.tempus.Tempus;
import dev.tfkls.tempus.misc.SeasonServerState;
import dev.tfkls.tempus.util.MathUtil;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import static dev.tfkls.tempus.Tempus.LOGGER;

/**
 * This class is a singleton which manages the seasons for the currently loaded world.
 * Sadly, this behaviour is required, as the Biome classes are not aware of the currently loaded world,
 * which makes it hard to implement changes to the Biome temperature (allowing for snow and similar) otherwise.
 */
public class SeasonManager {
    private static final SeasonManager THIS = new SeasonManager();
    MinecraftServer currentServer = null;
    private long seasonOffset = 0;
    private boolean seasonCycle = true;
    private long seasonPeriod = Tempus.config.seasonPeriod;

    public static SeasonManager getInstance() {
        return THIS;
    }

    public long getSeasonOffset() {
        return seasonOffset;
    }

    public void setSeasonOffset(long seasonOffset) {
        this.seasonOffset = (seasonOffset + seasonPeriod) % seasonPeriod;
    }

    public boolean getSeasonCycle() {
        return seasonCycle;
    }

    public void setSeasonCycle(boolean seasonCycle) {
        this.seasonCycle = seasonCycle;
    }

    public long getSeasonPeriod() {
        return seasonPeriod;
    }

    public void setSeasonPeriod(long seasonPeriod) {
        this.seasonPeriod = seasonPeriod;
    }

    private World getOverworld() {
        assert currentServer != null;
        return currentServer.getOverworld();
    }

    /**
     * We assume only one server instance is loaded at the same time here
     * This is a safer assumption than the one before (which assumed only one world is loaded, which is false as
     * dimensions are separate worlds)
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
        return (float) Math.sin((float) currentSeason() / seasonPeriod * (2 * Math.PI));
    }

    public float ambientTemperature() {
        return Tempus.config.ambientTemperatureMultiplier * sineMultiplier();
    }

    public float biomeTemperatureDelta() {
        return (currentServer == null) ? 0f : Math.min(0.5f, sineMultiplier());
    }

    public int modifyTickSpeed(int currentTickSpeed) {
        return currentTickSpeed
                + MathUtil.roundDown(Tempus.config.tickSpeedMultiplier * sineMultiplier() * currentTickSpeed);
    }
}
