package de.teamlapen.vampirism_integrations.coldsweat;

import com.momosoftworks.coldsweat.api.util.Temperature;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.event.PlayerFactionEvent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;


public class ColdSweatEventHandler {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final UUID VAMPIRE_MOD_UUID = UUID.fromString("648f1040-d104-4269-bcdd-161ff12dcb5e");
    private static final String ID = "cold_sweat";
    private static final RegistryObject<Attribute> BURNING_POINT = RegistryObject.create(new ResourceLocation(ID, "burning_point"), Registries.ATTRIBUTE, ID);
    private static final RegistryObject<Attribute> FREEZING_POINT = RegistryObject.create(new ResourceLocation(ID, "freezing_point"), Registries.ATTRIBUTE, ID);
    private boolean warnTemperature = true;


    @SubscribeEvent
    public void onFactionLevelChanged(PlayerFactionEvent.FactionLevelChanged event) {
        if (ColdSweatCompat.enableTemperatureVampires.get()) {
            try {
                boolean vamp = event.getCurrentFaction() == VReference.VAMPIRE_FACTION;
                AttributeInstance coldRes = event.getPlayer().getPlayer().getAttribute(FREEZING_POINT.get());
                if (coldRes != null) {
                    if (vamp) {
                        if (coldRes.getModifier(VAMPIRE_MOD_UUID) == null) {
                            coldRes.addTransientModifier(new AttributeModifier(VAMPIRE_MOD_UUID, "vampire", Temperature.convertUnits(ColdSweatCompat.vampireColdResistance.get(), Temperature.Units.C, Temperature.Units.MC, true), AttributeModifier.Operation.ADDITION));
                        }
                    } else {
                        coldRes.removeModifier(VAMPIRE_MOD_UUID);
                    }
                }
                AttributeInstance heatRes = event.getPlayer().getPlayer().getAttribute(BURNING_POINT.get());
                if (heatRes != null) {
                    if (vamp) {
                        if (heatRes.getModifier(VAMPIRE_MOD_UUID) == null) {
                            heatRes.addTransientModifier(new AttributeModifier(VAMPIRE_MOD_UUID, "vampire", - 1 + ColdSweatCompat.vampireBurningPointModifier.get(), AttributeModifier.Operation.MULTIPLY_TOTAL));
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
}
