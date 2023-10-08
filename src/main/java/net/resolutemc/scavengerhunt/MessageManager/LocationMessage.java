package net.resolutemc.scavengerhunt.MessageManager;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.resolutemc.scavengerhunt.PDCManager.LocationHolder;
import net.resolutemc.scavengerhunt.PDCManager.LocationType;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LocationMessage {


    private static String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }


    public static void sendMessage(World world, Player player) {
        NamespacedKey blockKey = new NamespacedKey(ScavengerHunt.getInstance(), "Block-Key");
        LocationHolder holder =  world.getPersistentDataContainer().get(blockKey, new LocationType());
        ChatMessage.sendMessage(player, "Admin-Head-List-Header");

        for (Location location : holder.getLocations()) {
            TextComponent message = new TextComponent(ColorTranslate.chatColor("&6Hover to view head location"));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorTranslate.chatColor("&eClick to teleport to: &3" + locationToString(location))).create()));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/teleport " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ()));
            player.spigot().sendMessage(message);
        }
    }


}
