package de.teamlapen.vampirism_integrations.guardvillagers;

import de.teamlapen.vampirism.api.event.VampirismVillageEvent;
import de.teamlapen.vampirism.entity.ai.goals.NearestTargetGoalModifier;
import de.teamlapen.vampirism.mixin.accessor.GoalSelectorAccessor;
import de.teamlapen.vampirism.mixin.accessor.NearestAttackableTargetGoalAccessor;
import de.teamlapen.vampirism_integrations.guardvillagers.tasks.GuardTargetNonFactionGoal;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class GuardVillagerEventHandler {

    private final static Logger LOGGER = LogManager.getLogger(GuardVillagerEventHandler.class);

    private final DeferredHolder<EntityType<?>, EntityType<? extends PathfinderMob>> GUARD = DeferredHolder.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("guardvillagers", "guard"));

    @SubscribeEvent
    public void onMakeAggressive(VampirismVillageEvent.MakeAggressive event) {
        event.setCanceled(true);
    }

    private boolean warnAboutGuardTask = true;

    @SubscribeEvent
    public void onEntityJoinWorld(@NotNull EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && GUARD.isBound() && event.getEntity() instanceof PathfinderMob guard && GUARD.get() == event.getEntity().getType()) {
            guard.targetSelector.addGoal(3, new GuardTargetNonFactionGoal(guard));

            //Guard villagers attack all {@link Enemy} creatures by default. When Smart IMOB type is activated in Vampirism, this will lead to undesired attacks. Therefore, we remove all faction entities from the default target goal.
            Goal mobTarget = null;
            for (WrappedGoal t : ((GoalSelectorAccessor) (guard).targetSelector).getAvailableGoals()) {
                if (t.getGoal() instanceof NearestAttackableTargetGoal && t.getPriority() == 3 && Mob.class.equals(((NearestAttackableTargetGoalAccessor<?>) t.getGoal()).getTargetType())) {
                    mobTarget = t.getGoal();
                    break;
                }
            }
            if (mobTarget != null) {
                ((NearestTargetGoalModifier) mobTarget).ignoreFactionEntities();
            } else {
                if (warnAboutGuardTask) {
                    LOGGER.warn("Could not modify guard target task");
                    warnAboutGuardTask = false;
                }
            }
        }
    }

    @SubscribeEvent
    public void onVillageCaptureComplete(@NotNull VampirismVillageEvent.VillagerCaptureFinish.Post event) {
        //After village capture, guards tend to continue attacking the (no-longer) aggressors. Resetting the target goal, helps a bit
        if (GUARD.isBound()) {
            event.getTotem().getTileWorld().getEntities(GUARD.get(), event.getVillageArea(), EntitySelector.NO_SPECTATORS).forEach(entity -> {
                entity.targetSelector.getAvailableGoals().forEach(WrappedGoal::stop);
            });
        } else {
            LogManager.getLogger().warn("Could not find guard villager for type " + GUARD.getId());
        }
    }
}
