package de.teamlapen.vampirism_integrations.mca;

import forge.net.mca.entity.ai.relationship.Gender;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class MCAEntityClassRedirect {
    public static EntityType.Builder<ConvertedVillagerEntityMCA> createConverted(boolean male) {
        return EntityType.Builder.<ConvertedVillagerEntityMCA>of((type, level) -> new ConvertedVillagerEntityMCA(type, level, male ? Gender.MALE : Gender.FEMALE), MobCategory.CREATURE).sized(0.6f, 2f);
    }

    public static EntityType.Builder<AngryVillagerEntityMCA> createAngry(boolean male) {
        return EntityType.Builder.<AngryVillagerEntityMCA>of((type, level) -> new AngryVillagerEntityMCA(type, level, male ? Gender.MALE : Gender.FEMALE), MobCategory.CREATURE).sized(0.6f, 2f);
    }
}
