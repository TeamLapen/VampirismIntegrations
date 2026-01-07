package de.teamlapen.vampirism_integrations;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.teamlapen.lib.lib.util.IInitListener;
import de.teamlapen.vampirism_integrations.bop.BOPCompat;
import de.teamlapen.vampirism_integrations.coldsweat.ColdSweatCompat;
import de.teamlapen.vampirism_integrations.crafttweaker.CrafttweakerCompat;
import de.teamlapen.vampirism_integrations.ctov.ChoiceTheoremOverhauledVillage;
import de.teamlapen.vampirism_integrations.evilcraft.EvilCraftCompat;
import de.teamlapen.vampirism_integrations.guardvillagers.GuardVillagerCompat;
import de.teamlapen.vampirism_integrations.mca.MCACompat;
import de.teamlapen.vampirism_integrations.survive.SurviveCompat;
import de.teamlapen.vampirism_integrations.tan.TANCompat;
import de.teamlapen.vampirism_integrations.util.IModCompat;
import de.teamlapen.vampirism_integrations.util.ModCompatLoader;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import de.teamlapen.vampirism_integrations.waila.WailaModCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Mod(value = REFERENCE.MODID)
public class VampirismIntegrationsMod {
    public static final Logger LOGGER = LogManager.getLogger();
    public static VampirismIntegrationsMod instance;
    public static boolean inDev = false;
    @Nonnull
    public final ModCompatLoader compatLoader;

    public VampirismIntegrationsMod(IEventBus modbus) {
        instance = this;
        checkDevEnv();
        Optional<? extends ModContainer> opt = ModList.get().getModContainerById(REFERENCE.MODID);
        if (opt.isPresent()) {
            REFERENCE.VERSION = opt.get().getModInfo().getVersion();
        } else {
            LOGGER.warn("Cannot get version from mod info");
        }


        compatLoader = new ModCompatLoader();
        compatLoader.addModCompat(new VampirismCompat());
        compatLoader.addModCompat(new BOPCompat());
        compatLoader.addModCompat(new WailaModCompat());
//      compatLoader.addModCompat(new BloodmagicCompat());
        compatLoader.addModCompat(new EvilCraftCompat());
//        compatLoader.addModCompat(new DietCompat());
//        compatLoader.addModCompat(new TConstructCompat());
        compatLoader.addModCompat(new SurviveCompat());
//        compatLoader.addModCompat(new BetterAnimalsPlusCompat());
//        compatLoader.addModCompat(new BetterAnimalsCompat());
        compatLoader.addModCompat(new CrafttweakerCompat());
//        compatLoader.addModCompat(new GraveyardCompat());
        compatLoader.addModCompat(new TANCompat());
        compatLoader.addModCompat(new MCACompat(modbus));
        compatLoader.addModCompat(new ChoiceTheoremOverhauledVillage());
//        compatLoader.addModCompat(new PlayerCompanionCompat());
//        compatLoader.addModCompat(new ConsecrationCompat());
//        compatLoader.addModCompat(new JadeModCompat());
        compatLoader.addModCompat(new ColdSweatCompat());
        compatLoader.addModCompat(new GuardVillagerCompat());
        modbus.register(this);
        NeoForge.EVENT_BUS.addListener(this::onCommandRegister);
        NeoForge.EVENT_BUS.register(new EventHandler());

    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        compatLoader.onInitStep(IInitListener.Step.CLIENT_SETUP, event);
    }

    @SubscribeEvent
    public void loadComplete(FMLLoadCompleteEvent event) {
        compatLoader.onInitStep(IInitListener.Step.LOAD_COMPLETE, event);
    }

    public void onCommandRegister(RegisterCommandsEvent event) {

        event.getDispatcher().register(LiteralArgumentBuilder.<CommandSourceStack>literal("vampirism-integrations")
                .then(Commands.literal("loaded").executes(context -> {
                    context.getSource().sendSuccess(() -> Component.literal("Loaded and active mods"), false);
                    for (IModCompat compat : compatLoader.getLoadedModCompats()) {
                        ModList.get().getModContainerById(compat.getModID()).ifPresent(container -> context.getSource().sendSuccess(() -> Component.literal("Active: " + compat.getModID() + " Version: " + container.getModInfo().getVersion().getQualifier()), false));
                    }
                    return 0;
                })));

    }

    @SubscribeEvent
    public void enqueueIMC(InterModEnqueueEvent event) {
        compatLoader.onInitStep(IInitListener.Step.ENQUEUE_IMC, event);
    }

    @SubscribeEvent
    public void processIMC(InterModProcessEvent event) {
        compatLoader.onInitStep(IInitListener.Step.PROCESS_IMC, event);
    }

    @SubscribeEvent
    public void registerItems(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            Config.buildConfiguration();
        }
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
        compatLoader.onInitStep(IInitListener.Step.COMMON_SETUP, event);
    }

    private void checkDevEnv() {
        String launchTarget = System.getenv().get("target");
        if (launchTarget != null && launchTarget.contains("dev")) {
            inDev = true;
        }
    }
}
