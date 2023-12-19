package de.teamlapen.vampirism_integrations.guardvillagers.tasks;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.blockentity.TotemBlockEntity;
import de.teamlapen.vampirism.config.VampirismConfig;
import de.teamlapen.vampirism.util.TotemHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import tallestegg.guardvillagers.entities.Guard;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class GuardTargetNonFactionGoal extends NearestAttackableTargetGoal<LivingEntity> {

    private static final Map<IFaction<?>, Predicate<LivingEntity>> predicates = new HashMap<>();
    private final Guard guard;
    private IFaction<?> faction;

    public GuardTargetNonFactionGoal(Guard guard) {
        super(guard, LivingEntity.class, true);
        this.guard = guard;
    }

    @Override
    public boolean canContinueToUse() {
        if (guard.tickCount % 16 == 0) {
            if (determineGolemFaction()) {
                return false;
            }
        }
        return super.canContinueToUse();
    }


    @Override
    public boolean canUse() {
        if (guard.tickCount < 20) return false; // Some delay to allow nearby totems to load
        return super.canUse();
    }

    @Override
    protected void findTarget() {
        determineGolemFaction();
        super.findTarget();
    }

    /**
     * Determine the faction of the golem by checking for nearby totems. Update the targetConditions accordingly
     *
     * @return Whether the faction has changed
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private boolean determineGolemFaction() {
        IFaction<?> faction = VReference.HUNTER_FACTION;
        if (VampirismConfig.BALANCE.golemAttackNonVillageFaction.get()) {
            Optional<TotemBlockEntity> tile = TotemHelper.getTotemNearPos(((ServerLevel) this.guard.level()), this.guard.blockPosition(), true);
            faction = tile.map(TotemBlockEntity::getControllingFaction).orElse(((IFaction) VReference.HUNTER_FACTION)); // Raw type because otherwise it does not work
        }

        if (faction != this.faction) {
            this.targetConditions.selector(predicates.computeIfAbsent(this.faction = faction, faction1 -> VampirismAPI.factionRegistry().getPredicate(faction1, true, true, false, false, null)));
            return true;
        }
        return false;
    }
}
