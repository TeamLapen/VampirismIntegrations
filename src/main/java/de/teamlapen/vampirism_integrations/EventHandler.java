package de.teamlapen.vampirism_integrations;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.lib.lib.util.VersionChecker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class EventHandler {

    private final static Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        VersionChecker.VersionInfo versionInfo = VampirismIntegrationsMod.instance.getVersionInfo();
        if (!versionInfo.isChecked())
            LOGGER.warn("Version check is not finished yet");
        if (!VampirismCompat.disableVersionCheck.get() && versionInfo.isNewVersionAvailable()) {
            if (!ServerLifecycleHooks.getCurrentServer().isDedicatedServer() || UtilLib.isPlayerOp(event.getPlayer()) || event.getPlayer().getRNG().nextInt(5) == 0) {
                if (event.getPlayer().getRNG().nextInt(4) == 0) {
                    VersionChecker.Version newVersion = versionInfo.getNewVersion();
                    //Inspired by @Vazikii's useful message
                    event.getPlayer().sendMessage(new TranslationTextComponent("text.vampirism.outdated", versionInfo.getCurrentVersion().name, newVersion.name));
                    String template = UtilLib.translate("text.vampirism.update_message");
                    template = template.replaceAll("@download@", newVersion.getUrl() == null ? versionInfo.getHomePage() : newVersion.getUrl()).replaceAll("@forum@", versionInfo.getHomePage());
                    ITextComponent component = ITextComponent.Serializer.fromJson(template);
                    if (component != null) {
                        event.getPlayer().sendMessage(component);
                    }
                }
            }

        }
        if (!ServerLifecycleHooks.getCurrentServer().isDedicatedServer() || UtilLib.isPlayerOp(event.getPlayer())) {
            if (event.getPlayer().getRNG().nextInt(4) == 0) {
                List<IModCompat> list = VampirismIntegrationsMod.instance.compatLoader.getIncompatibleCompats();
                for (IModCompat m : list) {
                    Optional<? extends ModContainer> mod = ModList.get().getModContainerById(m.getModID());
                    mod.ifPresent(modcontainer -> event.getPlayer().sendMessage(new StringTextComponent(String.format("Could not load Vampirism mod compat for %s because version %s is incompatible (Accepted %s)", m.getModID(), modcontainer.getModInfo().getVersion(), m.getAcceptedVersionRange()))));
                }
            }
        }

    }
}
