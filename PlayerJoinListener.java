package com.Alakaii.smartsort.listeners;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final SmartSortPlugin plugin;

    public PlayerJoinListener(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("smartsort.admin")) {
            return;
        }

        // Wait 3 seconds so the notice doesn't get lost among join messages
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                plugin.getUpdateChecker().notifyPlayer(player);
            }
        }, 60L);
    }
}
