package de.teamlapen.vampirism_integrations.util;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.tileentity.TotemTileEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;


/**
 * Entity target AI that targets vampires unless in a vampire village
 */
public class NearestVampireSmartTargetGoal extends NearestAttackableTargetGoal<CreatureEntity> {
    private boolean insideVampireVillage;

    public NearestVampireSmartTargetGoal(CreatureEntity creature, boolean checkSight, boolean onlyNearby) {
        super(creature, CreatureEntity.class, 10, checkSight, onlyNearby, VampirismAPI.factionRegistry().getPredicate(VReference.HUNTER_FACTION, false, true, false, false, VReference.VAMPIRE_FACTION));
    }

    @Override
    public boolean shouldExecute() {
        if (goalOwner.ticksExisted % 32 == 0) {
            this.insideVampireVillage = TotemTileEntity.isInsideVampireAreaCached(goalOwner.world.dimension, goalOwner.getPosition());
        }
        return !insideVampireVillage && super.shouldExecute();
    }
}
