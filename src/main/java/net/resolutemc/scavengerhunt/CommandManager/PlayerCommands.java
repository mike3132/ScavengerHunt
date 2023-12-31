package net.resolutemc.scavengerhunt.CommandManager;

import net.resolutemc.scavengerhunt.MessageManager.ChatMessage;
import net.resolutemc.scavengerhunt.MessageManager.ConsoleMessage;
import net.resolutemc.scavengerhunt.PDCManager.LocationHolder;
import net.resolutemc.scavengerhunt.PDCManager.LocationType;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class PlayerCommands implements CommandExecutor {

    public PlayerCommands() {
        ScavengerHunt.getInstance().getCommand("Heads").setExecutor(this);
    }

    NamespacedKey blockKey = new NamespacedKey(ScavengerHunt.getInstance(), "Block-Key");

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            ConsoleMessage.sendMessage(sender, "Console-Not-Player");
            return false;
        }

        if (!sender.hasPermission("ScavengerHunt.Command.Heads")) {
            ConsoleMessage.sendMessage(sender, "No-Permission");
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            ChatMessage.sendMessage(player, "Not-Enough-Args-Player");
            return false;
        }

        if (args[0].equalsIgnoreCase("Found")) {
            if (!player.hasPermission("ScavengerHunt.Command.Heads.Found")) {
                ChatMessage.sendMessage(player, "Player-No-Permission-Message");
                return false;
            }

            if (!player.getPersistentDataContainer().has(blockKey, PersistentDataType.INTEGER)) {
                ChatMessage.sendMessage(player, "Player-List-NeverClaimed-Message");
                return false;
            }
            String amount = String.valueOf(player.getPersistentDataContainer().get(blockKey, PersistentDataType.INTEGER));
            ChatMessage.sendHeadsFoundPlaceholderMessage(player, "Player-Message-Head-Found-Amount", amount);
            return false;
        }
        if (args[0].equalsIgnoreCase("Total")) {
            if (!player.hasPermission("ScavengerHunt.Command.Heads.Total")) {
                ChatMessage.sendMessage(player, "Player-No-Permission-Message");
                return false;
            }
            LocationHolder holder = player.getPlayer().getWorld().getPersistentDataContainer().get(blockKey, new LocationType());
            String amount = String.valueOf(holder.getLocations().size());
            ChatMessage.sendHeadsTotalPlacedPlaceholderMessage(player, "Player-Head-Total-World", amount);
            return false;
        }


        return false;
    }

}
