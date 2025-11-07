package de.teamlapen.vampirism_integrations.coldsweat;

import com.momosoftworks.coldsweat.api.util.Temperature;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import de.teamlapen.vampirism.api.util.VResourceLocation;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ColdSweatEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation VAMPIRE_MOD_UUID = VResourceLocation.mod("vampire_modifier");
    private static final String ID = "cold_sweat";
    private static final DeferredHolder<Attribute, Attribute> BURNING_POINT = DeferredHolder.create(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(ID, "burning_point")));
    private static final DeferredHolder<Attribute, Attribute> FREEZING_POINT = DeferredHolder.create(ResourceKey.create(Registries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(ID, "freezing_point")));
    private boolean warnTemperature = true;


    @SubscribeEvent
    public void onFactionLevelChanged(PlayerFactionEvent.FactionLevelChanged event) {
        if (ColdSweatCompat.enableTemperatureVampires.get()) {
            try {
                boolean vamp = event.getCurrentFaction() == VReference.VAMPIRE_FACTION;
                ModifyTemperatureValues(event.getPlayer().asEntity(), vamp);


            } catch (Throwable e) {
                if (warnTemperature) {
                    LOGGER.error("Failed to modify temperature resistance for vampires", e);
                    warnTemperature = false;
                }
            }
        }
    }

    @SubscribeEvent
    public void onRespawn(PlayerEvent.PlayerRespawnEvent event) {
        VanillaEventHandler(event);
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        VanillaEventHandler(event);
    }

    private void VanillaEventHandler(PlayerEvent event) {
        if (ColdSweatCompat.enableTemperatureVampires.get()) {
            try {
                boolean vamp = Helper.isVampire(event.getEntity());
                ModifyTemperatureValues(event.getEntity(), vamp);

            } catch (Throwable e) {
                if (warnTemperature) {
                    LOGGER.error("Failed to modify temperature resistance for vampires (vanilla event)", e);
                    warnTemperature = false;
                }
            }
        }
    }

    private void ModifyTemperatureValues(Player player, boolean vamp) {
        AttributeInstance coldRes = player.getAttribute(FREEZING_POINT);
        if (coldRes != null) {
            if (vamp) {
                if (coldRes.getModifier(VAMPIRE_MOD_UUID) == null) {
                    //Reduce the freezing point for vampires by the configured value in Celsius
                    coldRes.addTransientModifier(new AttributeModifier(VAMPIRE_MOD_UUID, Temperature.convert(-ColdSweatCompat.vampireColdResistance.get(), Temperature.Units.C, Temperature.Units.MC, true), AttributeModifier.Operation.ADD_VALUE));
                }
            } else {
                coldRes.removeModifier(VAMPIRE_MOD_UUID);
            }
        }
        AttributeInstance heatRes = player.getAttribute(BURNING_POINT);
        if (heatRes != null) {
            if (vamp) {
                if (heatRes.getModifier(VAMPIRE_MOD_UUID) == null) {
                    //Scale the burning point by a configured factor. Must subtract one due to attribute modifier logic
                    heatRes.addTransientModifier(new AttributeModifier(VAMPIRE_MOD_UUID, -1 + ColdSweatCompat.vampireBurningPointModifier.get(), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
                }
            } else {
                heatRes.removeModifier(VAMPIRE_MOD_UUID);
            }
        }
    }
}
