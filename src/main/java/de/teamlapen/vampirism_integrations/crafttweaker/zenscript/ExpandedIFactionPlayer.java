package de.teamlapen.vampirism_integrations.crafttweaker.zenscript;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;
import de.teamlapen.vampirism.api.entity.player.IFactionPlayer;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;

@ZenRegister
@Document("mods/vampirism/player/IFactionPlayer")
@NativeTypeRegistration(value = IFactionPlayer.class, zenCodeName = "mods.vampirism.player.IFactionPlayer")
public class ExpandedIFactionPlayer {
}
