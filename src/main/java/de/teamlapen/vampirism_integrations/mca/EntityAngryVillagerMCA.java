package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.IAggressiveVillager;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import de.teamlapen.vampirism.api.world.IVampirismVillage;
import de.teamlapen.vampirism.entity.ai.EntityAIDefendVillage;
import de.teamlapen.vampirism.entity.ai.EntityAIMoveThroughVillageCustom;
import mca.core.minecraft.ProfessionsMCA;
import mca.entity.EntityVillagerMCA;
import net.minecraft.world.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

/**
 * Angry version of the MCA villager.
 * Similar to the Vampirism's HunterVillager
 */
public class EntityAngryVillagerMCA extends EntityVillagerVampirismMCA implements IAggressiveVillager {

    private ItemStack pitchforkStack;
    private AABB area;


    public EntityAngryVillagerMCA(Level world) {
        super(world);
        pitchforkStack = new ItemStack(MCACompatREFERENCE.pitchfork);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        this.swingArm(getActiveHand());
        return super.attackEntityAsMob(entity);
    }

    @Nullable
    public static EntityAngryVillagerMCA makeAngry(EntityVillagerMCA villager) {
        if (villager.getProfessionForge() == ProfessionsMCA.guard || villager.getProfessionForge() == ProfessionsMCA.bandit || villager.get(EntityVillagerMCA.IS_INFECTED)) {
            return null;//Don't make guards or infected villagers angry
        }
        EntityAngryVillagerMCA angry = new EntityAngryVillagerMCA(villager.getEntityWorld());
        NBTTagCompound tag = new NBTTagCompound();
        villager.writeToNBT(tag);
        angry.readFromNBT(tag);
        angry.setUniqueId(MathHelper.getRandomUUID(villager.getRNG()));
        return angry;

    }

    @Override
    public ItemStack getHeldItem(EnumHand hand) {
        return pitchforkStack;
    }

    @Override
    public void attackVillage(AxisAlignedBB area) {
    }

    @Override
    public ItemStack getHeldItemMainhand() {
        return pitchforkStack;
    }

    @Override
    public void defendVillage(AxisAlignedBB area) {
        this.area = area;
    }

    @Nullable
    @Override
    public IVampirismVillage getCurrentFriendlyVillage() {
        return this.vampirismVillage != null ? this.vampirismVillage.getControllingFaction() == VReference.HUNTER_FACTION ? this.vampirismVillage : null : null;
    }

    @Nullable
    @Override
    public AxisAlignedBB getTargetVillageArea() {
        return area;
    }

    @Override
    public boolean isAttackingVillage() {
        return false;
    }

    @Override
    public void stopVillageAttackDefense() {
        EntityVillagerMCA mca = new EntityVillagerMCA(this.getEntityWorld());
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        mca.readFromNBT(nbt);
        this.world.spawnEntity(mca);
        this.setDead();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        applySpecialAITasks();
    }

    protected void applySpecialAITasks() {
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, 10, true, false, VampirismAPI.factionRegistry().getPredicate(getFaction(), true, false, false, false, null)));
        this.targetTasks.addTask(3, new EntityAIDefendVillage<>(this));
        this.targetTasks.addTask(4, new EntityAINearestAttackableTarget<EntityCreature>(this, EntityCreature.class, 5, true, false, VampirismAPI.factionRegistry().getPredicate(getFaction(), false, true, false, false, null)) {

            @Override
            protected double getTargetDistance() {
                return super.getTargetDistance() / 2;
            }
        });
    }


    @Override
    public IFaction getFaction() {
        return VReference.HUNTER_FACTION;
    }

    @Override
    public EntityLivingBase getRepresentingEntity() {
        return this;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.taskEntries.removeIf(entry -> entry.action instanceof EntityAITradePlayer || entry.action instanceof EntityAILookAtTradePlayer || entry.action instanceof EntityAIVillagerMate || entry.action instanceof EntityAIFollowGolem);
        this.tasks.addTask(6, new EntityAIAttackMelee(this, 0.6, false));
        this.tasks.addTask(8, new EntityAIMoveThroughVillageCustom(this, 0.55, false, 400));

        applySpecialAITasks();

    }

}
