package de.teamlapen.vampirism_integrations.mca;

import com.google.common.base.Predicates;
import de.teamlapen.vampirism.api.EnumStrength;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.entity.DamageHandler;
import de.teamlapen.vampirism.entity.ai.EntityAIMoveIndoorsDay;
import de.teamlapen.vampirism.entity.ai.VampireAIBiteNearbyEntity;
import de.teamlapen.vampirism.entity.ai.VampireAIFleeSun;
import de.teamlapen.vampirism.entity.ai.VampireAIMoveToBiteable;
import de.teamlapen.vampirism.util.Helper;
import mca.entity.EntityVillagerMCA;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Vampire version of MCA's villager
 * Tries to suck blood during the night
 */
public class EntityConvertedVillagerMCA extends EntityVillagerVampirismMCA implements IConvertedCreature<EntityVillagerMCA> {

    private EnumStrength garlicCache;
    private boolean sundamageCache;
    private int bloodTimer = 0;


    public EntityConvertedVillagerMCA(World world) {
        super(world);
        garlicCache = EnumStrength.NONE;
    }




    @Override
    public void onLivingUpdate() {
        if (this.ticksExisted % MCACompatREFERENCE.REFRESH_GARLIC_TICKS == 1) {
            isGettingGarlicDamage(true);
        }
        if (this.ticksExisted % MCACompatREFERENCE.REFRESH_SUNDAMAGE_TICKS == 2) {
            isGettingSundamage(true);
        }
        if (!world.isRemote) {
            if (isGettingSundamage() && ticksExisted % 40 == 11) {
                this.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 42));
                this.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 42));

            }
            if (isGettingGarlicDamage() != EnumStrength.NONE) {
                DamageHandler.affectVampireGarlicAmbient(this, isGettingGarlicDamage(), this.ticksExisted);
            }
        }

        super.onLivingUpdate();
        bloodTimer++;

    }

    @Override
    public void drinkBlood(int amt, float saturationMod, boolean useRemaining) {
        this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, amt * 20));
        bloodTimer = -1200 - rand.nextInt(1200);
    }

    @Override
    public boolean doesResistGarlic(EnumStrength strength) {
        return false;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();

        this.tasks.taskEntries.removeIf(entry -> entry.action instanceof EntityAIMoveIndoors || entry.action instanceof EntityAIVillagerMate || entry.action instanceof EntityAIFollowGolem);

        tasks.addTask(0, new EntityAIRestrictSun(this));
        tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityCreature.class, VampirismAPI.factionRegistry().getPredicate(getFaction(), true, true, false, false, VReference.HUNTER_FACTION), 10, 0.45F, 0.55F));
        tasks.addTask(2, new EntityAIMoveIndoorsDay(this));
        tasks.addTask(5, new VampireAIFleeSun(this, 0.6F, true));
        tasks.addTask(6, new EntityAIAttackMelee(this, 0.6F, false));
        tasks.addTask(7, new VampireAIBiteNearbyEntity(this));
        tasks.addTask(9, new VampireAIMoveToBiteable(this, 0.55F));


        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        bloodTimer = nbt.hasKey("vamp_converted_bloodtimer") ? nbt.getInteger("vamp_converted_bloodtimer") : 0;

    }

    @Nonnull
    @Override
    public EnumStrength isGettingGarlicDamage(boolean forceRefresh) {
        if (forceRefresh) {
            garlicCache = Helper.getGarlicStrength(this);
        }
        return garlicCache;
    }

    @Override
    public boolean isGettingSundamage(boolean forceRefresh) {
        if (!forceRefresh) return sundamageCache;
        return (sundamageCache = Helper.gettingSundamge(this));
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
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setInteger("vamp_converted_bloodtimer", bloodTimer);
    }

    @Override
    public EntityLivingBase getRepresentingEntity() {
        return this;
    }

    public static class ConvertingHandler implements IConvertingHandler<EntityVillagerMCA> {

        @Override
        public IConvertedCreature<EntityVillagerMCA> createFrom(EntityVillagerMCA entity) {
            NBTTagCompound nbt = new NBTTagCompound();
            entity.writeToNBT(nbt);
            EntityConvertedVillagerMCA converted = new EntityConvertedVillagerMCA(entity.world);
            converted.readFromNBT(nbt);
            converted.setUniqueId(MathHelper.getRandomUUID(converted.rand));
            return converted;
        }
    }
}
