package fr.themachupichu.cts.commands.subcommands;

import fr.themachupichu.cts.Language;
import fr.themachupichu.cts.utils.PlayersManager;
import org.bukkit.entity.Player;

public class CTSStats {
    public static void init(Player player) {
        StringBuilder message = new StringBuilder();

        for (String msg : Language.getInstance().getStatsMessage()) {
            message.append(msg
                    .replaceAll("%death%", PlayersManager.getDeaths(player) + "")
                    .replaceAll("%kill%", PlayersManager.getKills(player) + "")
                    .replaceAll("%ratio%", String.valueOf((PlayersManager.getKills(player) + 1) / (PlayersManager.getDeaths(player) + 1))));
            message.append("\n");
        }

        String messageSend = message.toString();
        player.sendMessage(messageSend);
    }
}
