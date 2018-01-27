package de.teamlapen.vampirism_integrations.compat.toroquest;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.torocraft.toroquest.entities.EntityGuard;
import net.torocraft.toroquest.entities.EntitySentry;

public class ToroQuestAIHandler {


    private static void addAttackVampire(EntityCreature entity) {
        int prio = -1;
        for (EntityAITasks.EntityAITaskEntry task : entity.targetTasks.taskEntries) {
            if (task.action.getClass().equals(EntityAINearestAttackableTarget.class)) {
                prio = task.priority;
                break;
            }
        }
        if (prio != -1) {
            entity.targetTasks.addTask(prio, new EntityAINearestAttackableTarget<>(entity, EntityCreature.class, 10, true, true, VampirismAPI.factionRegistry().getPredicate(VReference.HUNTER_FACTION, false, true, false, false, VReference.VAMPIRE_FACTION)));
        }
    }

    @SubscribeEvent
    public void onEntityCreated(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityGuard || event.getEntity() instanceof EntitySentry) {
            addAttackVampire((EntityCreature) event.getEntity());
        }
    }
}
