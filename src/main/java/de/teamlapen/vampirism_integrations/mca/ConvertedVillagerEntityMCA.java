package de.teamlapen.vampirism_integrations.mca;

import com.google.common.collect.Lists;
import com.mojang.serialization.Dynamic;
import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.EnumStrength;
import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import de.teamlapen.vampirism.api.entity.convertible.ICurableConvertedCreature;
import de.teamlapen.vampirism.api.entity.player.vampire.IBloodStats;
import de.teamlapen.vampirism.api.entity.player.vampire.IDrinkBloodContext;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.core.ModAdvancements;
import de.teamlapen.vampirism.core.ModAi;
import de.teamlapen.vampirism.core.ModAttributes;
import de.teamlapen.vampirism.core.ModVillage;
import de.teamlapen.vampirism.entity.ConvertedCreature;
import de.teamlapen.vampirism.entity.ExtendedCreature;
import de.teamlapen.vampirism.entity.converted.CurableConvertedCreature;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.vampire.DrinkBloodContext;
import de.teamlapen.vampirism.entity.villager.Trades;
import de.teamlapen.vampirism.mixin.accessor.VillagerAccessor;
import de.teamlapen.vampirism.util.DamageHandler;
import de.teamlapen.vampirism.util.Helper;
import de.teamlapen.vampirism.util.RegUtil;
import de.teamlapen.vampirism.util.VampirismEventFactory;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.conczin.mca.entity.VillagerEntityMCA;
import net.conczin.mca.entity.ai.brain.VillagerTasksMCA;
import net.conczin.mca.entity.ai.relationship.AgeState;
import net.conczin.mca.entity.ai.relationship.Gender;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforgespi.language.IModInfo;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class ConvertedVillagerEntityMCA extends VillagerEntityMCA implements CurableConvertedCreature<VillagerEntityMCA, ConvertedVillagerEntityMCA> {

    public static final List<SensorType<? extends Sensor<? super Villager>>> SENSOR_TYPES;
    private static final byte EVENT_ID_CURE = 40;
    private static final EntityDataAccessor<Boolean> CONVERTING;

    static {
        CONVERTING = SynchedEntityData.defineId(ConvertedVillagerEntityMCA.class, EntityDataSerializers.BOOLEAN);
        SENSOR_TYPES = Lists.newArrayList(VillagerAccessor.getSensorTypes());
        SENSOR_TYPES.remove(SensorType.VILLAGER_HOSTILES);
        SENSOR_TYPES.add(ModAi.VAMPIRE_VILLAGER_HOSTILES.get());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return VillagerEntityMCA.createAttributes().add(ModAttributes.SUNDAMAGE);  //No damage for now, because no avoid sun AI. BalanceMobProps.mobProps.VAMPIRE_MOB_SUN_DAMAGE
    }

    private final Data<VillagerEntityMCA> convertedData = new Data<>();
    private int bloodTimer;

    public ConvertedVillagerEntityMCA(EntityType<ConvertedVillagerEntityMCA> type, Level w, Gender gender) {
        super((EntityType) type, w, gender);
        this.bloodTimer = 0;
    }

    @Override
    public Data<VillagerEntityMCA> data() {
        return this.convertedData;
    }

    @Override
    public @NotNull Mob asEntity() {
        return this;
    }


    @Override
    public CompoundTag saveWithoutId(CompoundTag compound) {
        //Unfortunately, the MCA villager addAdditionalSaveData is final, so we have to use saveWithoutId
        CompoundTag tag = super.saveWithoutId(compound);
        this.addAdditionalSaveDataC(tag);
        return tag;
    }


    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        readAdditionalSaveDataC(compound);
    }


    @Override
    public void aiStep() {
        aiStepC( (EntityType) BuiltInRegistries.ENTITY_TYPE.get(this.getGenetics().getGender() == Gender.FEMALE ? MCACompat.FEMALE_VILLAGER : MCACompat.MALE_VILLAGER));
        ++this.bloodTimer;
        super.aiStep();
    }


    @Override
    public boolean doesResistGarlic(EnumStrength strength) {
        return false;
    }

    @Override
    public VillagerEntityMCA createCuredEntity(@NotNull PathfinderMob entity, @NotNull EntityType<VillagerEntityMCA> newType) {
        VillagerEntityMCA villager = newType.create(entity.level());
        assert villager != null;
        villager.restoreFrom(entity);
        villager.yBodyRot = entity.yBodyRot;
        villager.yHeadRot = entity.yHeadRot;
        return villager;
    }

    @Override
    public VillagerEntityMCA cureEntity(ServerLevel world, PathfinderMob entity, EntityType<VillagerEntityMCA> newType) {
        VillagerEntityMCA villager = CurableConvertedCreature.super.cureEntity(world, entity, newType);
        if (this.data().conversationStarter != null) {
            Player playerentity = world.getPlayerByUUID(this.data().conversationStarter);
            if (playerentity instanceof ServerPlayer) {
                ModAdvancements.TRIGGER_CURED_VAMPIRE_VILLAGER.get().trigger((ServerPlayer) playerentity, this, villager);
                world.onReputationEvent(ReputationEventType.ZOMBIE_VILLAGER_CURED, playerentity, villager);
            }
        }
        return villager;
    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity entity) {
        if (!this.level().isClientSide && this.wantsBlood() && entity instanceof Player player && !Helper.isHunter(entity) && !UtilLib.canReallySee((LivingEntity) entity, this, true)) {
            int amt = VampirePlayer.get(player).onBite(this);
            drinkBlood(amt, IBloodStats.MEDIUM_SATURATION, new DrinkBloodContext(player));
            return true;
        } else {
            return super.doHurtTarget(entity);
        }
    }

    @Override
    public EntityDataAccessor<Boolean> getConvertingDataParam() {
        return CONVERTING;
    }

    @Override
    public void drinkBlood(int amt, float saturationMod, boolean useRemaining, IDrinkBloodContext drinkContext) {
        BloodDrinkEvent.@NotNull EntityDrinkBloodEvent event = VampirismEventFactory.fireVampireDrinkBlood(this, amt, saturationMod, useRemaining, drinkContext);
        this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, event.getAmount() * 20));
        bloodTimer = -1200 - random.nextInt(1200);
    }

    @Override
    public LivingEntity getRepresentingEntity() {
        return this;
    }

    @Override
    public void handleEntityEventSuper(byte id) {
        super.handleEntityEvent(id);
    }


    @Override
    public InteractionResult mobInteractSuper(@NotNull Player player, @NotNull InteractionHand hand) {
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean hurtSuper(DamageSource damageSource, float amount) {
        return super.hurt(damageSource, amount);
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return this.mobInteractC(player, hand);
    }

    @Override
    protected @NotNull Component getTypeName() {
        ResourceLocation profName = RegUtil.id(this.getVillagerData().getProfession());
        return Component.translatable(EntityType.VILLAGER.getDescriptionId() + '.' + (!"minecraft".equals(profName.getNamespace()) ? profName.getNamespace() + '.' : "") + profName.getPath());
    }

    @Override
    public void handleEntityEvent(byte id) {
        //MCA villagers use 16 as sound event for reward hearts. ICurableConvertedCreature uses 16 for curing sound
        //We use SOUND_ID_CURE
        if (id == 16) {
            super.handleEntityEvent(id);
            return;
        } else if (id == EVENT_ID_CURE) {
            id = 16;
        }
        if (!this.handleSound(id, this)) {
            super.handleEntityEvent(id);
        }

    }



    @Override
    public boolean isIgnoringSundamage() {
        return false;
    }


    @Override
    public ItemStack eat(Level world, ItemStack stack, FoodProperties foodProperties) {
        //Also allow curing when gifting a golden apple
        if (stack.getItem() == Items.GOLDEN_APPLE) {
            if (!isConverting(this) && this.hasEffect(MobEffects.WEAKNESS)) {
                this.startConverting(this.getInteractions().getInteractingPlayer().map(Entity::getUUID).orElse(null), this.getRandom().nextInt(2400) + 2400, this);
            }
        }
        return super.eat(world, stack, foodProperties);
    }


    public void registerBrainGoals(@Nonnull Brain<VillagerEntityMCA> brain) {
        VillagerTasksMCA.initializeTasks(this, brain);
        AgeState age = AgeState.byCurrentAge(this.getAge());
        if (age == AgeState.ADULT) {
            brain.setSchedule(ModVillage.CONVERTED_DEFAULT.get());
            brain.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime());
        }
    }

    @Override
    public boolean wantsBlood() {
        return this.bloodTimer > 0;
    }

    @Override
    public boolean useBlood(int amt, boolean allowPartial) {
        this.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, amt * 20));
        this.bloodTimer = 0;
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        this.registerConvertingData(builder);
    }

    @Override
    @Nonnull
    protected Brain<?> makeBrain(@Nonnull Dynamic<?> dynamicIn) {
        Brain<VillagerEntityMCA> brain = VillagerTasksMCA.createProfile().makeBrain(dynamicIn);
        this.registerBrainGoals(brain);
        return brain;
    }

    @Override
    public void refreshBrain(ServerLevel world) {
        Brain<VillagerEntityMCA> brain = this.getMCABrain();
        brain.stopAll(world, this);
        this.brain = brain.copyWithoutBehaviors();
        registerBrainGoals(this.getMCABrain());
    }

    @Override
    protected void updateTrades() {
        super.updateTrades();
        if (!this.getOffers().isEmpty() && this.getRandom().nextInt(3) == 0) {
            this.addOffersFromItemListings(this.getOffers(), Trades.converted_trades, 1);
        }

    }

    @Override
    public void die(@NotNull DamageSource pCause) {
        super.die(pCause);
        this.dieC(pCause);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        this.tickDeathC();
    }
//      Unfortunately, the method is final in MCA
//    @Override
//    public boolean hurt(@NotNull DamageSource src, float amount) {
//        return this.hurtC(src, amount);
//    }
}
