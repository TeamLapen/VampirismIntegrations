package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.vampirism.tileentity.AltarInspirationTileEntity;
import de.teamlapen.vampirism.tileentity.BloodContainerTileEntity;
import de.teamlapen.vampirism.tileentity.GarlicBeaconTileEntity;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

@mcp.mobius.waila.api.WailaPlugin
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

        registrar.registerComponentProvider(new CreatureDataProvider(), TooltipPosition.BODY, CreatureEntity.class);
        registrar.registerComponentProvider(new PlayerDataProvider(), TooltipPosition.BODY, PlayerEntity.class);
        IComponentProvider tankDataProvider = new TankDataProvider();
        registrar.registerComponentProvider(tankDataProvider, TooltipPosition.BODY, AltarInspirationTileEntity.class);
        registrar.registerComponentProvider(tankDataProvider, TooltipPosition.BODY, BloodContainerTileEntity.class);
        GarlicBeaconProvider garlicBeaconProvider = new GarlicBeaconProvider();
        registrar.registerComponentProvider(garlicBeaconProvider, TooltipPosition.BODY, GarlicBeaconTileEntity.class);
    }


}
