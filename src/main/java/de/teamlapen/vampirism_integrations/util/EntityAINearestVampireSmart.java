package de.teamlapen.vampirism_integrations.util;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.world.IVampirismVillage;
import de.teamlapen.vampirism.world.villages.VampirismVillageHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

/**
 * Entity target AI that targets vampires unless in a vampire village
 */
public class EntityAINearestVampireSmart extends EntityAINearestAttackableTarget<EntityCreature> {
    private boolean insideVampireVillage;

    public EntityAINearestVampireSmart(EntityCreature creature, boolean checkSight, boolean onlyNearby) {
        super(creature, EntityCreature.class, 10, checkSight, onlyNearby, VampirismAPI.factionRegistry().getPredicate(VReference.HUNTER_FACTION, false, true, false, false, VReference.VAMPIRE_FACTION));
    }

    @Override
    public boolean shouldExecute() {
        if (taskOwner.ticksExisted % 32 == 0) {
            IVampirismVillage v = VampirismVillageHelper.getNearestVillage(taskOwner);
            this.insideVampireVillage = v != null && v.getControllingFaction() == VReference.VAMPIRE_FACTION;
        }
        return !insideVampireVillage && super.shouldExecute();
    }
}
