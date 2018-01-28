package de.teamlapen.vampirism_integrations.client;

import de.teamlapen.lib.lib.util.IInitListener;
import de.teamlapen.vampirism_integrations.client.render.ModBlocksRender;
import de.teamlapen.vampirism_integrations.client.render.ModItemsRender;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RegistryManagerClient implements IInitListener {
    @Override
    public void onInitStep(Step step, FMLStateEvent event) {

    }

    @SubscribeEvent
    public void onRegisterModels(ModelRegistryEvent event) {
        ModBlocksRender.register();
        ModItemsRender.register();
    }
}
