package de.teamlapen.vampirism_integrations.mca;


import com.google.common.collect.Lists;
import de.teamlapen.vampirism.api.entity.IExtendedCreatureVampirism;
import de.teamlapen.vampirism.core.ModSounds;
import de.teamlapen.vampirism.entity.ExtendedCreature;
import de.teamlapen.vampirism_integrations.VampirismIntegrationsMod;
import mca.actions.AbstractAction;
import mca.actions.ActionSleep;
import mca.core.Constants;
import mca.enums.EnumSleepingState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.nbt.NBTTagCompound;
import radixcore.math.Point3D;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

/**
 * Action handling moving to biteable, biting and moving back. If during the night.
 * Changes sleep state to interrupted during the activity and to awake once finished
 */
class ActionSuckBlood extends AbstractAction {
    private final EntityConvertedVillagerMCA actorVampirism;
    private final List<Entity> blackList = Lists.newArrayList();
    private boolean hunt;
    private int biting;
    private boolean hungry = true;
    private int walkingHome = 0;
    @Nullable
    private IExtendedCreatureVampirism target;

    public ActionSuckBlood(EntityConvertedVillagerMCA actor) {
        super(actor);
        this.actorVampirism = actor;
        hunt = false;
    }

    @Override
    public void onUpdateServer() {
        boolean day = actorVampirism.getEntityWorld().isDaytime();
        if (day && !hungry) hungry = true;
        if (!day && hungry) {
            ActionSleep sleep = actor.getBehavior(ActionSleep.class);
            if (sleep.getIsSleeping()) {
                if (actor.getRNG().nextInt(200) == 0) {
                    sleep.setSleepingState(EnumSleepingState.INTERRUPTED);
                    hunt = true;
                    VampirismIntegrationsMod.log.t("Interrupting sleep");

                }
            }
        } else {
            if (hunt) VampirismIntegrationsMod.log.t("day");

            hunt = false;
        }
        if (hunt) {

            if (target != null && target.getEntity().isDead) {
                target = null;
                biting = 0;
                VampirismIntegrationsMod.log.t("target dead");

            }
            if (target == null) {
                findTarget();
                if (target == null) {
                    blackList.clear();
                }
            }
            if (target != null) {
                if (actor.getDistanceSqToEntity(target.getEntity()) < 7) {
                    if (biting == 0) {
                        biting = 40;
                        VampirismIntegrationsMod.log.t("Strating bite");

                    }
                    EntityCreature e = target.getEntity();

                    actor.getLookHelper().setLookPosition(e.posX, e.posY + (double) e.getEyeHeight(), e.posZ, 10.0F, (float) (actor).getVerticalFaceSpeed());

                    biting--;
                    if (biting == 0) {
                        int amount = target.onBite(actorVampirism);
                        actor.playSound(ModSounds.player_bite, 1, 1);
                        actorVampirism.drinkBlood(amount, target.getBloodSaturation());
                        this.onFinish();
                    }
                } else {

                    biting = 0;
                    if (actor.getNavigator().noPath()) {
                        this.blackList.add(target.getEntity());
                        this.target = null;

                    } else {
                        actor.getNavigator().tryMoveToEntityLiving(target.getEntity(), Constants.SPEED_SNEAK);

                    }
                }
            }

        } else {
            biting = 0;
            target = null;
            blackList.clear();
            if (walkingHome > 0) {
                tryWalkHome();
            }
        }


    }

    @Override
    public void reset() {
        if (!actor.getEntityWorld().isRemote) {
            VampirismIntegrationsMod.log.t("Reset");
        }
        this.hunt = false;
        this.target = null;
        this.biting = 0;
        this.walkingHome = 0;
    }

    private void onFinish() {
        walkingHome = 100;
        hunt = false;
        target = null;
        VampirismIntegrationsMod.log.t("Bitten");


    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setBoolean("vamp_converted_hungry", hungry);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        hungry = !nbt.hasKey("vamp_converted_hungry") || nbt.getBoolean("vamp_converted_hungry");
    }

    private void tryWalkHome() {
        ActionSleep sleep = actor.getBehavior(ActionSleep.class);
        if (sleep.hasHomePoint() && sleep.isHomePointValid() && walkingHome > 1) {
            walkingHome--;
            Point3D home = sleep.getHomePoint();
            actor.getNavigator().tryMoveToXYZ(home.dX(), home.dY(), home.dZ(), Constants.SPEED_WALK);
        } else {
            actor.getBehavior(ActionSleep.class).setSleepingState(EnumSleepingState.AWAKE);
            reset();
            VampirismIntegrationsMod.log.t("done");

        }

    }

    private void findTarget() {
        List<Entity> list = actor.getEntityWorld().getEntitiesWithinAABB(EntityCreature.class, actor.getEntityBoundingBox().grow(20, 10, 20));
        list.removeIf(entity -> blackList.contains(entity));
        if (list.size() > 1) {

            try {
                list.sort(Comparator.comparingDouble(o -> actor.getDistanceSqToEntity(o)));
            } catch (Exception e) {

            }

        }

        for (Object o : list) {
            IExtendedCreatureVampirism bittingTarget = ExtendedCreature.get((EntityCreature) o);
            if (bittingTarget.canBeBitten(actorVampirism)) {
                target = bittingTarget;
                actor.getNavigator().tryMoveToEntityLiving(target.getEntity(), Constants.SPEED_WALK);

                VampirismIntegrationsMod.log.t("Fpund target %s", target);

                return;
            }

        }
    }
}
