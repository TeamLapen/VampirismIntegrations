package de.teamlapen.vampirism_integrations.waila;


import de.teamlapen.vampirism.blockentity.AltarInspirationBlockEntity;
import de.teamlapen.vampirism.blockentity.BloodContainerBlockEntity;
import de.teamlapen.vampirism.blockentity.GarlicDiffuserBlockEntity;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;

public class WailaPlugin implements IWailaPlugin {

    private final static ResourceLocation showCreatureInfo = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "show_creature_info");
    private final static ResourceLocation showPlayerInfo = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "show_player_info");

    static ResourceLocation getShowCreatureInfoConf() {
        return showCreatureInfo;
    }

    static ResourceLocation getShowPlayerInfoConf() {
        return showPlayerInfo;
    }

    @Override
    public void register(IRegistrar registrar) {
        registrar.addConfig(getShowCreatureInfoConf(), true);
        registrar.addConfig(getShowPlayerInfoConf(), true);

        registrar.addComponent(new CreatureDataProvider(), TooltipPosition.BODY, PathfinderMob.class);
        registrar.addComponent(new PlayerDataProvider(), TooltipPosition.BODY, Player.class);
        IBlockComponentProvider tankDataProvider = new TankDataProvider();
        registrar.addComponent(tankDataProvider, TooltipPosition.BODY, AltarInspirationBlockEntity.class);
        registrar.addComponent(tankDataProvider, TooltipPosition.BODY, BloodContainerBlockEntity.class);
        GarlicBeaconProvider garlicBeaconProvider = new GarlicBeaconProvider();
        registrar.addComponent(garlicBeaconProvider, TooltipPosition.BODY, GarlicDiffuserBlockEntity.class);

    }


}
