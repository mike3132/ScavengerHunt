package net.resolutemc.scavengerhunt.MessageManager;

import org.bukkit.ChatColor;

public class ColorTranslate {

    public static String chatColor(String chatColor) {
        return ChatColor.translateAlternateColorCodes('&', chatColor);
    }

}
