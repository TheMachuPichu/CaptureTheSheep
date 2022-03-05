package fr.themachupichu.cts.utils;

import fr.themachupichu.cts.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;

public class PlayersManager {
    private static HashMap<String, YamlConfiguration> playersFiles;

    public PlayersManager(boolean silent) {
        playersFiles = new HashMap<>();

        new File(Main.getInstance().getDataFolder() + "/Players/").mkdir();

        int number = 0;
        for (File files : Objects.requireNonNull(new File(Main.getInstance().getDataFolder() + "/Players/").listFiles())) {
            if (!playersFiles.containsKey(files.getName().replaceAll(".yml", ""))) {
                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(files);
                playersFiles.put(files.getName().replaceAll(".yml", ""), yamlConfiguration);
                number++;
            }
        }

        if (!silent)
            System.out.println(Main.ANSI_GREEN + "Loading of " + number + " player data completed." + Main.ANSI_RESET);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            try {
                saveAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 36000, 36000);
    }

    public static void create(Player player) throws IOException {
        if (!playersFiles.containsKey(player.getName())) {
            File playerFile = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getName() + ".yml");

            if (!playerFile.exists()) {
                playerFile.createNewFile();

                YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(playerFile);

                yamlConfiguration.set("Morts", 0);
                yamlConfiguration.set("Kills", 0);
                yamlConfiguration.set("Kit", "default");

                yamlConfiguration.save(playerFile);

                playersFiles.put(player.getName(), yamlConfiguration);
            }
        }
    }

    public static void save(Player player) throws IOException {
        File file = new File(Main.getInstance().getDataFolder() + "/Players/" + player.getName() + ".yml");
        playersFiles.get(player.getName()).save(file);
    }

    public static void saveAll() throws IOException {
        for (String player : playersFiles.keySet()) {
            if (player == null) continue;

            File file = new File(Main.getInstance().getDataFolder() + "/Players/" + player + ".yml");
            playersFiles.get(player).save(file);
        }
    }

    public static YamlConfiguration getPlayer(Player player) {
        return playersFiles.get(player.getName());
    }

    public static HashMap<String, YamlConfiguration> getPlayers() {
        return playersFiles;
    }

    public static Object getKit(Player player) {
        return playersFiles.get(player.getName()).get("Kit");
    }

    public static void setKit(Player player, String kit) throws IOException {
        playersFiles.get(player.getName()).set("Kit", kit);
        save(player);
    }

    public static int getDeaths(Player player) {
        return playersFiles.get(player.getName()).getInt("Morts");
    }

    public static void addDeath(Player player, Integer number) throws IOException {
        playersFiles.get(player.getName()).set("Morts", playersFiles.get(player.getName()).getInt("Morts") +  number);
        save(player);
    }

    public static int getKills(Player player) {
        return playersFiles.get(player.getName()).getInt("Kills");
    }

    public static void addKills(Player player, Integer number) throws IOException {
        playersFiles.get(player.getName()).set("Kills", playersFiles.get(player.getName()).getInt("Kills") +  number);
        save(player);
    }
}
