package de.teamlapen.vampirism_integrations.compat.mca;

import com.google.common.base.Predicates;
import de.teamlapen.vampirism.api.EnumStrength;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.entity.DamageHandler;
import de.teamlapen.vampirism.util.Helper;
import mca.entity.EntityVillagerMCA;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Vampire version of MCA's villager
 * Tries to suck blood during the night
 */
public class EntityConvertedVillagerMCA extends EntityVillagerVampirismMCA implements IConvertedCreature<EntityVillagerMCA> {

    private EnumStrength garlicCache;
    private boolean sundamageCache;
    private boolean hungry = true;


    public EntityConvertedVillagerMCA(World world) {
        super(world);
        garlicCache = EnumStrength.NONE;
    }


    @Override
    public void addAI() {
        super.addAI();
        this.getBehaviors().addAction(new ActionSuckBlood(this));

        this.tasks.taskEntries.removeIf(task -> task.action instanceof EntityAIMoveIndoors);
        this.tasks.addTask(1, new EntityAIRestrictSun(this));
        //Not sure if this has an effect
        this.tasks.addTask(2, new EntityAIAvoidEntity<>(this, EntityLivingBase.class, Predicates.and(VampirismAPI.factionRegistry().getPredicate(VReference.VAMPIRE_FACTION, true, true, false, false, VReference.HUNTER_FACTION), (entity -> !EntityConvertedVillagerMCA.this.attributes.getSpouseUUID().equals(entity))), 10, 0.7, 0.9));

    }


    @Override
    public boolean wantsBlood() {
        return hungry;
    }

    @Override
    public boolean doesResistGarlic(EnumStrength strength) {
        return false;
    }

    @Override
    public void drinkBlood(int amt, float saturationMod) {
        this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, amt * 20));
        hungry = false;

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
            }
            if (isGettingGarlicDamage() != EnumStrength.NONE) {
                DamageHandler.affectVampireGarlicAmbient(this, isGettingGarlicDamage(), this.ticksExisted);
            }
        }
        boolean day = getEntityWorld().isDaytime();
        if (day && !hungry) hungry = true;
        super.onLivingUpdate();
    }


    @Nonnull
    @Override
    public EnumStrength isGettingGarlicDamage(boolean forceRefresh) {
        if (forceRefresh) {
            garlicCache = Helper.getGarlicStrength(this);
        }
        return garlicCache;
    }

    @Nonnull
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
    public IFaction getFaction() {
        return VReference.VAMPIRE_FACTION;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setBoolean("vamp_converted_hungry", hungry);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        hungry = !nbt.hasKey("vamp_converted_hungry") || nbt.getBoolean("vamp_converted_hungry");

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
