package de.teamlapen.vampirism_integrations;

import de.teamlapen.lib.lib.util.IModCompat;
import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.lib.lib.util.VersionChecker;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
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

        boolean isAdminLikePlayer = !ServerLifecycleHooks.getCurrentServer().isDedicatedServer() || UtilLib.isPlayerOp(event.getPlayer());

        if (!VampirismCompat.disableVersionCheck.get() && versionInfo.isNewVersionAvailable()) {

            if (isAdminLikePlayer || event.getPlayer().getRandom().nextInt(5) == 0) {
                if (event.getPlayer().getRandom().nextInt(4) == 0) {
                    VersionChecker.Version newVersion = versionInfo.getNewVersion();
                    event.getPlayer().displayClientMessage(new TranslationTextComponent("text.vampirism.outdated", versionInfo.getCurrentVersion().name, newVersion.name), false);
                    IFormattableTextComponent download = new TranslationTextComponent("text.vampirism.update_message.download").withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, newVersion.getUrl() == null ? versionInfo.getHomePage() : newVersion.getUrl())).setUnderlined(true).withColor(TextFormatting.BLUE));
                    ITextComponent changelog = new TranslationTextComponent("text.vampirism.update_message.changelog").withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vampirism changelog")).setUnderlined(true));
                    ITextComponent modpage = new TranslationTextComponent("text.vampirism.update_message.modpage").withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, versionInfo.getHomePage())).setUnderlined(true).withColor(TextFormatting.BLUE));
                    event.getPlayer().displayClientMessage(download.append(" ").append(changelog).append(" ").append(modpage), false);
                }
            }


        }
        if (isAdminLikePlayer && event.getPlayer().getRandom().nextInt(4) == 0) {
            List<IModCompat> list = VampirismIntegrationsMod.instance.compatLoader.getIncompatibleCompats();
            for (IModCompat m : list) {
                Optional<? extends ModContainer> mod = ModList.get().getModContainerById(m.getModID());
                mod.ifPresent(modcontainer -> event.getPlayer().displayClientMessage(new StringTextComponent(String.format("Could not load Vampirism mod compat for %s because version %s is incompatible (Accepted %s)", m.getModID(), modcontainer.getModInfo().getVersion(), m.getAcceptedVersionRange())), false));
            }
        }


    }
}
