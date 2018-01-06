package de.teamlapen.vampirism_integrations.mca;

import com.google.common.base.Predicates;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import mca.actions.AbstractAction;
import mca.actions.ActionSleep;
import mca.core.Constants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;

import javax.annotation.Nullable;
import java.util.List;


/**
 * Similar as ActionDefend but (only) against vampires
 */
class ActionDefendAgainstVampire extends AbstractAction {
    private static final int TARGET_SEARCH_INTERVAL = 20;
    private final EntityAngryVillagerMCA actorVampirism;
    @Nullable
    private EntityLivingBase target;
    private int timeUntilTargetSearch;

    public ActionDefendAgainstVampire(EntityAngryVillagerMCA actor) {
        super(actor);
        this.actorVampirism = actor;
    }

    @Override
    public void onUpdateServer() {
        if (actor.getBehavior(ActionSleep.class).getIsSleeping()) {
            return;
        }

        if (actor instanceof EntityAngryVillagerMCA) {
            if (target == null) {
                if (timeUntilTargetSearch <= 0) {
                    tryAssignTarget();
                    timeUntilTargetSearch = TARGET_SEARCH_INTERVAL;
                } else {
                    timeUntilTargetSearch--;
                }
            } else {
                double distanceToTarget = actor.getDistanceSq(target);

                if (target.isDead || distanceToTarget >= 15.0D) {
                    reset();
                    return;
                }


                if (distanceToTarget <= 2.0F) {
                    actorVampirism.attackEntityAsMob(target);
                } else if (distanceToTarget > 2.0F && actor.getNavigator().noPath()) {
                    actor.getNavigator().tryMoveToEntityLiving(target, Constants.SPEED_WALK);
                }

            }
        }
    }

    @Override
    public void reset() {
        target = null;
    }

    private void tryAssignTarget() {
        List<Entity> possibleTargets = actor.getEntityWorld().getEntitiesInAABBexcluding(actor, actor.getEntityBoundingBox().grow(15, 5, 15), Predicates.or(VampirismAPI.factionRegistry().getPredicate(VReference.HUNTER_FACTION, true, true, false, false, VReference.VAMPIRE_FACTION), IMob.VISIBLE_MOB_SELECTOR));
        double closestDistance = 100.0D;

        for (Entity entity : possibleTargets) {
            if (actor.canEntityBeSeen(entity)) {
                double distance = entity.getDistanceSq(actor);

                if (distance < closestDistance) {
                    closestDistance = distance;
                    target = (EntityLivingBase) entity;
                }
            }
        }
    }
}
