package net.resolutemc.scavengerhunt.CommandManager;

import net.resolutemc.scavengerhunt.MessageManager.ChatMessage;
import net.resolutemc.scavengerhunt.MessageManager.ConsoleMessage;
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
            player.sendMessage("You have claimed " + player.getPersistentDataContainer().get(blockKey, PersistentDataType.INTEGER));
            return false;
        }


        return false;
    }

}
