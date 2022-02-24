package de.teamlapen.vampirism_integrations.mca;

import mca.entity.VillagerEntityMCA;
import mca.entity.ai.relationship.Gender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * Vampirism's basic extension of MCA's villager
 */
abstract class EntityVillagerVampirismMCA extends VillagerEntityMCA {


    private final boolean peaceful = false;


    public EntityVillagerVampirismMCA(EntityType<? extends VillagerEntityMCA> type, World w, Gender gender) {
        super((EntityType<VillagerEntityMCA>) type, w, gender);
    }


    @Override
    public boolean checkSpawnRules(IWorld worldIn, SpawnReason spawnReasonIn) {
        return (peaceful || worldIn.getDifficulty() != Difficulty.PEACEFUL) && super.checkSpawnRules(worldIn, spawnReasonIn);
    }


    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide && !peaceful && this.level.getDifficulty() == Difficulty.PEACEFUL) {
            this.remove();
        }
    }


}
