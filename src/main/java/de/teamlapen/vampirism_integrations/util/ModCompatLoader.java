package de.teamlapen.vampirism_integrations.util;

import com.google.common.collect.ImmutableList;
import de.teamlapen.lib.lib.util.IInitListener;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Handles loading of mod compatibilities
 */
public class ModCompatLoader implements IInitListener {

    private final static Logger LOGGER = LogManager.getLogger();
    private final Map<IModCompat, ForgeConfigSpec.BooleanValue> compatEnableMap = new HashMap<>();
    private final List<IModCompat> incompatibleCompats = new LinkedList<>();
    private
    @Nullable
    List<IModCompat> availableModCompats = new LinkedList<>();
    private List<IModCompat> loadedModCompats;

    /**
     * Add any compats BEFORE pre-init
     *
     * @param compat The compat to add
     */
    public void addModCompat(IModCompat compat) {
        if (availableModCompats == null) {
            throw new IllegalStateException("Add mod compats BEFORE init (" + compat.getModID() + ")");
        }
        availableModCompats.add(compat);
    }

    public void buildConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Compatibility");
        assert availableModCompats != null;
        for (IModCompat c : availableModCompats) {
            if (!isModLoaded(c)) continue;
            builder.push(c.getModID());
            compatEnableMap.put(c, builder.define("enable_compat_" + c.getModID(), true)); //Can only access configuration after game loaded
            c.buildConfig(builder);
            builder.pop();
        }

    }

    @Nullable
    public List<IModCompat> getAvailableModCompats() {
        return availableModCompats;
    }

    public List<IModCompat> getIncompatibleCompats() {
        return ImmutableList.copyOf(incompatibleCompats);
    }

    public List<IModCompat> getLoadedModCompats() {
        return ImmutableList.copyOf(loadedModCompats);
    }


    @Override
    public void onInitStep(Step step, ParallelDispatchEvent event) {
        if (step == Step.COMMON_SETUP) {
            prepareModCompats();
        }
        Iterator<IModCompat> it = loadedModCompats.iterator();
        while (it.hasNext()) {
            IModCompat next = it.next();
            try {
                next.onInitStep(step, event);
            } catch (Exception e) {
                LOGGER.error("---------------------------------------------------------", e);
                LOGGER.error("Mod Compat {} threw an exception during {}. Unloading.", next.getModID(), step);
                LOGGER.error("---------------------------------------------------------");
                it.remove();
            }
        }
    }


    private boolean isModLoaded(IModCompat modCompat) {
        return ModList.get().isLoaded(modCompat.getModID());
    }

    private boolean isVersionOk(IModCompat modCompat) {
        Optional<? extends ModContainer> mod = ModList.get().getModContainerById(modCompat.getModID());
        if (mod.isPresent()) {
            String s = modCompat.getAcceptedVersionRange();
            if (s == null) return true;
            VersionRange range;
            try {
                range = VersionRange.createFromVersionSpec(s);
            } catch (InvalidVersionSpecificationException e) {
                LOGGER.error("Invalid version spec {} for {}", s, modCompat.getModID());
                return false;
            }
            return range.containsVersion(mod.get().getModInfo().getVersion());
        }
        return false;
    }

    private void prepareModCompats() {
        if (availableModCompats == null) {
            LOGGER.warn("Trying to load mod compat twice");
            return;
        }


        List<IModCompat> loaded = new LinkedList<>();
        for (IModCompat modCompat : availableModCompats) {
            if (isModLoaded(modCompat)) {
                ForgeConfigSpec.BooleanValue enabled = compatEnableMap.get(modCompat);
                if (enabled != null && enabled.get()) {
                    if (isVersionOk(modCompat)) {
                        loaded.add(modCompat);
                        LOGGER.debug("Prepared {} compatibility", modCompat.getModID());
                    } else {
                        LOGGER.warn("Cannot load {} compat due to incompatible version", modCompat.getModID());
                        incompatibleCompats.add(modCompat);
                    }
                }
            }
        }
        loadedModCompats = loaded;
        availableModCompats = null;
    }

}
