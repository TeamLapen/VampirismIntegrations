package de.teamlapen.vampirism_integrations.consecration;


import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.core.ModTags;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import top.theillusivec4.consecration.api.ConsecrationApi;

public class HolyRegistration {


    static void registerToHolyRegistry(FMLServerStartedEvent event) {
        ModTags.Entities.VAMPIRE.getAllElements().forEach(t -> ConsecrationApi.getHolyRegistry().addUndead(t));
        ConsecrationApi.getHolyRegistry().addHolyDamage(VReference.VAMPIRE_IN_FIRE.getDamageType());
        ConsecrationApi.getHolyRegistry().addHolyDamage(VReference.VAMPIRE_ON_FIRE.getDamageType());
        ConsecrationApi.getHolyRegistry().addHolyDamage(VReference.HOLY_WATER.getDamageType());
    }
}
