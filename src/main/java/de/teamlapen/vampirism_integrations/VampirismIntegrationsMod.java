package de.teamlapen.vampirism_integrations;

import de.teamlapen.lib.lib.util.IInitListener;
import de.teamlapen.lib.lib.util.Logger;
import de.teamlapen.lib.lib.util.ModCompatLoader;
import de.teamlapen.lib.lib.util.VersionChecker;
import de.teamlapen.vampirism_integrations.compat.VampirismCompat;
import de.teamlapen.vampirism_integrations.compat.abyssalcraft.AbyssalcraftCompat;
import de.teamlapen.vampirism_integrations.compat.bop.BOPCompat;
import de.teamlapen.vampirism_integrations.compat.mca.MCACompat;
import de.teamlapen.vampirism_integrations.compat.toroquest.ToroQuestCompat;
import de.teamlapen.vampirism_integrations.compat.waila.WailaModCompat;
import de.teamlapen.vampirism_integrations.core.ModBlocks;
import de.teamlapen.vampirism_integrations.core.RegistryManager;
import de.teamlapen.vampirism_integrations.proxy.IProxy;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import javax.annotation.Nonnull;

@Mod(modid = REFERENCE.MODID, version = REFERENCE.MODID, dependencies = REFERENCE.DEPENDENCIES, updateJSON = REFERENCE.VERSION_UPDATE_FILE)
public class VampirismIntegrationsMod {
    public static final Logger log = new Logger(REFERENCE.MODID, "de.teamlapen.vampirism_integrations");
    @Mod.Instance
    public static VampirismIntegrationsMod instance;
    public static boolean inDev = false;
    @Nonnull
    public final ModCompatLoader compatLoader;
    private VersionChecker.VersionInfo versionInfo;
    public static final CreativeTabs creativeTab = new CreativeTabs(REFERENCE.MODID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ModBlocks.blood_grinder);
        }
    };
    @SidedProxy(clientSide = "de.teamlapen.vampirism_integrations.proxy.ClientProxy", serverSide = "de.teamlapen.vampirism_integrations.proxy.CommonProxy")
    public static IProxy proxy;
    private final RegistryManager registryManager;


    public VampirismIntegrationsMod() {
        registryManager = new RegistryManager();
        MinecraftForge.EVENT_BUS.register(registryManager);
        compatLoader = new ModCompatLoader(REFERENCE.MODID + ".cfg");
        compatLoader.addModCompat(new MCACompat());
        compatLoader.addModCompat(new BOPCompat());
        compatLoader.addModCompat(new AbyssalcraftCompat());
        compatLoader.addModCompat(new WailaModCompat());
        compatLoader.addModCompat(new ToroQuestCompat());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        checkDevEnv();
        proxy.onInitStep(IInitListener.Step.PRE_INIT, event);
        compatLoader.onInitStep(IInitListener.Step.PRE_INIT, event);
        MinecraftForge.EVENT_BUS.register(new EventHandler());

    }

    public VersionChecker.VersionInfo getVersionInfo() {
        return versionInfo;
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        String currentVersion = "@VERSION@".equals(REFERENCE.VERSION) ? "0.0.0-test" : REFERENCE.VERSION;
        if (VampirismCompat.disableVersionCheck) {
            versionInfo = new VersionChecker.VersionInfo(currentVersion);
        } else {
            versionInfo = VersionChecker.executeVersionCheck(REFERENCE.VERSION_UPDATE_FILE, currentVersion);
        }
        proxy.onInitStep(IInitListener.Step.INIT, event);
        compatLoader.onInitStep(IInitListener.Step.INIT, event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.onInitStep(IInitListener.Step.POST_INIT, event);
        compatLoader.onInitStep(IInitListener.Step.POST_INIT, event);
    }

    @Mod.EventHandler
    public void onServerStart(FMLServerStartingEvent event) {
        event.registerServerCommand(new Command());
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
