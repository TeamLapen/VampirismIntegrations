package de.teamlapen.vampirism_integrations.util;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.tileentity.TotemHelper;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;


/**
 * Entity target AI that targets vampires unless in a vampire village
 */
public class NearestVampireSmartTargetGoal extends NearestAttackableTargetGoal<PathfinderMob> {
    private boolean insideVampireVillage;

    public NearestVampireSmartTargetGoal(PathfinderMob creature, boolean checkSight, boolean onlyNearby) {
        super(creature, PathfinderMob.class, 10, checkSight, onlyNearby, VampirismAPI.factionRegistry().getPredicate(VReference.HUNTER_FACTION, false, true, false, false, VReference.VAMPIRE_FACTION));
    }

    @Override
    public boolean canUse() {
        if (mob.tickCount % 32 == 0) {
            this.insideVampireVillage = TotemHelper.isInsideVampireAreaCached(mob.level.dimension(), mob.blockPosition());
        }
        return !insideVampireVillage && super.canUse();
    }
}
