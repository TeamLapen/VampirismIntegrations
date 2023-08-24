package de.teamlapen.vampirism_integrations.waila;

import de.teamlapen.vampirism_integrations.util.REFERENCE;
import mcp.mobius.waila.api.ITooltipComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BloodComponent implements ITooltipComponent {
    static final ResourceLocation ICONS = new ResourceLocation(REFERENCE.VAMPIRISM_ID, "textures/gui/icons.png");

    private final int blood;
    private final int iconCount;
    private final int lineWidth;

    public BloodComponent(int blood, int maxBlood, int maxPerLine) {
        this.blood = blood;
        this.iconCount = Mth.positiveCeilDiv(Mth.ceil(Math.max(blood, maxBlood)), 2);
        this.lineWidth = Math.min(iconCount, maxPerLine);
    }

    @Override
    public int getWidth() {
        return (lineWidth * 9) + 1;
    }

    @Override
    public int getHeight() {
        return (Mth.positiveCeilDiv(iconCount, lineWidth) * 3) + 6;
    }

    @Override
    public void render(GuiGraphics ctx, int x, int y, float delta) {
        int filled = blood / 2 - 1;
        int half = filled + blood % 2;

        for (int i = iconCount - 1; i >= 0; i--) {
            int ix = x + ((i % lineWidth) * 8);
            int iy = y + ((i / lineWidth) * 3);

            ctx.blit(ICONS, ix, iy, 0, 0, 9, 9);
            if (i <= filled) {
                ctx.blit(ICONS, ix, iy, 9, 0, 9, 9);
            } else if (i == half) {
                ctx.blit(ICONS, ix, iy, 18, 0, 9, 9);
            }
        }
    }
}
