package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism.util.Helper;
import net.minecraft.entity.EntityLivingBase;
import toughasnails.potion.PotionHypothermia;

public class PotionHypothermiaVampirism extends PotionHypothermia {
    public PotionHypothermiaVampirism(int id) {
        super(id);
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (Helper.isVampire(entity)) {
            //No effect to vampires
        } else {
            super.performEffect(entity, amplifier);
        }
    }
}
