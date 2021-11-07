package de.teamlapen.vampirism_integrations.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.BracketResolver;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.core.ModRegistries;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Locale;

@ZenRegister
@Document("mods/vampirism/BracketHandlers")
@ZenCodeType.Name("mods.vampirism.BracketHandlers")
public class SkillBracket {

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
    public static ISkill getSkill(String tokens) {
        if(!tokens.toLowerCase(Locale.ENGLISH).equals(tokens)) {
            CraftTweakerAPI.logWarning("Skill BEP <skill:%s> does not seem to be lower-cased!", tokens);
        }

        final String[] split = tokens.split(":");
        if(split.length != 2)
            throw new IllegalArgumentException("Could not get skill with name: <skill:" + tokens + ">! Syntax is <skill:modid:skillname>");
        ResourceLocation key = new ResourceLocation(split[0], split[1]);
        if(!ModRegistries.SKILLS.containsKey(key)) {
            throw new IllegalArgumentException("Could not get skill with name: <skill:" + tokens + ">! Skill does not appear to exist!");
        }

        return ModRegistries.SKILLS.getValue(key);
    }
}
