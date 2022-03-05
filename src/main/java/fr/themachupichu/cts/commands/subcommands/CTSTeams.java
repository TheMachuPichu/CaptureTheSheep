package fr.themachupichu.cts.commands.subcommands;

import fr.ChadOW.cinventory.CInventory;
import fr.ChadOW.cinventory.CItem;
import fr.ChadOW.cinventory.ItemCreator;
import fr.themachupichu.cts.Language;
import fr.themachupichu.cts.Settings;
import fr.themachupichu.cts.utils.FileManager;
import fr.themachupichu.cts.utils.Files;
import fr.themachupichu.cts.utils.GameManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.Set;

public class CTSTeams {
    public static void init(Player player) {
        if (GameManager.getInGamePlayers().contains(player)) {
            YamlConfiguration config = FileManager.getValues().get(Files.Config);

            CInventory cInventory = new CInventory(Settings.getInstance().getGUITeamsSize(), Settings.getInstance().getGUITeamsTitle());

            CItem blueTeam = new CItem(new ItemCreator(Material.getMaterial(config.getString("GUI.Teams.Items.BlueTeam.Material")), 0)
                    .setName(config.getString("GUI.Teams.Items.BlueTeam.Title"))
                    .setLores(config.getStringList("GUI.Teams.Items.BlueTeam.Lore")))
                    .setSlot(config.getInt("GUI.Teams.Items.BlueTeam.Location"));

            blueTeam.addEvent((cInventory1, cItem, player1, clickContext) -> {
                if (GameManager.getNumberPlayers().containsKey(player.getWorld())) {

                    Set<Player> teamPlayers = GameManager.getBlueTeam().get(player.getWorld());
                    teamPlayers.add(player);
                    GameManager.getBlueTeam().put(player.getWorld(), teamPlayers);

                    player.sendMessage(Language.getInstance().getSuccessChooseTeam()
                            .replaceAll("%team%", Language.getInstance().getTeamsNameBlue()));
                }
            });
            cInventory.addElement(blueTeam);

            CItem redTeam = new CItem(new ItemCreator(Material.getMaterial(config.getString("GUI.Teams.Items.RedTeam.Material")), 0)
                    .setName(config.getString("GUI.Teams.Items.RedTeam.Title"))
                    .setLores(config.getStringList("GUI.Teams.Items.RedTeam.Lore")))
                    .setSlot(config.getInt("GUI.Teams.Items.RedTeam.Location"));

            redTeam.addEvent((cInventory1, cItem, player1, clickContext) -> {
                if (GameManager.getNumberPlayers().containsKey(player.getWorld())) {

                    Set<Player> teamPlayers = GameManager.getRedTeam().get(player.getWorld());
                    teamPlayers.add(player);
                    GameManager.getRedTeam().put(player.getWorld(), teamPlayers);

                    player.sendMessage(Language.getInstance().getSuccessChooseTeam()
                            .replaceAll("%team%", Language.getInstance().getTeamsNameRed()));
                }
            });
            cInventory.addElement(redTeam);

            cInventory.open(player);
        }
    }
}
