package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.IAggressiveVillager;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import mca.entity.EntityVillagerMCA;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

public class EntityAngryVillagerMCA extends EntityVillagerVampirismMCA implements IAggressiveVillager {
    @GameRegistry.ObjectHolder("vampirism:pitchfork")
    private static final Item pitchfork = null;
    @Nonnull
    private ItemStack oldItem = ItemStack.EMPTY;
    private ItemStack pitchforkStack;

    public EntityAngryVillagerMCA(World world) {
        super(world);
        getBehaviors().addAction(new ActionDefendAgainstVampire(this));
        pitchforkStack = new ItemStack(pitchfork);
    }


    public static EntityAngryVillagerMCA makeAngry(EntityVillagerMCA villager) {
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
