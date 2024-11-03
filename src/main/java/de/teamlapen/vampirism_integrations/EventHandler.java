package de.teamlapen.vampirism_integrations;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism_integrations.util.IModCompat;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class EventHandler {

    private final static Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        boolean isAdminLikePlayer = !ServerLifecycleHooks.getCurrentServer().isDedicatedServer() || UtilLib.isPlayerOp(event.getEntity());

        if (isAdminLikePlayer && event.getEntity().getRandom().nextInt(4) == 0) {
            List<IModCompat> list = VampirismIntegrationsMod.instance.compatLoader.getIncompatibleCompats();
            for (IModCompat m : list) {
                Optional<? extends ModContainer> mod = ModList.get().getModContainerById(m.getModID());
                mod.ifPresent(modcontainer -> event.getEntity().displayClientMessage(Component.literal(String.format("Could not load Vampirism mod compat for %s because version %s is incompatible (Accepted %s)", m.getModID(), modcontainer.getModInfo().getVersion(), m.getAcceptedVersionRange())), false));
            }
        }


    }
}
