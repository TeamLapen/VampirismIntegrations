package de.teamlapen.vampirism_integrations.jade;

import de.teamlapen.vampirism.blockentity.GarlicDiffuserBlockEntity;
import de.teamlapen.vampirism.blocks.GarlicDiffuserBlock;
import de.teamlapen.vampirism_integrations.jade.provider.EntityBloodProvider;
import de.teamlapen.vampirism_integrations.jade.provider.GarlicDiffuserProvider;
import de.teamlapen.vampirism_integrations.jade.provider.PlayerFactionProvider;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    public static final ResourceLocation ENTITY_BLOOD = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "entity_blood");
    public static final ResourceLocation GARLIC_BEACON = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "garlic_beacon");
    public static final ResourceLocation PLAYER_FACTION = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "player_faction");
    public static final ResourceLocation TOTEM = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "totem");

    public static final ResourceLocation ENTITY_BLOOD_MAX_FOR_RENDER = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "entity_blood.max_for_render");
    public static final ResourceLocation ENTITY_BLOOD_ICONS_PER_LINE = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "entity_blood.icon_per_line");
    public static final ResourceLocation ENTITY_BLOOD_SHOW_FRACTION = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "entity_blood.show_fraction");
    public static final ResourceLocation PLAYER_FACTION_LORD_LEVEL_NUMBER = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "player_faction.lord_level_number");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(GarlicDiffuserProvider.INSTANCE, GarlicDiffuserBlockEntity.class);
        registration.registerEntityDataProvider(PlayerFactionProvider.INSTANCE, Player.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addConfig(ENTITY_BLOOD_MAX_FOR_RENDER, 40, 0,100,false);
        registration.addConfig(ENTITY_BLOOD_ICONS_PER_LINE, 10, 5,300,false);
        registration.addConfig(ENTITY_BLOOD_SHOW_FRACTION, false);
        registration.addConfig(PLAYER_FACTION_LORD_LEVEL_NUMBER, false);
        registration.registerEntityComponent(EntityBloodProvider.INSTANCE, PathfinderMob.class);
        registration.registerBlockComponent(GarlicDiffuserProvider.INSTANCE, GarlicDiffuserBlock.class);
        registration.registerEntityComponent(PlayerFactionProvider.INSTANCE, Player.class);
        registration.markAsClientFeature(ENTITY_BLOOD_MAX_FOR_RENDER);
        registration.markAsClientFeature(ENTITY_BLOOD_ICONS_PER_LINE);
        registration.markAsClientFeature(ENTITY_BLOOD_SHOW_FRACTION);
        registration.markAsClientFeature(PLAYER_FACTION_LORD_LEVEL_NUMBER);
    }
}
