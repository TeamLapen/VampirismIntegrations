package de.teamlapen.vampirism_integrations;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import net.minecraft.world.entity.player.Player;

public class Helper {

    public static boolean isVampire(Player player) {
        return VampirismAPI.factionPlayerHandler(player).isInFaction(VReference.VAMPIRE_FACTION);
    }

    public static boolean isHunter(Player player) {
        return VampirismAPI.factionPlayerHandler(player).isInFaction(VReference.HUNTER_FACTION);
    }
}
