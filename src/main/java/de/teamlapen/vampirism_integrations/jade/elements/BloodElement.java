package de.teamlapen.vampirism_integrations.jade.elements;

import de.teamlapen.vampirism_integrations.jade.JadePlugin;
import de.teamlapen.vampirism_integrations.util.REFERENCE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.Identifiers;
import snownee.jade.api.theme.IThemeHelper;
import snownee.jade.api.ui.Element;
import snownee.jade.impl.config.PluginConfig;
import snownee.jade.overlay.DisplayHelper;

public class BloodElement extends Element {
    static final ResourceLocation ICONS = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "textures/gui/icons.png");

    private final int maxBlood;
    private final int blood;
    private final String text;

    public BloodElement(int maxBlood, int blood) {
        if (!PluginConfig.INSTANCE.get(JadePlugin.ENTITY_BLOOD_SHOW_FRACTION)) {
            maxBlood = Mth.ceil(maxBlood);
            blood = Mth.ceil(blood);
        }
        this.maxBlood = maxBlood;
        this.blood = blood;
        this.text = String.format("  %s/%s", DisplayHelper.dfCommas.format((double)blood), DisplayHelper.dfCommas.format((double)maxBlood));
    }

    public Vec2 getSize() {
        if (this.maxBlood > PluginConfig.INSTANCE.getInt(JadePlugin.ENTITY_BLOOD_MAX_FOR_RENDER)) {
            Font font = Minecraft.getInstance().font;
            return new Vec2((float)(8 + font.width(this.text)), 10.0F);
        } else {
            int maxDrops = PluginConfig.INSTANCE.getInt(JadePlugin.ENTITY_BLOOD_ICONS_PER_LINE);
            int maxBlood = (int) (this.maxBlood * 0.5F);
            int heartsPerLine = Math.min(maxDrops, maxBlood);
            int lineCount = (int)Math.ceil((double) maxBlood / maxDrops);
            return new Vec2((float)(8 * heartsPerLine), (float)(10 * lineCount));
        }
    }

    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        int maxDrops = PluginConfig.INSTANCE.getInt(JadePlugin.ENTITY_BLOOD_ICONS_PER_LINE);
        int maxDropsForRender = PluginConfig.INSTANCE.getInt(JadePlugin.ENTITY_BLOOD_MAX_FOR_RENDER);
        boolean showNumbers = this.maxBlood > maxDropsForRender;
        int dropCount = showNumbers ? 1 : Mth.ceil(this.maxBlood * 0.5F);
        float blood = showNumbers ? 1.0F : this.blood * 0.5F;
        int dropsPerLine = (int)Math.min((double)maxDrops, Math.ceil((double)this.maxBlood));
        int xOffset = 0;

        for(int i = 1; i <= dropCount; ++i) {
            guiGraphics.blit(ICONS, (int)x + xOffset, (int)y, 0,0, 9, 9);
            if (i <= Mth.floor(blood)) {
                guiGraphics.blit(ICONS, (int)x + xOffset, (int)y, 9,0, 9, 9);
                xOffset += 8;
            }

            if ((float)i > blood && (float)i < blood + 1.0F) {
                guiGraphics.blit(ICONS, (int)x + xOffset, (int)y, 18,0, 9, 9);
                xOffset += 8;
            }

            if ((float)i >= blood + 1.0F) {
                xOffset += 8;
            }

            if (!showNumbers && i % dropsPerLine == 0) {
                y += 10.0F;
                xOffset = 0;
            }
        }

        if (showNumbers) {
            DisplayHelper.INSTANCE.drawText(guiGraphics, this.text, x + 8.0F, y, IThemeHelper.get().getNormalColor());
        }

    }

    public @Nullable String getMessage() {
        return I18n.get("narration.vampirism.health", DisplayHelper.dfCommas.format((double)this.blood));
    }
}
