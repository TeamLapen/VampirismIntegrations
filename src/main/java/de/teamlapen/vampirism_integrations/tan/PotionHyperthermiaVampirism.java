package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        super.renderInventoryEffect(x, y, effect, mc);

        if (Helper.isVampire(mc.player)) {
            String s1 = I18n.format(getName());

            mc.fontRenderer.drawStringWithShadow(s1, (float) (x + 10 + 18), (float) (y + 6), 16777215);
            String s = "Vampirism";
            mc.fontRenderer.drawStringWithShadow(s, (float) (x + 10 + 18), (float) (y + 6 + 10), 8355711);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRenderInvText(PotionEffect effect) {
        return false;
    }
}
