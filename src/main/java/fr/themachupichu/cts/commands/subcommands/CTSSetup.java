package fr.themachupichu.cts.commands.subcommands;

import fr.themachupichu.cts.listeners.player.PlayerJoin;
import fr.themachupichu.cts.Main;
import fr.themachupichu.cts.utils.FileManager;
import fr.themachupichu.cts.utils.Files;
import fr.themachupichu.cts.utils.TempManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class CTSSetup {
    private static final String doneColor = "§a";
    private static final String undoneColor = "§c";

    public static void init(Player player) {
        YamlConfiguration spawnConfig = FileManager.getValues().get(Files.Spawn);
        YamlConfiguration sheepConfig = FileManager.getValues().get(Files.Sheep);
        YamlConfiguration kitsConfig = FileManager.getValues().get(Files.Kits);
        YamlConfiguration zoneConfig = FileManager.getValues().get(Files.Zone);

        if (!TempManager.isSetup()) {
            StringBuilder message = new StringBuilder();

            String spawnLobby = booleanToColor(spawnConfig.contains("lobby"));
            String spawnBlue = booleanToColor(spawnConfig.contains("blue"));
            String spawnRed = booleanToColor(spawnConfig.contains("red"));
            String spawnWait = booleanToColor(spawnConfig.contains("wait"));

            String zoneBlue = booleanToColor(zoneConfig.contains("blue"));
            String zoneRed = booleanToColor(zoneConfig.contains("red"));

            String sheepBlue = booleanToColor(sheepConfig.contains("blue"));
            String sheepRed = booleanToColor(sheepConfig.contains("red"));

            String kitDefault = booleanToColor(kitsConfig.contains("default"));

            message.append("------------ CTS Setup ------------")
                    .append("\n");

            if (spawnBlue.equals(doneColor)
                    && spawnLobby.equals(doneColor)
                    && spawnRed.equals(doneColor)
                    && spawnWait.equals(doneColor)
                    && zoneBlue.equals(doneColor)
                    && zoneRed.equals(doneColor)
                    && sheepBlue.equals(doneColor)
                    && sheepRed.equals(doneColor)
                    && kitDefault.equals(doneColor)) {

                message.append("§aPlugin initialization completed.")
                        .append("\n");

                TempManager.getConfiguration().set("isSetup", true);
                TempManager.save();

                Bukkit.getPluginManager().disablePlugin(Main.getInstance());
                Bukkit.getPluginManager().enablePlugin(Main.getInstance());

                PlayerJoin.sendLobby(player);
            } else {
                message.append("1) Places spawns (/cts setspawn) : ")
                        .append(spawnBlue)
                        .append("Blue§f, ")
                        .append(spawnRed)
                        .append("Red§f, ")
                        .append(spawnWait)
                        .append("Wait §fand ")
                        .append(spawnLobby)
                        .append("Lobby§f.")
                        .append("\n");

                message.append("2) Set the zones (/cts setzone) : ")
                        .append(zoneBlue)
                        .append("Blue §fand ")
                        .append(zoneRed)
                        .append("Red§f.")
                        .append("\n");

                message.append("3) Set up the sheep (/cts setsheep) : ")
                        .append(sheepBlue)
                        .append("Blue §fand ")
                        .append(sheepRed)
                        .append("Red§f.")
                        .append("\n");

                message.append("4) Set the default kit (/cts setkit) : ")
                        .append(kitDefault)
                        .append("default§f.")
                        .append("\n");
            }

            message.append("-----------------------------------");

            player.sendMessage(message.toString());
        }
    }

    private static String booleanToColor(Boolean a) {
        if (a)
            return doneColor;
        else
            return undoneColor;
    }
}
