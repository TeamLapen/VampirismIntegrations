package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.IAggressiveVillager;
import de.teamlapen.vampirism.api.world.ICaptureAttributes;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.entity.ai.goals.DefendVillageGoal;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import forge.net.mca.entity.VillagerEntityMCA;
import forge.net.mca.entity.ai.relationship.Gender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;


public class AggressiveVillagerEntityMCA extends VillagerEntityMCA implements IAggressiveVillager {
    @Nullable
    public static Villager makeAngry(VillagerEntityMCA villager) {
        if (villager.getProfession() == ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("mca:guard")) || villager.getProfession() == ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation("mca:outlaw")) || villager.isInfected()) {
            return null;
        }
        EntityType<? extends Villager> t = villager.getGenetics().getGender() == Gender.FEMALE ? MCARegistration.FEMALE_AGGRESSIVE_VILLAGER.get() : MCARegistration.MALE_AGGRESSIVE_VILLAGER.get();
        Villager angry = t.create(villager.level());
        if (angry == null) return null;
        angry.restoreFrom(villager);
        if (ModList.get().getModContainerById(REFERENCE.VAMPIRISM_ID).map(ModContainer::getModInfo).map(IModInfo::getVersion).map(version -> version.getMinorVersion() <= 9 && version.getIncrementalVersion() <= 3).orElse(true)) {
            villager.discard(); //Force discard the entity ourselves. Older Vampirism versions add the new entity first and thereby cause an UUID conflict
        }
        return angry;
    }

    @Nullable
    private ICaptureAttributes villageAttributes;

    public AggressiveVillagerEntityMCA(EntityType<AggressiveVillagerEntityMCA> type, Level w, Gender gender) {
        super(((EntityType) type), w, gender);
    }

    @Override
    public void attackVillage(ICaptureAttributes villageAttributes) {
        this.villageAttributes = villageAttributes;
    }

    @Override
    public void defendVillage(ICaptureAttributes villageAttributes) {
        this.villageAttributes = villageAttributes;
    }

    @Override
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor worldIn, @Nonnull DifficultyInstance difficultyIn, @Nonnull MobSpawnType reason, @javax.annotation.Nullable SpawnGroupData spawnDataIn, @javax.annotation.Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.PITCHFORK.get()));
        return data;
    }

    @Override
    @Nullable
    public ICaptureAttributes getCaptureInfo() {
        return this.villageAttributes;
    }

    @Override
    public LivingEntity getRepresentingEntity() {
        return this;
    }

    @Override
    @javax.annotation.Nullable
    public AABB getTargetVillageArea() {
        return this.villageAttributes == null ? null : this.villageAttributes.getVillageArea();
    }

    @Override
    public boolean isAttackingVillage() {
        return false;
    }

    @Override
    public boolean isDefendingVillage() {
        return this.villageAttributes != null;
    }

    @Override
    public void stopVillageAttackDefense() {
        LivingEntity villager = (LivingEntity) (this.getGenetics().getGender() == Gender.FEMALE ? ForgeRegistries.ENTITY_TYPES.getValue(MCACompat.FEMALE_VILLAGER) : ForgeRegistries.ENTITY_TYPES.getValue(MCACompat.MALE_VILLAGER)).create(this.level());
        assert villager != null;
        this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        villager.restoreFrom(this);
        if (ModList.get().getModContainerById(REFERENCE.VAMPIRISM_ID).map(ModContainer::getModInfo).map(IModInfo::getVersion).map(version -> version.getMinorVersion() <= 9 && version.getIncrementalVersion() <= 3).orElse(true)) {
            this.discard(); //Force discard the entity ourselves. Older Vampirism versions add the new entity first and thereby cause an UUID conflict
        }
        UtilLib.replaceEntity(this, villager);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 0.6D, false));
        this.goalSelector.addGoal(8, new MoveThroughVillageGoal(this, 0.55D, false, 400, () -> {
            return true;
        }));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 5, true, false, VampirismAPI.factionRegistry().getPredicate(this.getFaction(), true, false, false, false, null)));
        this.targetSelector.addGoal(3, new DefendVillageGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<PathfinderMob>(this, PathfinderMob.class, 5, true, false, VampirismAPI.factionRegistry().getPredicate(this.getFaction(), false, true, false, false, null)) {
            protected double getFollowDistance() {
                return super.getFollowDistance() / 2.0D;
            }
        });
    }
}
