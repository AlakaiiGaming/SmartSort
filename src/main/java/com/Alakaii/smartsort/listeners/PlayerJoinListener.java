package com.Alakaii.smartsort.listeners;

import com.Alakaii.smartsort.SmartSortPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {
    
    private final SmartSortPlugin plugin;
    
    public PlayerJoinListener(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        // Only notify admins
        if (!player.hasPermission("smartsort.admin")) {
            return;
        }
        
        // Delay notification by 3 seconds so it doesn't get lost in join messages
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getUpdateChecker().notifyPlayer(player);
            }
        }.runTaskLater(plugin, 60L); // 60 ticks = 3 seconds
    }
}
