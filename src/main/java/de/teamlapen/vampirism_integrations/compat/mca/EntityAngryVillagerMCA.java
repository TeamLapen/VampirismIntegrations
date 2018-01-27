package de.teamlapen.vampirism_integrations.compat.mca;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.IAggressiveVillager;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import mca.actions.ActionSleep;
import mca.entity.EntityVillagerMCA;
import mca.enums.EnumProfessionSkinGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Angry version of the MCA villager.
 * Similar to the Vampirism's HunterVillager
 */
public class EntityAngryVillagerMCA extends EntityVillagerVampirismMCA implements IAggressiveVillager {

    private ItemStack pitchforkStack;

    public EntityAngryVillagerMCA(World world) {
        super(world);
        getBehaviors().addAction(new ActionDefendAgainstVampire(this));
        pitchforkStack = new ItemStack(MCACompatREFERENCE.pitchfork);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        this.swingArm(getActiveHand());
        return super.attackEntityAsMob(entity);
    }

    @Nullable
    public static EntityAngryVillagerMCA makeAngry(EntityVillagerMCA villager) {
        if (villager.attributes.getProfessionSkinGroup() == EnumProfessionSkinGroup.Guard || villager.attributes.getIsInfected()) {
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
        return this.getBehavior(ActionSleep.class).getIsSleeping() ? ItemStack.EMPTY : pitchforkStack;
    }


    @Override
    public Entity makeCalm() {
        EntityVillagerMCA mca = new EntityVillagerMCA(this.getEntityWorld());
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        mca.readFromNBT(nbt);
        return mca;
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
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
    }
}
