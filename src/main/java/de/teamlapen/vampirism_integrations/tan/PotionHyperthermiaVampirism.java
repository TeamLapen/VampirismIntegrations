package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism.util.Helper;
import net.minecraft.entity.EntityLivingBase;
import toughasnails.potion.PotionHyperthermia;

public class PotionHyperthermiaVampirism extends PotionHyperthermia {
    public PotionHyperthermiaVampirism(int id) {
        super(id);
    }


    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (Helper.isVampire(entity)) {
            //Damage twice. Need to reset hurt resistant timer otherwise second damage will be blocked
            int hurtResistantTime = entity.hurtResistantTime;
            super.performEffect(entity, amplifier);
            entity.hurtResistantTime = hurtResistantTime;
        }
        super.performEffect(entity, amplifier);
    }
}
