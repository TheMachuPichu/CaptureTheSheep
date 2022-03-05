package fr.themachupichu.cts.listeners.player;

import fr.themachupichu.cts.utils.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerDrop(PlayerDropItemEvent event) {
        if (GameManager.getInGamePlayers().contains(event.getPlayer()))
            event.setCancelled(true);
    }
}
