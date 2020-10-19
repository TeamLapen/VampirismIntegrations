package de.teamlapen.vampirism_integrations;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.lib.lib.util.VersionChecker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
            if (isAdminLikePlayer || event.getPlayer().getRNG().nextInt(5) == 0) {
                if (event.getPlayer().getRNG().nextInt(4) == 0) {
                    VersionChecker.Version newVersion = versionInfo.getNewVersion();
                    event.getPlayer().sendMessage(new TranslationTextComponent("text.vampirism.outdated", versionInfo.getCurrentVersion().name, newVersion.name));
                    ITextComponent download = new TranslationTextComponent("text.vampirism.update_message.download").applyTextStyle(style -> style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, newVersion.getUrl() == null ? versionInfo.getHomePage() : newVersion.getUrl())).setUnderlined(true).setColor(TextFormatting.BLUE));
                    ITextComponent changelog = new TranslationTextComponent("text.vampirism.update_message.changelog").applyTextStyle(style -> style.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vampirism changelog")).setUnderlined(true));
                    ITextComponent modpage = new TranslationTextComponent("text.vampirism.update_message.modpage").applyTextStyle(style -> style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, versionInfo.getHomePage())).setUnderlined(true).setColor(TextFormatting.BLUE));
                    event.getPlayer().sendMessage(download.appendText(" ").appendSibling(changelog).appendText(" ").appendSibling(modpage));
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
