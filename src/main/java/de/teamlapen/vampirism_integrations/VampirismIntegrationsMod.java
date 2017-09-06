package de.teamlapen.vampirism_integrations;

import de.teamlapen.lib.lib.util.IInitListener;
import de.teamlapen.lib.lib.util.Logger;
import de.teamlapen.lib.lib.util.ModCompatLoader;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = REFERENCE.MODID, version = REFERENCE.MODID, dependencies = "required-after:forge@[" + REFERENCE.FORGE_VERSION_MIN + ",);required-after:vampirism@[" + REFERENCE.VAMPIRISM_VERSION_MIN + ",];after:guideapi")
public class VampirismIntegrationsMod {
    public static final Logger log = new Logger(REFERENCE.MODID, "de.teamlapen.vampirism_integrations");
    @Mod.Instance
    public static VampirismIntegrationsMod instance;
    public static boolean inDev = false;

    public final ModCompatLoader compatLoader;

    public VampirismIntegrationsMod() {
        compatLoader = new ModCompatLoader(REFERENCE.MODID + ".cfg");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        checkDevEnv();
        compatLoader.onInitStep(IInitListener.Step.PRE_INIT, event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        compatLoader.onInitStep(IInitListener.Step.INIT, event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        compatLoader.onInitStep(IInitListener.Step.POST_INIT, event);
    }

    private void checkDevEnv() {
        if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            inDev = true;
            log.setDebug(true);
            if (FMLCommonHandler.instance().getSide().isClient()) {
                log.displayModID();
            }
        }
    }
}
