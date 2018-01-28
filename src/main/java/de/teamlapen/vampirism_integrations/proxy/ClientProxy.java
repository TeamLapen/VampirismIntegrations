package de.teamlapen.vampirism_integrations.proxy;

import de.teamlapen.vampirism_integrations.core.RegistryManager;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {


    public ClientProxy() {
        RegistryManager.setupClientRegistryManager();
    }

    @Override
    public void onInitStep(Step step, FMLStateEvent event) {
        super.onInitStep(step, event);
        RegistryManager.getRegistryManagerClient().onInitStep(step, event);
    }
}
