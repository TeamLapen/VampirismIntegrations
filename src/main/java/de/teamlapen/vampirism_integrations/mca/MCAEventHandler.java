package de.teamlapen.vampirism_integrations.mca;

import de.teamlapen.lib.lib.util.UtilLib;
import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.entity.factions.IFactionEntity;
import de.teamlapen.vampirism.api.event.VampirismVillageEvent;
import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.entity.converted.ConvertedVillagerEntity;
import net.conczin.mca.entity.VillagerEntityMCA;
import net.conczin.mca.entity.ai.relationship.Gender;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.neoforged.bus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;


public class MCAEventHandler {


    @SubscribeEvent
    public void onCreateAggressiveVillager(VampirismVillageEvent.MakeAggressive event) {
        if (event.getOldVillager() instanceof VillagerEntityMCA villagerEntityMCA) {
            if (villagerEntityMCA instanceof IFactionEntity) return;
            if (villagerEntityMCA.getAge() < 0) return;
            Villager v = AggressiveVillagerEntityMCA.makeAngry(villagerEntityMCA);
            if (v != null) {
                UtilLib.replaceEntity(event.getOldVillager(), v);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onSpawnNewVillager(VampirismVillageEvent.SpawnNewVillager event) {
        boolean gender = event.getNewVillager().getRandom().nextBoolean();
        boolean vampire = event.getNewVillager() instanceof IConvertedCreature<?>;

        EntityType<?> t = vampire ?  (gender ? MCARegistration.MALE_CONVERTED_VILLAGER.get() : MCARegistration.FEMALE_CONVERTED_VILLAGER.get()) : gender ? BuiltInRegistries.ENTITY_TYPE.get(MCACompat.MALE_VILLAGER) : BuiltInRegistries.ENTITY_TYPE.get(MCACompat.FEMALE_VILLAGER);
        event.setNewVillager((Villager) t.create(event.getWorld()));
    }

    @SubscribeEvent
    public void onVillageCapture(VampirismVillageEvent.VillagerCaptureFinish.Pre event){
        //Replace existing vampire villagers on full convert with hunter villagers
        if (VReference.VAMPIRE_FACTION.equals(event.getControllingFaction())) {
            List<Villager> newVillagers = new ArrayList<>();
            List<Villager> oldVillagers = new ArrayList<>();

            for (Villager villager : event.getVillager()) {
                if(villager instanceof ConvertedVillagerEntityMCA mca){
                    if (villager.hasEffect(ModEffects.SANGUINARE)) {
                        villager.removeEffect(ModEffects.SANGUINARE);
                    }
                    if (event.isForced()) {
                            Gender gender = mca.getGenetics().getGender();
                            boolean vampire = false;
                            EntityType<VillagerEntityMCA> t = gender == Gender.MALE ? (EntityType<VillagerEntityMCA>) BuiltInRegistries.ENTITY_TYPE.get(MCACompat.MALE_VILLAGER) : (EntityType<VillagerEntityMCA>) BuiltInRegistries.ENTITY_TYPE.get(MCACompat.FEMALE_VILLAGER);
                            VillagerEntityMCA replacement = mca.createCuredEntity(mca, t);
                            replacement.setUUID(Mth.createInsecureUUID());
                            UtilLib.replaceEntity(mca, replacement);
                            oldVillagers.add(mca);
                            newVillagers.add(replacement);
                    }
                }
            }
            //Update event list
            event.getVillager().removeAll(oldVillagers);
            event.getVillager().addAll(newVillagers);
        }
    }
}
