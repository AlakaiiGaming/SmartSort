package com.Alakaii.smartsort.utils;

import com.Alakaii.smartsort.SmartSortPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;

public class MessageUtil {

    private static final LegacyComponentSerializer AMPERSAND = LegacyComponentSerializer.legacyAmpersand();

    private final SmartSortPlugin plugin;

    public MessageUtil(SmartSortPlugin plugin) {
        this.plugin = plugin;
    }

    /** Sends a message from the config's messages section. Empty messages are skipped. */
    public void sendMessage(CommandSender sender, String key) {
        String message = plugin.getConfig().getString("messages." + key, "&cMessage not found: " + key);
        if (message == null || message.isEmpty()) {
            return;
        }
        sender.sendMessage(colorize(message));
    }

    /** Turns a string using &-color codes into a chat component. */
    public Component colorize(String message) {
        return AMPERSAND.deserialize(message);
    }
}
