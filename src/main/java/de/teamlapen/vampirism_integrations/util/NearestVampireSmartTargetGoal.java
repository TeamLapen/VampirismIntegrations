package de.teamlapen.vampirism_integrations.util;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.util.TotemHelper;
import net.minecraft.server.level.ServerLevel;
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
        if (mob.tickCount % 32 == 0 && mob.level instanceof ServerLevel serverLevel) {
            this.insideVampireVillage = TotemHelper.getTotemNearPos(serverLevel, mob.blockPosition(), true).map(totem -> totem.getControllingFaction() == VReference.VAMPIRE_FACTION).orElse(false);
        }
        return !insideVampireVillage && super.canUse();
    }
}
