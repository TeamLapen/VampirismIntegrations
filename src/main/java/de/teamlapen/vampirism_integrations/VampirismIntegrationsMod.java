package de.teamlapen.vampirism_integrations;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.teamlapen.lib.lib.util.IInitListener;
import de.teamlapen.lib.lib.util.VersionChecker;
import de.teamlapen.vampirism_integrations.betteranimals.BetterAnimalsCompat;
import de.teamlapen.vampirism_integrations.bop.BOPCompat;
import de.teamlapen.vampirism_integrations.ctov.ChoiceTheoremOverhauledVillage;
import de.teamlapen.vampirism_integrations.evilcraft.EvilCraftCompat;
import de.teamlapen.vampirism_integrations.graveyard.GraveyardCompat;
import de.teamlapen.vampirism_integrations.mca.MCACompat;
import de.teamlapen.vampirism_integrations.player_companion.PlayerCompanionCompat;
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
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
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
    private VersionChecker.VersionInfo versionInfo;

    public VampirismIntegrationsMod() {
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
//        compatLoader.addModCompat(new BloodmagicCompat());
        compatLoader.addModCompat(new EvilCraftCompat());
//        compatLoader.addModCompat(new DietCompat());
//        compatLoader.addModCompat(new TConstructCompat());
        compatLoader.addModCompat(new SurviveCompat());
//        compatLoader.addModCompat(new BetterAnimalsPlusCompat());
        compatLoader.addModCompat(new BetterAnimalsCompat());
//        compatLoader.addModCompat(new CrafttweakerCompat());
        compatLoader.addModCompat(new GraveyardCompat());
        compatLoader.addModCompat(new TANCompat());
        compatLoader.addModCompat(new MCACompat());
        compatLoader.addModCompat(new ChoiceTheoremOverhauledVillage());
        compatLoader.addModCompat(new PlayerCompanionCompat());
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);
        MinecraftForge.EVENT_BUS.register(new EventHandler());

    }

    public VersionChecker.VersionInfo getVersionInfo() {
        return versionInfo;
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
                    context.getSource().sendSuccess(Component.literal("Loaded and active mods"), false);
                    for (IModCompat compat : compatLoader.getLoadedModCompats()) {
                        ModList.get().getModContainerById(compat.getModID()).ifPresent(container -> context.getSource().sendSuccess(Component.literal("Active: " + compat.getModID() + " Version: " + container.getModInfo().getVersion().getQualifier()), false));
                    }
                    return 0;
                }))
                .then(Commands.literal("changelog").executes(context -> {
                    if (!getVersionInfo().isNewVersionAvailable()) {
                        context.getSource().sendSuccess(Component.translatable("command.vampirism.base.changelog.newversion"), false);
                        return 0;
                    }
                    VersionChecker.Version newVersion = getVersionInfo().getNewVersion();
                    List<String> changes = newVersion.getChanges();
                    context.getSource().sendSuccess(Component.literal(ChatFormatting.GREEN + "Vampirism Integrations" + newVersion.name + "(" + SharedConstants.getCurrentVersion().getName() + ")"), true);
                    for (String c : changes) {
                        context.getSource().sendSuccess(Component.literal("-" + c), false);
                    }
                    context.getSource().sendSuccess(Component.literal(""), false);
                    String homepage = getVersionInfo().getHomePage();

                    MutableComponent download = Component.translatable("text.vampirism.update_message.download").withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, newVersion.getUrl() == null ? homepage : newVersion.getUrl())).withUnderlined(true).withColor(ChatFormatting.BLUE));
                    Component changelog = Component.translatable("text.vampirism.update_message.changelog").withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vampirism-integrations changelog")).withUnderlined(true));
                    Component modpage = Component.translatable("text.vampirism.update_message.modpage").withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, homepage)).withUnderlined(true).withColor(ChatFormatting.BLUE));
                    context.getSource().sendSuccess(download.append(" ").append(changelog).append(" ").append(modpage), false);
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
        if (event.getRegistryKey().equals(ForgeRegistries.ITEMS.getRegistryKey())) {
            Config.buildConfiguration();
        }
    }

    @SubscribeEvent
    public void setup(FMLCommonSetupEvent event) {
        compatLoader.onInitStep(IInitListener.Step.COMMON_SETUP, event);
        if (VampirismCompat.disableVersionCheck.get()) {
            versionInfo = new VersionChecker.VersionInfo(REFERENCE.VERSION);
        } else {
            versionInfo = VersionChecker.executeVersionCheck(REFERENCE.VERSION_UPDATE_FILE, REFERENCE.VERSION, false);
        }
    }

    private void checkDevEnv() {
        String launchTarget = System.getenv().get("target");
        if (launchTarget != null && launchTarget.contains("dev")) {
            inDev = true;
        }
    }
}
