package de.teamlapen.vampirism_integrations;

import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

public interface IInterModeEnqueue {
    void enqueueIMC(InterModEnqueueEvent event);
}
