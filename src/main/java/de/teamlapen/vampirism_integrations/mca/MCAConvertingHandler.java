package de.teamlapen.vampirism_integrations.mca;

import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.teamlapen.vampirism.api.entity.convertible.Converter;
import de.teamlapen.vampirism.api.entity.convertible.IConvertedCreature;
import de.teamlapen.vampirism.api.entity.convertible.IConvertingHandler;
import de.teamlapen.vampirism.datamaps.ConverterEntry;
import de.teamlapen.vampirism.entity.ExtendedCreature;
import de.teamlapen.vampirism.entity.converted.DefaultConvertingHandler;
import de.teamlapen.vampirism.entity.converted.converter.DefaultConverter;
import net.conczin.mca.entity.VillagerEntityMCA;
import net.conczin.mca.entity.ai.relationship.Gender;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Very basic converting handler. The registration is done via the datapack `data/vampirism/data_maps/entity_type/entity_converter.json
 */
public class MCAConvertingHandler implements IConvertingHandler<VillagerEntityMCA> {

    @Override
    public IConvertedCreature<VillagerEntityMCA> createFrom(VillagerEntityMCA entity) {
        ConvertedVillagerEntityMCA converted = (entity.getGenetics().getGender() == Gender.FEMALE ? MCARegistration.FEMALE_CONVERTED_VILLAGER : MCARegistration.MALE_CONVERTED_VILLAGER).get().create(entity.level());
        Optional<CompoundTag> data = ExtendedCreature.getSafe(converted).map(ec -> ec.serializeNBT(converted.registryAccess()));
        converted.restoreFrom(entity);
        data.ifPresent(tag -> ExtendedCreature.getSafe(converted).ifPresent(ec -> ec.deserializeNBT(converted.registryAccess(), tag)));
//            if (ModList.get().getModContainerById(REFERENCE.VAMPIRISM_ID).map(ModContainer::getModInfo).map(IModInfo::getVersion).map(version -> version.getMinorVersion() <= 9 && version.getIncrementalVersion() <= 3).orElse(true)) {
//                entity.discard(); //Force discard the entity ourselves. Older Vampirism versions add the new entity first and thereby cause an UUID conflict
//            }
        converted.yBodyRot = entity.yBodyRot;
        converted.yHeadRot = entity.yHeadRot;
        return converted;
    }

    public static class MCAConverter implements Converter{
        private static final MCAConverter INSTANCE = new MCAConverter();

        public static final MapCodec<MCAConverter> CODEC = MapCodec.of(Encoder.empty(), Decoder.unit(INSTANCE));

        @Override
        public IConvertingHandler<?> createHandler(@Nullable ResourceLocation texture) {
            return new MCAConvertingHandler();
        }

        @Override
        public MapCodec<? extends Converter> codec() {
            return MCARegistration.MCA_CONVERTER.get();
        }
    }
}
