package de.teamlapen.vampirism_integrations.mca;

import mca.entity.ai.relationship.Gender;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.npc.Villager;

public class MCAEntityClassRedirect {
    public static EntityType.Builder<? extends Villager> create(boolean male) {
        return EntityType.Builder.<ConvertedVillagerEntityMCA>of((type, level) -> new ConvertedVillagerEntityMCA(type, level, male ? Gender.MALE : Gender.FEMALE), MobCategory.CREATURE).sized(0.6f, 2f);
    }
}
