package de.teamlapen.vampirism_integrations.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotation.BracketDumper;
import com.blamejared.crafttweaker.api.annotation.BracketResolver;
import com.blamejared.crafttweaker.api.annotation.BracketValidator;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.bracket.BracketValidators;
import com.blamejared.crafttweaker.api.logging.CommonLoggers;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.core.ModRegistries;
import net.minecraft.resources.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@ZenRegister
@Document("mods/vampirism/BracketHandlers")
@ZenCodeType.Name("mods.vampirism.BracketHandlers")
public class SkillBracket {

    @SuppressWarnings("unused")
    @ZenCodeType.Method
    @BracketValidator("skill")
    public static boolean validateSkill(String tokens) {
        if (ResourceLocation.tryParse(tokens) == null) {
            CommonLoggers.zenCode().error("Invalid Bracket Syntax: <skill:" + tokens + ">! Syntax is <skill:modid:skill_id>");
            return false;
        }

        return BracketValidators.validateBracket("skill", tokens, SkillBracket::getSkill);
    }

    /**
     * Gets the give {@link ISkill}. Throws an Exception if not found
     *
     * @param tokens What you would write in the BEP call.
     *
     * @return The found {@link ISkill}
     *
     * @docParam tokens "vampirism:master_brewer"
     */
    @ZenCodeType.Method
    @BracketResolver("skill")
    public static ISkill<?> getSkill(String tokens) {

        final int length = tokens.split(":").length;
        if (length != 2) {
            throw new IllegalArgumentException("Could not get skill <skill:" + tokens + ">");
        }
        final ResourceLocation resourceLocation = new ResourceLocation(tokens);

        ISkill<?> skill = ModRegistries.SKILLS.get().getValue(resourceLocation);
        if (skill == null) {
            throw new IllegalArgumentException("Could not get skill <skill:" + tokens + ">");
        }
        return skill;
    }

    public static String getCommandString(ISkill<?> skill) {
        return "<skill:" + ModRegistries.SKILLS.get().getKey(skill) + ">";
    }

    @ZenCodeType.Method
    @BracketDumper("skill")
    public static Collection<String> getSkillDump(){
        return ModRegistries.SKILLS.get().getValues().stream().map(SkillBracket::getCommandString).collect(Collectors.toList());
    }
}
