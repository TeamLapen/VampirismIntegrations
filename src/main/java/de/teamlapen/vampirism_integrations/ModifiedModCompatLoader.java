package de.teamlapen.vampirism_integrations;

import de.teamlapen.lib.lib.util.IModCompat;
import de.teamlapen.lib.lib.util.ModCompatLoader;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

/**
 * because {@link de.teamlapen.lib.lib.util.IInitListener.Step} has no Step for {@link InterModEnqueueEvent} create temporary class
 * TODO 1.17 switch to IInitListener.Step
 */
public class ModifiedModCompatLoader extends ModCompatLoader {

    public void enqueueIMC(InterModEnqueueEvent event) {
        for (IModCompat modCompat : getLoadedModCompats()) {
            if (modCompat instanceof IInterModeEnqueue) {
                ((IInterModeEnqueue) modCompat).enqueueIMC(event);
            }
        }
    }
}
