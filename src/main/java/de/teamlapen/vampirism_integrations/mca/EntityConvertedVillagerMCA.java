package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.vampirism.api.EnumStrength;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import de.teamlapen.vampirism.api.entity.factions.IFaction;
import mca.entity.EntityVillagerMCA;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityConvertedVillagerMCA extends EntityVillagerMCA implements IConvertedCreature<EntityVillagerMCA> {
    public EntityConvertedVillagerMCA(World world) {
        super(world);
    }

    @Override
    public boolean wantsBlood() {
        return false;
    }

    @Override
    public boolean doesResistGarlic(EnumStrength strength) {
        return false;
    }

    @Override
    public void drinkBlood(int amt, float saturationMod) {

    }

    @Override
    public EnumStrength isGettingGarlicDamage() {
        return null;
    }

    @Override
    public EnumStrength isGettingGarlicDamage(boolean forcerefresh) {
        return null;
    }

    @Override
    public boolean isGettingSundamage(boolean forcerefresh) {
        return false;
    }

    @Override
    public boolean isGettingSundamage() {
        return false;
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
