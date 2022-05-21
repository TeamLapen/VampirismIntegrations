package de.teamlapen.vampirism_integrations.betteranimalsplus;

import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.api.entity.IVampirismEntityRegistry;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import dev.itsmeow.betteranimalsplus.init.ModEntities;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class BetterAnimalsPlusConvertibles {

    static void registerConvertibles() {
        Function<String, ResourceLocation> overlay = (String name) -> new ResourceLocation(REFERENCE.MODID, String.format("textures/entity/betteranimalsplus/%s_overlay.png", name));
        IVampirismEntityRegistry registry = VampirismAPI.entityRegistry();

        registry.addConvertible(ModEntities.PHEASANT.getEntityType(), overlay.apply("pheasant"));
        registry.addConvertible(ModEntities.BADGER.getEntityType(), overlay.apply("badger"));
        registry.addConvertible(ModEntities.MOOSE.getEntityType(), overlay.apply("moose"));
        registry.addConvertible(ModEntities.COYOTE.getEntityType(), overlay.apply("coyote"));
        registry.addConvertible(ModEntities.GOOSE.getEntityType(), overlay.apply("goose"));
        registry.addConvertible(ModEntities.BOAR.getEntityType(), overlay.apply("boar"));
        registry.addConvertible(ModEntities.DEER.getEntityType(), overlay.apply("deer"));
        registry.addConvertible(ModEntities.REINDEER.getEntityType(), overlay.apply("deer"));
        registry.addConvertible(ModEntities.BROWN_BEAR.getEntityType(), overlay.apply("bear"));
        registry.addConvertible(ModEntities.TURKEY.getEntityType(), overlay.apply("turkey"));
        registry.addConvertible(ModEntities.WALRUS.getEntityType(), overlay.apply("walrus"));
        registry.addConvertible(ModEntities.BLACK_BEAR.getEntityType(), overlay.apply("bear"));
        registry.addConvertible(ModEntities.FERAL_WOLF.getEntityType(), overlay.apply("feralwolf"));
    }
}
