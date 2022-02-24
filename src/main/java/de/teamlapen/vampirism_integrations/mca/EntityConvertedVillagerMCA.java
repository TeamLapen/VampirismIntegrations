package de.teamlapen.vampirism_integrations.mca;

import com.mojang.serialization.Dynamic;
import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.EnumStrength;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.api.entity.player.vampire.IBloodStats;
import de.teamlapen.vampirism.core.ModVillage;
import de.teamlapen.vampirism.entity.DamageHandler;
import de.teamlapen.vampirism.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
import mca.entity.VillagerEntityMCA;
import mca.entity.ai.relationship.Gender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;

/**
 * Vampire version of MCA's villager
 * Tries to suck blood during the night
 */
public class EntityConvertedVillagerMCA extends EntityVillagerVampirismMCA implements IConvertedCreature<VillagerEntityMCA> {

    @ObjectHolder("vampirism_integrations:mca_converted_villager_male")
    public static final EntityType<EntityConvertedVillagerMCA> converted_villager_male = UtilLib.getNull();
    @ObjectHolder("vampirism_integrations:mca_converted_villager_female")
    public static final EntityType<EntityConvertedVillagerMCA> converted_villager_female = UtilLib.getNull();

    private EnumStrength garlicCache;
    private boolean sundamageCache;
    private int bloodTimer = 0;


    public EntityConvertedVillagerMCA(EntityType<EntityConvertedVillagerMCA> type, World w, Gender gender) {
        super(type, w, gender);
        garlicCache = EnumStrength.NONE;
    }

    public void adjustBrainGoals(@Nonnull Brain<?> brain) {
        brain.setSchedule(ModVillage.converted_default);
        brain.updateActivityFromSchedule(this.level.getDayTime(), this.level.getGameTime());
    }

    @Override
    public void aiStep() {
        if (this.tickCount % MCACompatREFERENCE.REFRESH_GARLIC_TICKS == 1) {
            isGettingGarlicDamage(level, true);
        }
        if (this.tickCount % MCACompatREFERENCE.REFRESH_SUNDAMAGE_TICKS == 2) {
            isGettingSundamage(level, true);
        }
        if (!level.isClientSide) {
            if (isGettingSundamage(level) && tickCount % 40 == 11) {
                this.addEffect(new EffectInstance(Effects.WEAKNESS, 42));
            }
            if (isGettingGarlicDamage(level) != EnumStrength.NONE) {
                DamageHandler.affectVampireGarlicAmbient(this, isGettingGarlicDamage(level), this.tickCount);
            }
        }

        super.aiStep();
        bloodTimer++;

    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (!level.isClientSide && wantsBlood() && entity instanceof PlayerEntity && !Helper.isHunter(entity) && !UtilLib.canReallySee((LivingEntity) entity, this, true)) {
            int amt = VampirePlayer.getOpt((PlayerEntity) entity).map(vampire -> vampire.onBite(this)).orElse(0);
            drinkBlood(amt, IBloodStats.MEDIUM_SATURATION);
            return true;
        }
        return super.doHurtTarget(entity);
    }

    @Override
    public boolean doesResistGarlic(EnumStrength strength) {
        return false;
    }

    @Override
    public void drinkBlood(int amt, float saturationMod, boolean useRemaining) {
        this.addEffect(new EffectInstance(Effects.REGENERATION, amt * 20));
        bloodTimer = -1200 - random.nextInt(1200);
    }

    @Override
    public LivingEntity getRepresentingEntity() {
        return this;
    }

    @Nonnull
    @Override
    public EnumStrength isGettingGarlicDamage(IWorld iWorld, boolean forceRefresh) {
        if (forceRefresh) {
            garlicCache = Helper.getGarlicStrength(this, iWorld);
        }
        return garlicCache;
    }

    @Override
    public boolean isGettingSundamage(IWorld iWorld, boolean forceRefresh) {
        if (!forceRefresh) return sundamageCache;
        return (sundamageCache = Helper.gettingSundamge(this, iWorld, this.level.getProfiler()));
    }

    @Override
    public void refreshBrain(ServerWorld world) {
        super.refreshBrain(world);
        adjustBrainGoals(getMCABrain());
    }

    @Override
    public boolean isIgnoringSundamage() {
        return false;
    }

    @Override
    public boolean useBlood(int amt, boolean allowPartial) {
        return false;
    }

    @Override
    public IFaction getFaction() {
        return VReference.VAMPIRE_FACTION;
    }

    @Override
    public boolean wantsBlood() {
        return bloodTimer > 0;
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        Brain<?> b = super.makeBrain(dynamic);
        adjustBrainGoals(b);
        return b;
    }

    public static class ConvertingHandler implements IConvertingHandler<VillagerEntityMCA> {

        @Override
        public IConvertedCreature<VillagerEntityMCA> createFrom(VillagerEntityMCA entity) {
            CompoundNBT nbt = new CompoundNBT();
            entity.saveWithoutId(nbt);
            EntityConvertedVillagerMCA converted = new EntityConvertedVillagerMCA(entity.getGenetics().getGender() == Gender.FEMALE ? converted_villager_female : converted_villager_male, entity.level, entity.getGenetics().getGender());
            converted.load(nbt);
            converted.setUUID(MathHelper.createInsecureUUID(converted.random));
            return converted;
        }
    }
}
