package de.teamlapen.vampirism_integrations.jade;

import de.teamlapen.vampirism.api.util.VResourceLocation;
import de.teamlapen.vampirism.blockentity.*;
import de.teamlapen.vampirism.blocks.*;
import de.teamlapen.vampirism_integrations.jade.provider.*;
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

    public static final ResourceLocation ENTITY_BLOOD = VResourceLocation.mod("entity_blood");
    public static final ResourceLocation GARLIC_BEACON = VResourceLocation.mod( "garlic_beacon");
    public static final ResourceLocation PLAYER_FACTION = VResourceLocation.mod( "player_faction");
    public static final ResourceLocation TOTEM = VResourceLocation.mod( "totem");
    public static final ResourceLocation PEDESTAL_CHARGING = VResourceLocation.mod( "pedestal_charging");
    public static final ResourceLocation POTION_TABLE = VResourceLocation.mod( "potion_table");
    public static final ResourceLocation ALCHEMY_TABLE = VResourceLocation.mod( "alchemy_table");
    public static final ResourceLocation WEAPON_TABLE = VResourceLocation.mod( "weapon_table");
    public static final ResourceLocation RESEARCH_TABLE = VResourceLocation.mod( "research_table");
    public static final ResourceLocation ALTAR_OF_INSPIRATION = VResourceLocation.mod( "altar_of_inspiration");
    public static final ResourceLocation ALTAR_OF_INFUSION = VResourceLocation.mod( "altar_of_infusion");
    public static final ResourceLocation ALTAR_PILLAR = VResourceLocation.mod( "altar_pillar");

    public static final ResourceLocation ENTITY_BLOOD_MAX_FOR_RENDER = VResourceLocation.mod( "entity_blood.max_for_render");
    public static final ResourceLocation ENTITY_BLOOD_ICONS_PER_LINE = VResourceLocation.mod( "entity_blood.icon_per_line");
    public static final ResourceLocation ENTITY_BLOOD_SHOW_FRACTION = VResourceLocation.mod( "entity_blood.show_fraction");
    public static final ResourceLocation PLAYER_FACTION_LORD_LEVEL_NUMBER = VResourceLocation.mod( "player_faction.lord_level_number");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(GarlicDiffuserProvider.INSTANCE, GarlicDiffuserBlockEntity.class);
        registration.registerBlockDataProvider(TotemProvider.INSTANCE, TotemBlockEntity.class);
        registration.registerBlockDataProvider(PedestalProvider.INSTANCE, PedestalBlockEntity.class);
        registration.registerBlockDataProvider(AlchemyTableProvider.INSTANCE, AlchemyTableBlockEntity.class);
        registration.registerBlockDataProvider(PotionTableProvider.INSTANCE, PotionTableBlockEntity.class);
        registration.registerEntityDataProvider(PlayerFactionProvider.INSTANCE, Player.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addConfig(ENTITY_BLOOD_MAX_FOR_RENDER, 40, 0,100,false);
        registration.addConfig(ENTITY_BLOOD_ICONS_PER_LINE, 10, 5,300,false);
        registration.addConfig(ENTITY_BLOOD_SHOW_FRACTION, false);
        registration.addConfig(PLAYER_FACTION_LORD_LEVEL_NUMBER, false);
        registration.registerEntityComponent(EntityBloodProvider.INSTANCE, PathfinderMob.class);
        registration.registerEntityComponent(PlayerFactionProvider.INSTANCE, Player.class);
        registration.registerBlockComponent(GarlicDiffuserProvider.INSTANCE, GarlicDiffuserBlock.class);
        registration.registerBlockComponent(TotemProvider.INSTANCE, TotemTopBlock.class);
        registration.registerBlockComponent(PedestalProvider.INSTANCE, PedestalBlock.class);
        registration.registerBlockComponent(AlchemyTableProvider.INSTANCE, AlchemyTableBlock.class);
        registration.registerBlockComponent(PotionTableProvider.INSTANCE, PotionTableBlock.class);
        registration.registerBlockComponent(WeaponTableProvider.INSTANCE, WeaponTableBlock.class);
        registration.registerBlockComponent(AltarOfInspirationProvider.INSTANCE, AltarInspirationBlock.class);
        registration.registerBlockComponent(AltarOfInfusionProvider.INSTANCE, AltarInfusionBlock.class);
        registration.registerBlockComponent(ResearchTableProvider.INSTANCE, HunterTableBlock.class);
        registration.registerBlockComponent(AltarPillarProvider.INSTANCE, AltarPillarBlock.class);
        registration.markAsClientFeature(ENTITY_BLOOD_MAX_FOR_RENDER);
        registration.markAsClientFeature(ENTITY_BLOOD_ICONS_PER_LINE);
        registration.markAsClientFeature(ENTITY_BLOOD_SHOW_FRACTION);
        registration.markAsClientFeature(PLAYER_FACTION_LORD_LEVEL_NUMBER);
        registration.markAsClientFeature(ENTITY_BLOOD);
        registration.markAsClientFeature(WEAPON_TABLE);
        registration.markAsClientFeature(ALTAR_OF_INSPIRATION);
        registration.markAsClientFeature(ALTAR_OF_INFUSION);
        registration.markAsClientFeature(RESEARCH_TABLE);
        registration.markAsClientFeature(ALTAR_PILLAR);
    }
}
