package de.teamlapen.vampirism_integrations.consecration;


import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.core.ModTags;
import top.theillusivec4.consecration.api.ConsecrationApi;

public class HolyRegistration {


    static void registerToHolyRegistry(FMLServerStartedEvent event) {
        ModTags.Entities.VAMPIRE.getValues().forEach(t -> ConsecrationApi.getHolyRegistry().addUndead(t));
        ConsecrationApi.getHolyRegistry().addHolyDamage(VReference.VAMPIRE_IN_FIRE.getMsgId());
        ConsecrationApi.getHolyRegistry().addHolyDamage(VReference.VAMPIRE_ON_FIRE.getMsgId());
        ConsecrationApi.getHolyRegistry().addHolyDamage(VReference.HOLY_WATER.getMsgId());
    }
}
