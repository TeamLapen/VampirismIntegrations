package de.teamlapen.vampirism_integrations.betteranimals;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.IVampirismEntityRegistry;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class BetterAnimalsConvertibles {

    static void changeConvertibles() {
        Function<String, ResourceLocation> overlay = (String name) -> new ResourceLocation(REFERENCE.MODID, String.format("textures/entity/betteranimals/%s_overlay.png", name));
        IVampirismEntityRegistry registry = VampirismAPI.entityRegistry();

        registry.addConvertible(EntityType.COW, overlay.apply("cow"));
        registry.addConvertible(EntityType.OCELOT, overlay.apply("ocelot"));
        registry.addConvertible(EntityType.PIG, overlay.apply("pig"));
        registry.addConvertible(EntityType.POLAR_BEAR, overlay.apply("polar_bear"));
        registry.addConvertible(EntityType.SHEEP, overlay.apply("sheep"));
    }
}
