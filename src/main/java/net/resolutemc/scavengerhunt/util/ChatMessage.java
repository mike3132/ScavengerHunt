package net.resolutemc.scavengerhunt.util;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ChatMessage {

    private static FileConfiguration messageConfig;

    static {
        reload(); // Load the config
    }

    public static void reload() {
        File messageFile = new File(ScavengerHunt.getInstance().getDataFolder(), "messages.yml");
        if (!messageFile.exists()) {
            ScavengerHunt.getInstance().saveResource("messages.yml", false);
        }

        messageConfig = YamlConfiguration.loadConfiguration(messageFile);
    }

    /**
     * Send a basic message to someone from the config.
     *
     * @param sender The  person to send the message to.
     * @param key    The key of the message in the config.
     */
    public static void send(CommandSender sender, String key) {
        final String prefix = messageConfig.getString("Messages.Prefix");

        sender.sendMessage(color(prefix + messageConfig.getString("Messages." + key)));
    }

    /**
     * Send a message to someone from the config with placeholders.
     *
     * @param sender       The person to send the message to.
     * @param key          The key of the message in the config.
     * @param placeholders The placeholders to apply to the message.
     */
    public static void send(CommandSender sender, String key, StringPlaceholders placeholders) {
        final String prefix = messageConfig.getString("Messages.Prefix");

        sender.sendMessage(color(prefix + placeholders.apply(messageConfig.getString("Messages." + key))));
    }

    /**
     * Send a custom message to someone.
     *
     * @param sender  The person to send the message to.
     * @param message The message to send.
     */
    public static void custom(CommandSender sender, String message) {
        final String prefix = messageConfig.getString("Messages.Prefix");

        sender.sendMessage(color(prefix + message));
    }

    /**
     * Color a message using the '&' character.
     *
     * @param message The message to color.
     * @return The colored message.
     */
    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
