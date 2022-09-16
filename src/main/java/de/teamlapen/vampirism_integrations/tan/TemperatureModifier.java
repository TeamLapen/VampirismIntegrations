package de.teamlapen.vampirism_integrations.tan;

import de.teamlapen.vampirism.util.Helper;
import net.minecraft.world.entity.player.Player;
import toughasnails.api.temperature.IPlayerTemperatureModifier;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

public class TemperatureModifier implements IPlayerTemperatureModifier {

    static void register() {
        TemperatureHelper.registerPlayerTemperatureModifier(new TemperatureModifier());
    }

    @Override
    public TemperatureLevel modify(Player player, TemperatureLevel temperatureLevel) {
        return (temperatureLevel == TemperatureLevel.ICY || temperatureLevel == TemperatureLevel.COLD) && Helper.isVampire(player) ? TemperatureLevel.NEUTRAL : temperatureLevel;
    }
}
