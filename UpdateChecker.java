package com.Alakaii.smartsort.utils;

import com.Alakaii.smartsort.SmartSortPlugin;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Checks Modrinth for a newer SmartSort release built for this server's Minecraft version.
 */
public class UpdateChecker {

    private static final String MODRINTH_URL = "https://modrinth.com/plugin/smartsort";
    private static final String MODRINTH_PROJECT = "smartsort";

    private final SmartSortPlugin plugin;
    private final String currentVersion;
    private volatile String latestVersion;
    private volatile boolean updateAvailable;

    public UpdateChecker(SmartSortPlugin plugin) {
        this.plugin = plugin;
        this.currentVersion = plugin.getPluginMeta().getVersion();
    }

    public void checkForUpdates() {
        if (!plugin.getConfig().getBoolean("check-for-updates", true)) {
            return;
        }

        String loaders = URLEncoder.encode("[\"paper\"]", StandardCharsets.UTF_8);
        String gameVersions = URLEncoder.encode("[\"" + Bukkit.getMinecraftVersion() + "\"]", StandardCharsets.UTF_8);
        URI uri = URI.create("https://api.modrinth.com/v2/project/" + MODRINTH_PROJECT
                + "/version?loaders=" + loaders + "&game_versions=" + gameVersions);

        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(5))
                .header("User-Agent", "AlakaiiGaming/SmartSort/" + currentVersion)
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() != 200) {
                        if (plugin.getConfigManager().isVerbose()) {
                            plugin.getLogger().info("Update check returned HTTP " + response.statusCode());
                        }
                        return;
                    }
                    JsonArray versions = JsonParser.parseString(response.body()).getAsJsonArray();
                    if (versions.isEmpty()) {
                        return;
                    }
                    String newest = versions.get(0).getAsJsonObject().get("version_number").getAsString();
                    if (isNewer(newest, currentVersion)) {
                        latestVersion = newest;
                        updateAvailable = true;
                        Bukkit.getScheduler().runTask(plugin, this::notifyUpdate);
                    }
                })
                .exceptionally(error -> {
                    if (plugin.getConfigManager().isVerbose()) {
                        plugin.getLogger().warning("Could not check for updates: " + error.getMessage());
                    }
                    return null;
                });
    }

    /** True if version a is higher than version b (compares numbers like 2.0.10 > 2.0.9). */
    static boolean isNewer(String a, String b) {
        // "2.0.1+26.2" or "2.0.1-beta" -> "2.0.1"
        String[] pa = a.split("[+-]")[0].replaceAll("[^0-9.]", "").split("\\.");
        String[] pb = b.split("[+-]")[0].replaceAll("[^0-9.]", "").split("\\.");
        for (int i = 0; i < Math.max(pa.length, pb.length); i++) {
            int na = i < pa.length && !pa[i].isEmpty() ? Integer.parseInt(pa[i]) : 0;
            int nb = i < pb.length && !pb[i].isEmpty() ? Integer.parseInt(pb[i]) : 0;
            if (na != nb) {
                return na > nb;
            }
        }
        return false;
    }

    private void notifyUpdate() {
        plugin.getLogger().info("A new version of SmartSort is available! Current: v" + currentVersion
                + " | Latest: v" + latestVersion + " | Download: " + MODRINTH_URL);
        for (Player player : Bukkit.getOnlinePlayers()) {
            notifyPlayer(player);
        }
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getModrinthUrl() {
        return MODRINTH_URL;
    }

    public void notifyPlayer(Player player) {
        if (!updateAvailable || !player.hasPermission("smartsort.admin")) {
            return;
        }
        try {
            sendFancyNotice(player);
        } catch (Throwable error) {
            // Plain-text fallback so an update notice can never break anything
            player.sendMessage("[SmartSort] A new version is available (v" + currentVersion + " -> v"
                    + latestVersion + "): " + MODRINTH_URL);
        }
    }

    private void sendFancyNotice(Player player) {
        Component prefix = Component.text("[SmartSort] ", NamedTextColor.YELLOW, TextDecoration.BOLD);

        player.sendMessage(prefix.append(Component.text("A new version is available! ", NamedTextColor.GREEN)
                .decoration(TextDecoration.BOLD, false)
                .append(Component.text("(v" + currentVersion + " → v" + latestVersion + ")", NamedTextColor.GRAY))));

        player.sendMessage(prefix.append(Component.text("Download: ", NamedTextColor.GRAY)
                .decoration(TextDecoration.BOLD, false)
                .append(Component.text(MODRINTH_URL, NamedTextColor.AQUA, TextDecoration.UNDERLINED)
                        .clickEvent(ClickEvent.openUrl(MODRINTH_URL))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to open Modrinth", NamedTextColor.GREEN))))));
    }
}
