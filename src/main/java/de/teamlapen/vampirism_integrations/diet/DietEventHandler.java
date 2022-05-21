package de.teamlapen.vampirism_integrations.diet;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import top.theillusivec4.diet.api.DietCapability;


public class DietEventHandler {

    private final static String DATA_KEY = "vampirism_integrations_disabled_diet";

    public static void onLevelChanged(PlayerFactionEvent.FactionLevelChanged event) {
        if (DietCompat.disableDiet.get() && event.getCurrentFaction() == VReference.VAMPIRE_FACTION && event.getNewLevel() > 0) {
            DietCapability.get(event.getPlayer().getPlayer()).ifPresent(c -> c.setActive(false));
            event.getPlayer().getPlayer().getPersistentData().putBoolean(DATA_KEY, true);
        } else if (event.getPlayer().getPlayer().getPersistentData().getBoolean(DATA_KEY)) {
            DietCapability.get(event.getPlayer().getPlayer()).ifPresent(c -> c.setActive(true));
            event.getPlayer().getPlayer().getPersistentData().remove(DATA_KEY);
        }
    }
}
