package de.teamlapen.vampirism_integrations;

import de.teamlapen.lib.lib.util.IModCompat;
import de.teamlapen.lib.lib.util.ModCompatLoader;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

public class ModifiedModCompatLoader extends ModCompatLoader {

    public void enqueueIMC(InterModEnqueueEvent event) {
        for (IModCompat modCompat : getLoadedModCompats()) {
            if (modCompat instanceof IInterModeEnqueue) {
                ((IInterModeEnqueue) modCompat).enqueueIMC(event);
            }
        }
    }
}
