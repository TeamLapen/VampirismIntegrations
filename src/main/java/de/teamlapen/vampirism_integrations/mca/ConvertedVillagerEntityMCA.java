package de.teamlapen.vampirism_integrations.mca;

import com.google.common.collect.Lists;
import com.mojang.serialization.Dynamic;
import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.EnumStrength;
import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import de.teamlapen.vampirism.api.entity.convertible.ICurableConvertedCreature;
import de.teamlapen.vampirism.api.entity.player.vampire.IDrinkBloodContext;
import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.core.ModAdvancements;
import de.teamlapen.vampirism.core.ModAi;
import de.teamlapen.vampirism.core.ModVillage;
import de.teamlapen.vampirism.entity.ExtendedCreature;
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
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
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


public class ConvertedVillagerEntityMCA extends VillagerEntityMCA implements ICurableConvertedCreature<VillagerEntityMCA> {

    public static final List<SensorType<? extends Sensor<? super Villager>>> SENSOR_TYPES;
    private static final byte EVENT_ID_CURE = 40;
    private static final EntityDataAccessor<Boolean> CONVERTING;

    static {
        CONVERTING = SynchedEntityData.defineId(ConvertedVillagerEntityMCA.class, EntityDataSerializers.BOOLEAN);
        SENSOR_TYPES = Lists.newArrayList(VillagerAccessor.getSensorTypes());
        SENSOR_TYPES.remove(SensorType.VILLAGER_HOSTILES);
        SENSOR_TYPES.add(ModAi.VAMPIRE_VILLAGER_HOSTILES.get());
    }

    private EnumStrength garlicCache;
    private boolean sundamageCache;
    private int bloodTimer;
    private int conversionTime;
    private UUID conversationStarter;

    public ConvertedVillagerEntityMCA(EntityType<ConvertedVillagerEntityMCA> type, Level w, Gender gender) {
        super((EntityType) type, w, gender);
        this.garlicCache = EnumStrength.NONE;
        this.bloodTimer = 0;
    }

    @Override
    public @NotNull Mob asEntity() {
        return this;
    }

    //    @Override
//    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
//        super.addAdditionalSaveData(compound);
//        compound.putInt("ConversionTime", this.isConverting(this) ? this.conversionTime : -1);
//        if (this.conversationStarter != null) {
//            compound.putUUID("ConversionPlayer", this.conversationStarter);
//        }
//
//    }
    @Override
    public void aiStep() {
        if (!this.level().isClientSide && this.isAlive() && this.isConverting(this)) {
            --this.conversionTime;
            if (this.conversionTime <= 0 && EventHooks.canLivingConvert(this, EntityType.VILLAGER, (timer) -> {
                this.conversionTime = timer;
            })) {
                this.cureEntity((ServerLevel) this.level(), this, (EntityType) BuiltInRegistries.ENTITY_TYPE.get(this.getGenetics().getGender() == Gender.FEMALE ? MCACompat.FEMALE_VILLAGER : MCACompat.MALE_VILLAGER));
            }
        }

        if (this.tickCount % 40 == 1) {
            this.isGettingGarlicDamage(this.level(), true);
        }

        if (this.tickCount % 8 == 2) {
            this.isGettingSundamage(this.level(), true);
        }

        if (!this.level().isClientSide) {
            if (this.isGettingSundamage(this.level()) && this.tickCount % 40 == 11) {
                this.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 42));
            }

            if (this.isGettingGarlicDamage(this.level()) != EnumStrength.NONE) {
                DamageHandler.affectVampireGarlicAmbient(this, this.isGettingGarlicDamage(this.level()), this.tickCount);
            }
        }

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
        VillagerEntityMCA villager = ICurableConvertedCreature.super.cureEntity(world, entity, newType);
//        villager.setVillagerData(this.getVillagerData());
//        villager.setGossips(this.getGossips().store(NbtOps.INSTANCE).getValue());
//        villager.setOffers(this.getOffers());
//        villager.setVillagerXp(this.getVillagerXp());
        if (this.conversationStarter != null) {
            Player playerentity = world.getPlayerByUUID(this.conversationStarter);
            if (playerentity instanceof ServerPlayer) {
                ModAdvancements.TRIGGER_CURED_VAMPIRE_VILLAGER.get().trigger((ServerPlayer) playerentity, this, villager);
                world.onReputationEvent(ReputationEventType.ZOMBIE_VILLAGER_CURED, playerentity, villager);
            }
        }

        return villager;
    }

    @Override
    public boolean doHurtTarget(@Nonnull Entity entity) {
        if (!this.level().isClientSide && this.wantsBlood() && entity instanceof Player && !Helper.isHunter(entity) && !UtilLib.canReallySee((LivingEntity) entity, this, true)) {
            int amt = VampirePlayer.getOpt((Player) entity).map((vampire) -> vampire.onBite(this)).orElse(0);
            this.drinkBlood(amt, 0.7F, new DrinkBloodContext((LivingEntity) entity));
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
        this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, amt * 20));
        this.bloodTimer = -1200 - this.random.nextInt(1200);
    }

    @Override
    public LivingEntity getRepresentingEntity() {
        return this;
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
    @Nonnull
    public EnumStrength isGettingGarlicDamage(LevelAccessor iWorld, boolean forceRefresh) {
        if (forceRefresh) {
            this.garlicCache = Helper.getGarlicStrength(this, iWorld);
        }

        return this.garlicCache;
    }

    @Override
    public boolean isGettingSundamage(LevelAccessor iWorld, boolean forceRefresh) {
        return !forceRefresh ? this.sundamageCache : (this.sundamageCache = Helper.gettingSundamge(this, iWorld, this.level().getProfiler()));
    }

    @Override
    public boolean isIgnoringSundamage() {
        return false;
    }

    @Override
    @Nonnull
    public InteractionResult mobInteract(Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return stack.getItem() != Items.GOLDEN_APPLE ? super.mobInteract(player, hand) : this.interactWithCureItem(player, stack, this);
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

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("ConversionTime", 99) && compound.getInt("ConversionTime") > -1) {
            this.startConverting(compound.hasUUID("ConversionPlayer") ? compound.getUUID("ConversionPlayer") : null, compound.getInt("ConversionTime"), this);
        }

    }

    @Override
    public void startConverting(@Nullable UUID conversionStarterIn, int conversionTimeIn, @Nonnull PathfinderMob entity) {
        ICurableConvertedCreature.super.startConverting(conversionStarterIn, conversionTimeIn, entity);
        entity.level().broadcastEntityEvent(entity, EVENT_ID_CURE); //Use our own id to avoid clash with MCA reward hearts event

        this.conversationStarter = conversionStarterIn;
        this.conversionTime = conversionTimeIn;
    }

    public void registerBrainGoals(@Nonnull Brain<VillagerEntityMCA> brain) {
        VillagerTasksMCA.initializeTasks(this, brain);
        AgeState age = AgeState.byCurrentAge(this.getAge());
        if (age != AgeState.ADULT) {
            brain.setSchedule(ModVillage.CONVERTED_DEFAULT.get());
            brain.updateActivityFromSchedule(this.level().getDayTime(), this.level().getGameTime() + 21 /*Trick update*/);
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

}
