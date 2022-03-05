package fr.themachupichu.cts;

import fr.ChadOW.cinventory.CUtils;
import fr.ChadOW.cinventory.ItemCreator;
import fr.themachupichu.cts.commands.CTS;
import fr.themachupichu.cts.commands.CTSTab;
import fr.themachupichu.cts.listeners.block.BlockBreak;
import fr.themachupichu.cts.listeners.block.BlockPlace;
import fr.themachupichu.cts.listeners.entity.EntityDamage;
import fr.themachupichu.cts.listeners.entity.EntityExplode;
import fr.themachupichu.cts.listeners.entity.EntitySpawn;
import fr.themachupichu.cts.listeners.food.FoodLevelChange;
import fr.themachupichu.cts.listeners.player.*;
import fr.themachupichu.cts.utils.*;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends JavaPlugin {
    public static String ANSI_RESET = "";
    public static String ANSI_BLACK = "";
    public static String ANSI_RED = "";
    public static String ANSI_GREEN = "";
    public static String ANSI_YELLOW = "";
    public static String ANSI_BLUE = "";
    public static String ANSI_PURPLE = "";
    public static String ANSI_CYAN = "";
    public static String ANSI_WHITE = "";
    private static Main Instance;

    private final List<Listener> listeners = new ArrayList<>(Arrays.asList(
            new BlockBreak(),
            new BlockPlace(),
            new EntityDamage(),
            new EntityExplode(),
            new EntitySpawn(),
            new FoodLevelChange(),
            new PlayerDeath(),
            new PlayerDropItem(),
            new PlayerInteract(),
            new PlayerInteractAtEntity(),
            new PlayerJoin(),
            new PlayerQuit()
    ));

    public static void setLobbyInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[0]);

        YamlConfiguration config = FileManager.getValues().get(Files.Config);

        for (String items : config.getConfigurationSection("LobbyItems").getKeys(false)) {
            player.getInventory().setItem(config.getInt("LobbyItems." + items + ".Location"),
                    new ItemCreator(Material.getMaterial(config.getString("LobbyItems." + items + ".Material")), 0)
                            .setName(config.getString("LobbyItems." + items + ".Title"))
                            .setLores(config.getStringList("LobbyItems." + items + ".Lore"))
                            .getItem());
        }
    }

    @Override
    public void onDisable() {
        if (TempManager.isSetup())
            SheepManager.removeAll();
    }

    @Override
    public void onEnable() {
        Instance = this;

        saveDefaultConfig();

        if (getConfig().getBoolean("ColorConsole")) {
            ANSI_RESET = "\u001B[0m";
            ANSI_BLACK = "\u001B[30m";
            ANSI_RED = "\u001B[31m";
            ANSI_GREEN = "\u001B[32m";
            ANSI_YELLOW = "\u001B[33m";
            ANSI_BLUE = "\u001B[34m";
            ANSI_PURPLE = "\u001B[35m";
            ANSI_CYAN = "\u001B[36m";
            ANSI_WHITE = "\u001B[37m";
        }

        System.out.println(ANSI_BLUE + "  ____  " + ANSI_WHITE + " _____  " + ANSI_RED + " ____" + ANSI_RESET);
        System.out.println(ANSI_BLUE + " / ___| " + ANSI_WHITE + "|_   _| " + ANSI_RED + "/ ___|" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "| |     " + ANSI_WHITE + "  | |   " + ANSI_RED + "\\___ \\" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "| |___  " + ANSI_WHITE + "  | |   " + ANSI_RED + " ___) |" + ANSI_RESET);
        System.out.println(ANSI_BLUE + " \\____|" + ANSI_WHITE + "   |_|   " + ANSI_RED + "|____/" + ANSI_RESET);
        System.out.println(" ");

        System.out.println(ANSI_CYAN + "Loading configuration files in progress ..." + ANSI_RESET);
        try {
            new FileManager(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(ANSI_CYAN + "Loading current player data in progress ..." + ANSI_RESET);
        new PlayersManager(false);

        if (TempManager.isSetup()) {
            System.out.println(ANSI_CYAN + "Loading worlds in progress ..." + ANSI_RESET);
            new WorldManager(false);

            System.out.println(ANSI_CYAN + "Loading games in progress ..." + ANSI_RESET);
            new GameManager(false);

            System.out.println(ANSI_CYAN + "Finalization of the loading of the plugin in progress ..." + ANSI_RESET);

            for (Listener listener : listeners)
                getServer().getPluginManager().registerEvents(listener, this);

            new SheepManager();

            CUtils.init(this);

            System.out.println(ANSI_CYAN + "Loading of the finalized plugin." + ANSI_RESET);
        } else {
            System.out.println(ANSI_CYAN + "Waiting for the setup of the ingame plugin." + ANSI_RESET);

            getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        }

        getCommand("CaptureTheSheep").setExecutor(new CTS());
        getCommand("CaptureTheSheep").setTabCompleter(new CTSTab());

        System.out.println(" ");
    }

    public static Main getInstance() {
        return Instance;
    }
}
