package dev.tfkls.tempus.core;

import dev.tfkls.tempus.util.MathUtil;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/* TODO:
    - Add persistence (load data and save data with world)
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

    @Nullable
    World currentWorld = null;
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
    public World getCurrentWorld() {
        assert currentWorld != null;
        return currentWorld;
    }

    /** We only load on overworld, as it's the only dimension where a bed works :))
     *  We kind of already gave up on intermodal compatibility, so this will need a major refactor
     *  if we one day decide to support other dimensions.
     */
    public boolean loadWorld(World world) {
        if (world.isClient() || world == currentWorld || !world.getDimension().bedWorks()) {
            return false;
        } else {
            currentWorld = world;
            return true;
        }
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
        return seasonCycle ? (getCurrentWorld().getTime() + seasonOffset) % seasonPeriod : seasonOffset;
    }

    private float sineMultiplier() {
        return (float) Math.sin((float) currentSeason() / seasonPeriod * (2*Math.PI));
    }

    public float ambientTemperature() {
        return 3.0f*sineMultiplier();
    }

    public float biomeTemperatureDelta() {
        return (currentWorld == null) ? 0f : Math.min(0.5f, sineMultiplier());
    }

    public int modifyTickSpeed(int currentTickSpeed) {
        return currentTickSpeed + MathUtil.roundDown(0.8f * sineMultiplier() * currentTickSpeed);
    }
}
