package de.teamlapen.vampirism_integrations;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.lib.lib.util.VersionChecker;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.List;

public class EventHandler {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        VersionChecker.VersionInfo versionInfo = VampirismIntegrationsMod.instance.getVersionInfo();
        if (!versionInfo.isChecked())
            VampirismIntegrationsMod.log.w("EventHandler", "Version check is not finished yet");
        if (!VampirismCompat.disableVersionCheck && versionInfo.isNewVersionAvailable()) {
            if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer() || UtilLib.isPlayerOp(event.player) || event.player.getRNG().nextInt(5) == 0) {
                if (event.player.getRNG().nextInt(4) == 0) {
                    VersionChecker.Version newVersion = versionInfo.getNewVersion();
                    //Inspired by @Vazikii's useful message
                    event.player.sendMessage(new TextComponentTranslation("text.vampirism.outdated", versionInfo.getCurrentVersion().name, newVersion.name));
                    String template = UtilLib.translate("text.vampirism.update_message");
                    template = template.replaceAll("@download@", newVersion.getUrl() == null ? versionInfo.getHomePage() : newVersion.getUrl()).replaceAll("@forum@", versionInfo.getHomePage());
                    ITextComponent component = ITextComponent.Serializer.jsonToComponent(template);
                    event.player.sendMessage(component);
                }
            }

        }
        if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer() || UtilLib.isPlayerOp(event.player)) {
            if (event.player.getRNG().nextInt(4) == 0) {
                List<IModCompat> list = VampirismIntegrationsMod.instance.compatLoader.getIncompatibleCompats();
                for (IModCompat m : list) {
                    ModContainer mod = Loader.instance().getIndexedModList().get(m.getModID());
                    event.player.sendMessage(new TextComponentString(String.format("Could not load Vampirism mod compat for %s because version %s is incompatible (Accepted %s)", m.getModID(), mod == null ? "Unknown" : mod.getVersion(), m.getAcceptedVersionRange())));
                }
            }
        }

    }
}
