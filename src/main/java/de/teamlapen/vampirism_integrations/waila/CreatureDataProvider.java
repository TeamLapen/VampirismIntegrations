package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.IExtendedCreatureVampirism;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Provide data for creatures
 */
class CreatureDataProvider implements IWailaEntityProvider {


    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if (config.getConfig(WailaHandler.getShowCreatureInfoConf())) {
            if (entity instanceof EntityCreature && VReference.VAMPIRE_FACTION.getPlayerCapability(accessor.getPlayer()).getLevel() > 0) {
                IExtendedCreatureVampirism extendedCreature = VampirismAPI.getExtendedCreatureVampirism((EntityCreature) entity);
                int blood = extendedCreature.getBlood();
                if (extendedCreature.hasPoisonousBlood()) {
                    currenttip.add(UtilLib.translate("text.vampirism_integrations.poisonous"));
                } else if (blood > 0) {
                    currenttip.add(String.format("%s%s: %d", TextFormatting.RED, UtilLib.translate("text.vampirism.entitysblood"), blood));
                }

            }
        }
        return currenttip;
    }


}