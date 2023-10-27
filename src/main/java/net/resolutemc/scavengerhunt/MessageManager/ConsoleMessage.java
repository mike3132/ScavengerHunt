package net.resolutemc.scavengerhunt.MessageManager;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ConsoleMessage {

    public static void sendMessage(CommandSender sender, String key) {
        File messagesConfig = new File(ScavengerHunt.getInstance().getDataFolder(), "messages.yml");
        YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
        String message = configMessages.getString("Messages.Prefix") + configMessages.getString("Messages." + key);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendPlayerPlaceholderMessage(CommandSender sender, String key, String target) {
        File messagesConfig = new File(ScavengerHunt.getInstance().getDataFolder(), "messages.yml");
        YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
        String message = configMessages.getString("Messages.Prefix") + configMessages.getString("Messages." + key);
        message = message.replace("%playerName%", target);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    // May not need
    //public static void sendPlaceholderMessage(CommandSender sender, String key, String target) {
    //    File messagesConfig = new File(ScavengerHunt.getInstance().getDataFolder(), "messages.yml");
    //    YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
    //    String message = configMessages.getString("Messages.Prefix") + configMessages.getString("Messages." + key);
    //    message = message.replace("%playerName%", target);
    //    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    //}
}
