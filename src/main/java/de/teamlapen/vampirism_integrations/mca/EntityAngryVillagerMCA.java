package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.IAggressiveVillager;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.api.world.ICaptureAttributes;
import de.teamlapen.vampirism.core.ModItems;
import de.teamlapen.vampirism.entity.goals.DefendVillageGoal;
import mca.entity.VillagerEntityMCA;
import mca.entity.ai.ProfessionsMCA;
import mca.entity.ai.relationship.Gender;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Angry version of the MCA villager.
 * Similar to the Vampirism's HunterVillager
 */
public class EntityAngryVillagerMCA extends EntityVillagerVampirismMCA implements IAggressiveVillager {

    @ObjectHolder("vampirism_integrations:mca_angry_villager_male")
    private static final EntityType<EntityAngryVillagerMCA> angry_villager_male = UtilLib.getNull();
    @ObjectHolder("vampirism_integrations:mca_angry_villager_female")
    private static final EntityType<EntityAngryVillagerMCA> angry_villager_female = UtilLib.getNull();

    @Nullable
    public static EntityAngryVillagerMCA makeAngry(VillagerEntityMCA villager) {
        if (villager.getProfession() == ProfessionsMCA.GUARD || villager.getProfession() == ProfessionsMCA.OUTLAW || villager.isInfected()) {
            return null;//Don't make guards or infected villagers angry
        }
        EntityAngryVillagerMCA angry = new EntityAngryVillagerMCA(villager.getGenetics().getGender() == Gender.FEMALE ? angry_villager_female : angry_villager_male, villager.level, villager.getGenetics().getGender());
        CompoundNBT tag = new CompoundNBT();
        villager.saveWithoutId(tag);
        angry.load(tag);
        angry.setUUID(MathHelper.createInsecureUUID(villager.getRandom()));
        return angry;

    }

    @Nullable
    private ICaptureAttributes villageAttributes;


    public EntityAngryVillagerMCA(EntityType<? extends EntityAngryVillagerMCA> type, World w, Gender gender) {
        super(type, w, gender);
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
    public ILivingEntityData finalizeSpawn(@Nonnull IServerWorld worldIn, @Nonnull DifficultyInstance difficultyIn, @Nonnull SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        ILivingEntityData data = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.pitchfork));
        return data;
    }

    @Nullable
    @Override
    public ICaptureAttributes getCaptureInfo() {
        return villageAttributes;
    }

    @Override
    public IFaction getFaction() {
        return VReference.HUNTER_FACTION;
    }

    @Override
    public boolean isAttackingVillage() {
        return false;
    }

    @Override
    public LivingEntity getRepresentingEntity() {
        return this;
    }

    @Nullable
    @Override
    public AxisAlignedBB getTargetVillageArea() {
        return villageAttributes == null ? null : villageAttributes.getVillageArea();
    }

    @Override
    public boolean isDefendingVillage() {
        return villageAttributes != null;
    }

    @Override
    public void refreshBrain(@Nonnull ServerWorld serverWorldIn) {
    }

    @Override
    public void stopVillageAttackDefense() {
        VillagerEntityMCA villager = this.getGenetics().getGender().getVillagerType().create(this.level);
        assert villager != null;
        this.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        CompoundNBT nbt = new CompoundNBT();
        this.saveWithoutId(nbt);
        villager.load(nbt);
        villager.setUUID(MathHelper.createInsecureUUID(this.random));
        UtilLib.replaceEntity(this, villager);
    }

    @Override
    protected ITextComponent getTypeName() {
        return this.getType().getDescription(); //Don't use profession as part of the translation key
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(6, new MeleeAttackGoal(this, 0.6, false));
        this.goalSelector.addGoal(8, new MoveThroughVillageGoal(this, 0.55, false, 400, () -> true));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 5, true, false, VampirismAPI.factionRegistry().getPredicate(getFaction(), true, false, false, false, null)));
        this.targetSelector.addGoal(3, new DefendVillageGoal<>(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<CreatureEntity>(this, CreatureEntity.class, 5, true, false, VampirismAPI.factionRegistry().getPredicate(getFaction(), false, true, false, false, null)) {

            @Override
            protected double getFollowDistance() {
                return super.getFollowDistance() / 2;
            }
        });
    }


}
