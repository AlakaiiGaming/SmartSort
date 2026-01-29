package com.Alakaii.smartsort.utils;

import com.Alakaii.smartsort.SmartSortPlugin;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker {

    private final SmartSortPlugin plugin;
    private final String currentVersion;
    private String latestVersion;
    private boolean updateAvailable;
    private static final String MODRINTH_URL = "https://modrinth.com/plugin/smartsort";

    // Replace with your actual Modrinth project ID when you publish
    private static final String MODRINTH_PROJECT_ID = "smartsort"; // Change this to your project slug
    private static final String MODRINTH_API = "https://api.modrinth.com/v2/project/%s/version";

    public UpdateChecker(SmartSortPlugin plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
        this.updateAvailable = false;
    }

    /**
     * Check for updates asynchronously
     */
    public void checkForUpdates() {
        // Don't check if disabled in config
        if (!plugin.getConfig().getBoolean("check-for-updates", true)) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // Build API URL
                    String apiUrl = String.format(MODRINTH_API, MODRINTH_PROJECT_ID);
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("User-Agent", "SmartSort/" + currentVersion);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    int responseCode = connection.getResponseCode();

                    if (responseCode == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Parse JSON response
                        JsonArray versions = JsonParser.parseString(response.toString()).getAsJsonArray();

                        if (versions.size() > 0) {
                            JsonObject latestVersionObj = versions.get(0).getAsJsonObject();
                            latestVersion = latestVersionObj.get("version_number").getAsString();

                            // Compare versions
                            if (!currentVersion.equals(latestVersion)) {
                                updateAvailable = true;

                                // Schedule notification on main thread
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        notifyUpdate();
                                    }
                                }.runTask(plugin);
                            }
                        }
                    } else if (responseCode == 404) {
                        // Project not found on Modrinth yet - that's okay
                        plugin.getLogger().info("Update checker: Plugin not yet published on Modrinth");
                    }

                } catch (Exception e) {
                    // Silently fail - don't spam console if Modrinth is down or project doesn't exist
                    if (plugin.getConfig().getBoolean("verbose", false)) {
                        plugin.getLogger().warning("Could not check for updates: " + e.getMessage());
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * Notify console and online admins about update
     */
    private void notifyUpdate() {
        // Log to console
        plugin.getLogger().info("========================================");
        plugin.getLogger().info("A new version of SmartSort is available!");
        plugin.getLogger().info("Current: v" + currentVersion + " | Latest: v" + latestVersion);
        plugin.getLogger().info("Download: " + MODRINTH_URL);
        plugin.getLogger().info("========================================");

        // Notify online admins with clickable link
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("smartsort.admin")) {
                sendUpdateMessage(player);
            }
        }
    }

    /**
     * Check if an update is available
     */
    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    /**
     * Get the latest version string
     */
    public String getLatestVersion() {
        return latestVersion;
    }

    /**
     * Get the Modrinth URL
     */
    public String getModrinthUrl() {
        return MODRINTH_URL;
    }

    /**
     * Notify a specific player about available update
     */
    public void notifyPlayer(Player player) {
        if (!updateAvailable || !player.hasPermission("smartsort.admin")) {
            return;
        }
        sendUpdateMessage(player);
    }

    /**
     * Send clickable update message to a player
     */
    private void sendUpdateMessage(Player player) {
        // First line: Update available message
        TextComponent message = new TextComponent("§e§l[SmartSort] §aA new version is available! §7(v" + currentVersion + " → v" + latestVersion + ")");
        player.spigot().sendMessage(message);

        // Second line: Clickable download link
        TextComponent prefix = new TextComponent("§e§l[SmartSort] §7Download: ");
        TextComponent link = new TextComponent("§b§n" + MODRINTH_URL);
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, MODRINTH_URL));
        link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§aClick to open Modrinth")));

        prefix.addExtra(link);
        player.spigot().sendMessage(prefix);
    }
}