package net.resolutemc.scavengerhunt.command.admin;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.command.api.SubCommand;
import net.resolutemc.scavengerhunt.data.DataKeys;
import net.resolutemc.scavengerhunt.data.holder.LocationHolder;
import net.resolutemc.scavengerhunt.data.type.LocationType;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class PlacedCommand extends SubCommand {

    public PlacedCommand(ScavengerHunt plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (!player.hasPermission("ScavengerHunt.Command.Admin.Placed")) {
            ChatMessage.send(player, "Admin-Head-List-Permission");
            return;
        }

        LocationHolder holder = player.getWorld().getPersistentDataContainer().get(DataKeys.BLOCK_KEY, new LocationType());
        if (holder == null || holder.getLocations().isEmpty()) {
            ChatMessage.send(player, "Admin-No-Heads");
            return;
        }

        for (Location loc : holder.getLocations()) {
            TextComponent message = new TextComponent(ChatMessage.color("&6Hover to view head location"));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatMessage.color("&eClick to teleport to: &3" + this.format(loc))).create()));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/teleport " + String.format("%s %s %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())));
            player.spigot().sendMessage(message);
        }

    }

    /**
     * Format a location into "world, x, y, z
     *
     * @param location The location to format.
     * @return The formatted location.
     */
    private String format(Location location) {
        return String.format(
                "%s, %s, %s, %s",
                location.getWorld(),
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );

    }

}
