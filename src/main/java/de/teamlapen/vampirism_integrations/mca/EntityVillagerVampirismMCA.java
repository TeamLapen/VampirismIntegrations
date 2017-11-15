package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.vampirism.api.world.IVampirismVillage;
import de.teamlapen.vampirism.util.Helper;
import de.teamlapen.vampirism.world.villages.VampirismVillageHelper;
import mca.entity.EntityVillagerMCA;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import javax.annotation.Nullable;

class EntityVillagerVampirismMCA extends EntityVillagerMCA {
    protected boolean peaceful = false;
    protected
    @Nullable
    IVampirismVillage IVampirismVillageObj;
    /**
     * A timer which reaches 0 every 70 to 120 ticks
     */
    private int randomTickDivider;

    public EntityVillagerVampirismMCA(World worldIn) {
        super(worldIn);
    }


    public boolean attackEntityAsMob(Entity entity) {
        float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entity instanceof EntityLivingBase) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entity).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag) {
            if (i > 0) {
                entity.addVelocity((double) (-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * (float) i * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0) {
                entity.setFire(j * 4);
            }

            this.applyEnchantments(this, entity);

        }


        return flag;
    }

    @Override
    public boolean attackEntityFrom(DamageSource src, float amount) {
        if (this.isEntityInvulnerable(src)) {
            return false;
        } else if (super.attackEntityFrom(src, amount)) {
            Entity entity = src.getTrueSource();
            if (entity instanceof EntityLivingBase) {
                this.setAttackTarget((EntityLivingBase) entity);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean getCanSpawnHere() {
        return (peaceful || this.world.getDifficulty() != EnumDifficulty.PEACEFUL) && super.getCanSpawnHere();
    }

    @Nullable
    public IVampirismVillage getVampirismVillage() {
        return IVampirismVillageObj;
    }

    @Override
    public void onLivingUpdate() {
        this.updateArmSwingProgress();
        super.onLivingUpdate();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.world.isRemote && !peaceful && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE) == null) {
            this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

        }
    }

    protected void teleportAway() {
        this.setInvisible(true);
        Helper.spawnParticlesAroundEntity(this, EnumParticleTypes.PORTAL, 5, 64);

        this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);

        this.setDead();
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (--this.randomTickDivider <= 0) {
            this.randomTickDivider = 70 + rand.nextInt(50);
            this.IVampirismVillageObj = VampirismVillageHelper.getNearestVillage(world, getPosition(), 32);
        }

    }
}
