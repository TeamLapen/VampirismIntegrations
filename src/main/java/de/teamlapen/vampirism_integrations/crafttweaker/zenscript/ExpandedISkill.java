package de.teamlapen.vampirism_integrations.crafttweaker.zenscript;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;
import com.blamejared.crafttweaker_annotations.annotations.TaggableElement;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;

@ZenRegister
@Document("mods/vampirism/skills/ISkill")
@NativeTypeRegistration(value = ISkill.class, zenCodeName = "mods.vampirism.skills.ISkill")
public class ExpandedISkill {
}
