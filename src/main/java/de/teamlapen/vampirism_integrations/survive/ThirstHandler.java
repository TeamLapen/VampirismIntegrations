package de.teamlapen.vampirism_integrations.survive;


import com.stereowalker.survive.entity.SurviveEntityStats;
import com.stereowalker.survive.entity.ai.SAttributes;
import com.stereowalker.survive.util.EnergyStats;
import com.stereowalker.survive.util.WaterStats;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.event.FactionEvent;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;


public class ThirstHandler {

    private static final Logger LOGGER = LogManager.getLogger();
    private final static UUID VAMPIRE_MOD_UUID = UUID.fromString("3625f8a1-3ac3-4289-8c26-b50ddccf066c");
    private boolean warnThirst = true;
    private boolean warnTemperature = true;
    private boolean warnStamina = true;

    @SubscribeEvent
    public void onFactionLevelChanged(FactionEvent.FactionLevelChanged event) {
        if (SurviveCompat.enableTemperatureVampires.get()) {
            try {
                boolean vamp = event.getCurrentFaction() == VReference.VAMPIRE_FACTION;
                ModifiableAttributeInstance coldRes = event.getPlayer().getPlayer().getAttribute(SAttributes.COLD_RESISTANCE);
                if (coldRes != null) {
                    if (vamp) {
                        if (coldRes.getModifier(VAMPIRE_MOD_UUID) == null) {
                            coldRes.applyNonPersistentModifier(new AttributeModifier(VAMPIRE_MOD_UUID, "vampire", 20, AttributeModifier.Operation.ADDITION));
                        }
                    } else {
                        coldRes.removeModifier(VAMPIRE_MOD_UUID);
                    }
                }
                ModifiableAttributeInstance heatRes = event.getPlayer().getPlayer().getAttribute(SAttributes.HEAT_RESISTANCE);
                if (heatRes != null) {
                    if (vamp) {
                        if (heatRes.getModifier(VAMPIRE_MOD_UUID) == null) {
                            heatRes.applyNonPersistentModifier(new AttributeModifier(VAMPIRE_MOD_UUID, "vampire", -0.3, AttributeModifier.Operation.MULTIPLY_TOTAL));
                        }
                    } else {
                        heatRes.removeModifier(VAMPIRE_MOD_UUID);
                    }
                }
            } catch (Exception e) {
                if (warnTemperature) {
                    LOGGER.error("Failed to modifiy temperature resistance for vampires", e);
                    warnTemperature = false;
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (SurviveCompat.disableThirstForVampires.get() && event.getEntity() instanceof ServerPlayerEntity) {
            try {
                if (Helper.isVampire((PlayerEntity) event.getEntity())) {
                    WaterStats stats = SurviveEntityStats.getWaterStats(event.getEntityLiving());
                    if (stats.needWater()) {
                        stats.setWaterLevel(stats.getWaterLevel() + 1);
                        SurviveEntityStats.setWaterStats(event.getEntityLiving(), stats);
                    }
                }
            } catch (Exception e) {
                if (warnThirst) {
                    LOGGER.error("Failed to disable thirst for vampire", e);
                    warnThirst = false;
                }
            }
        }
        if (SurviveCompat.enableStaminaBoostVampires.get() && event.getEntity() instanceof ServerPlayerEntity) {
            try {
                if (Helper.isVampire((PlayerEntity) event.getEntity())) {
                    if (event.getEntity().ticksExisted % 64 == 0) {
                        EnergyStats stats = SurviveEntityStats.getEnergyStats(event.getEntityLiving());
                        if (stats.isTired()) {
                            stats.addStats(1);
                            SurviveEntityStats.setEnergyStats(event.getEntityLiving(), stats);
                        }
                    }

                }
            } catch (Exception e) {
                if (warnStamina) {
                    LOGGER.error("Failed to grant stamina boost for vampire", e);
                    warnStamina = false;
                }
            }
        }

    }
}
