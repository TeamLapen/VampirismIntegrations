package de.teamlapen.vampirism_integrations.survive;


import com.stereowalker.survive.needs.IRealisticEntity;
import com.stereowalker.survive.needs.StaminaData;
import com.stereowalker.survive.needs.WaterData;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import de.teamlapen.vampirism.api.util.VResourceLocation;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;


public class SurviveHandler {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ID = "survive";
    private final static ResourceLocation VAMPIRE_MOD_UUID = VResourceLocation.mod("vampire_modifier");
    private static final ResourceLocation THIRST_OVERLAY = ResourceLocation.fromNamespaceAndPath(ID, "thirst_level");
    private static final ResourceLocation STAMINA_OVERLAY = ResourceLocation.fromNamespaceAndPath(ID, "stamina_level");
    private static final DeferredHolder<Attribute, Attribute> COLD_RESISTANCE = DeferredHolder.create(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(ID, "generic.cold_resistance")));
    private static final DeferredHolder<Attribute, Attribute> HEAT_RESISTANCE = DeferredHolder.create(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(ID, "generic.heat_resistance")));
    private boolean warnThirst = true;
    private boolean warnTemperature = true;
    private boolean warnStamina = true;

    @SubscribeEvent
    public void onFactionLevelChanged(PlayerFactionEvent.FactionLevelChanged event) {
        if (SurviveCompat.enableTemperatureVampires.get()) {
            try {
                    boolean vamp = event.getCurrentFaction() == VReference.VAMPIRE_FACTION;
                    AttributeInstance coldRes = event.getPlayer().getPlayer().getAttribute(COLD_RESISTANCE);
                    if (coldRes != null) {
                        if (vamp) {
                            if (coldRes.getModifier(VAMPIRE_MOD_UUID) == null) {
                                coldRes.addTransientModifier(new AttributeModifier(VAMPIRE_MOD_UUID, 20, AttributeModifier.Operation.ADD_VALUE));
                            }
                        } else {
                            coldRes.removeModifier(VAMPIRE_MOD_UUID);
                        }
                    }
                    AttributeInstance heatRes = event.getPlayer().getPlayer().getAttribute(HEAT_RESISTANCE);
                    if (heatRes != null) {
                        if (vamp) {
                            if (heatRes.getModifier(VAMPIRE_MOD_UUID) == null) {
                                heatRes.addTransientModifier(new AttributeModifier(VAMPIRE_MOD_UUID,  -0.3, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                            }
                        } else {
                            heatRes.removeModifier(VAMPIRE_MOD_UUID);
                        }
                    }


            } catch (Throwable e) {
                if (warnTemperature) {
                    LOGGER.error("Failed to modify temperature resistance for vampires", e);
                    warnTemperature = false;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(EntityTickEvent event) {
        if (SurviveCompat.disableThirstForVampires.get() && event.getEntity() instanceof ServerPlayer player && player instanceof IRealisticEntity realisticEntity) {
            try {
                if (Helper.isVampire(player)) {
                    WaterData stats = realisticEntity.waterData();
                    if (stats.needWater() && stats.getWaterLevel() < 20) {
                        stats.setWaterLevel(stats.getWaterLevel() + 1);
                    }
                }
            } catch (Throwable e) {
                if (warnThirst) {
                    LOGGER.error("Failed to disable thirst for vampire", e);
                    warnThirst = false;
                }
            }
        }
        if (SurviveCompat.enableStaminaBoostVampires.get() && event.getEntity() instanceof ServerPlayer player && player instanceof IRealisticEntity realisticEntity) {
            try {
                if (Helper.isVampire(player)) {
                    if (event.getEntity().tickCount % 64 == 0) {
                        StaminaData stats = realisticEntity.staminaData();
                        if (stats.isTired()) {
                            stats.setEnergyLevel(stats.getLTS() + 1); //Since we are tired, we should be below maxStamina
                        }
                    }

                }
            } catch (Throwable e) {
                if (warnStamina) {
                    LOGGER.error("Failed to grant stamina boost for vampire", e);
                    warnStamina = false;
                }
            }
        }

    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGuiLayerEvent.Pre event) {
        if (Minecraft.getInstance().player == null) return;
        if (event.getName().equals(THIRST_OVERLAY) && SurviveCompat.disableThirstForVampires.get() && Helper.isVampire(Minecraft.getInstance().player)) {
            event.setCanceled(true);
        }
        if (event.getName().equals(STAMINA_OVERLAY) && SurviveCompat.enableStaminaBoostVampires.get() && Helper.isVampire(Minecraft.getInstance().player)) {
            event.setCanceled(true);
        }
    }
}
