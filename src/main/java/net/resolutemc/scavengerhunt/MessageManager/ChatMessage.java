package net.resolutemc.scavengerhunt.MessageManager;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ChatMessage {

    public static void sendMessage(Player player, String key) {
        File messagesConfig = new File(ScavengerHunt.getInstance().getDataFolder(), "messages.yml");
        YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
        String message = configMessages.getString("Messages.Prefix") + configMessages.getString("Messages." + key);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendPlayerPlaceholderMessage(Player player, String key, String target) {
        File messagesConfig = new File(ScavengerHunt.getInstance().getDataFolder(), "messages.yml");
        YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
        String message = configMessages.getString("Messages.Prefix") + configMessages.getString("Messages." + key);
        message = message.replace("%playerName%", target);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendHeadsFoundPlaceholderMessage(Player player, String key, String target) {
        File messagesConfig = new File(ScavengerHunt.getInstance().getDataFolder(), "messages.yml");
        YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
        String message = configMessages.getString("Messages.Prefix") + configMessages.getString("Messages." + key);
        message = message.replace("%ScavengerHunt_Heads_Found%", target);
        message = message.replace("%playerName%", player.getName());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendHeadsTotalPlacedPlaceholderMessage(Player player, String key, String target) {
        File messagesConfig = new File(ScavengerHunt.getInstance().getDataFolder(), "messages.yml");
        YamlConfiguration configMessages = YamlConfiguration.loadConfiguration(messagesConfig);
        String message = configMessages.getString("Messages.Prefix") + configMessages.getString("Messages." + key);
        message = message.replace("%ScavengerHunt_Heads_Total_World%", target);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

}
