package de.teamlapen.vampirism_integrations.guardvillagers;

import de.teamlapen.vampirism.api.event.VampirismVillageEvent;
import de.teamlapen.vampirism_integrations.guardvillagers.tasks.GuardTargetNonFactionGoal;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import org.jetbrains.annotations.NotNull;
import tallestegg.guardvillagers.common.entities.Guard;

public class GuardVillagerEventHandler {

    @SubscribeEvent
    public void onMakeAggressive(VampirismVillageEvent.MakeAggressive event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(@NotNull EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Guard guard) {
            guard.targetSelector.addGoal(3, new GuardTargetNonFactionGoal(guard));
        }
    }
}
