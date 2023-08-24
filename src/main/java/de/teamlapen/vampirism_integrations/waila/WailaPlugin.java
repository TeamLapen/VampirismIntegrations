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

    public static final ResourceLocation SHOW_CREATURE_INFO = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "show_creature_info");
    public static final ResourceLocation SHOW_PLAYER_INFO = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "show_player_info");
    public static final ResourceLocation MAX_BLOOD_ICONS_PER_LINE = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "blood.icon_per_line");
    public static final ResourceLocation MAX_LONG_BLOOD_MAX = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "blood.long_max");

    @Override
    public void register(IRegistrar registrar) {
        registrar.addConfig(SHOW_CREATURE_INFO, true);
        registrar.addConfig(SHOW_PLAYER_INFO, true);
        registrar.addConfig(MAX_LONG_BLOOD_MAX, 100);

        registrar.addComponent(new CreatureDataProvider(), TooltipPosition.BODY, PathfinderMob.class, 975);
        registrar.addComponent(new PlayerDataProvider(), TooltipPosition.BODY, Player.class);
        IBlockComponentProvider tankDataProvider = new TankDataProvider();
        registrar.addComponent(tankDataProvider, TooltipPosition.BODY, AltarInspirationBlockEntity.class);
        registrar.addComponent(tankDataProvider, TooltipPosition.BODY, BloodContainerBlockEntity.class);
        GarlicBeaconProvider garlicBeaconProvider = new GarlicBeaconProvider();
        registrar.addComponent(garlicBeaconProvider, TooltipPosition.BODY, GarlicDiffuserBlockEntity.class);

    }


}
