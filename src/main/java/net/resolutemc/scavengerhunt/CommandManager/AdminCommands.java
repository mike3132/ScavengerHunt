package net.resolutemc.scavengerhunt.CommandManager;

import net.resolutemc.scavengerhunt.ItemManager.HeadItem;
import net.resolutemc.scavengerhunt.MessageManager.ChatMessage;
import net.resolutemc.scavengerhunt.MessageManager.ColorTranslate;
import net.resolutemc.scavengerhunt.MessageManager.ConsoleMessage;
import net.resolutemc.scavengerhunt.PDCManager.LocationHolder;
import net.resolutemc.scavengerhunt.PDCManager.LocationType;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.SetManager.AdminSet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class AdminCommands implements CommandExecutor {

    public AdminCommands() {
        ScavengerHunt.getInstance().getCommand("Hunt").setExecutor(this);
    }

    NamespacedKey blockKey = new NamespacedKey(ScavengerHunt.getInstance(), "Block-Key");

    /*
    TODO: Rewrite this to be a switch statement instead of a ton of if statements
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            ConsoleMessage.sendMessage(sender, "Console-Not-Player");
            return false;
        }

        if (!sender.hasPermission("ScavengerHunt.Command.ScavengerHunt")) {
            ConsoleMessage.sendMessage(sender, "No-Permission");
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            ChatMessage.sendMessage(player, "Not-Enough-Args");
            return false;
        }

        if (!player.hasPermission("ScavengerHunt.Command.Admin")) {
            ChatMessage.sendMessage(player, "No-Admin-Mode-Permission");
            return false;
        }

        if (args[0].equalsIgnoreCase("Admin")) {
            if (AdminSet.getAdmins().contains(player.getUniqueId())) {
                AdminSet.removeAdmins(player.getUniqueId());
                ChatMessage.sendMessage(player, "Admin-Mode-Deactivated");
                for (ItemStack item : player.getInventory().getContents()) {
                    NamespacedKey key = new NamespacedKey(ScavengerHunt.getInstance(), "Scavenger-Head-Key");
                    if (item == null) continue;
                    if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                        item.setAmount(0);
                    }
                }
                return false;
            }
            ChatMessage.sendMessage(player, "Admin-Mode-Activated");
            AdminSet.addAdmins(player.getUniqueId());
            HeadItem headItem = new HeadItem();
            ItemStack head = headItem.getHead();
            player.getInventory().addItem(head);
            return false;
        }
        /*
        TODO: Make this location print look nice, and possibly a placeholder
         */
        if (args[0].equalsIgnoreCase("List")) {
            if (!player.hasPermission("ScavengerHunt.Command.Admin.List")) {
                ChatMessage.sendMessage(player, "Admin-Head-List-Permission");
                return false;
            }
            World world = player.getWorld();

            if (!world.getPersistentDataContainer().has(blockKey, new LocationType())) {
                ChatMessage.sendMessage(player, "Admin-No-Heads");
                return false;
            }
            LocationHolder holder =  world.getPersistentDataContainer().get(blockKey, new LocationType());

            for (Location location : holder.getLocations()) {
                player.sendMessage(ColorTranslate.chatColor("&2Locations") + location.toString());
            }
            return false;
        }

        if (args[0].equalsIgnoreCase("Reset")) {
            if (!player.hasPermission("ScavengerHunt.Command.Admin.Reset")) {
                ChatMessage.sendMessage(player, "Admin-Head-Reset-Permission");
                return false;
            }
            if (args.length < 2) {
                ChatMessage.sendMessage(player, "Not-Enough-Args");
                return false;
            }
            Player target = (Bukkit.getPlayer(args[1]));
            if (target == null) {
                ChatMessage.sendMessage(player, "Admin-Target-Not-Found");
                return false;
            }

            /*
            TODO: Make a line in the messages.yml for this
             */
            if (!target.getPersistentDataContainer().has(blockKey, PersistentDataType.INTEGER))  {
                player.sendMessage(target.getName() + " Has not found any heads, Unable to reset");
                return false;
            }
            player.sendMessage("Resetting claimed heads for " + target.getName());
            target.getPersistentDataContainer().set(blockKey, PersistentDataType.INTEGER, 0);
            return false;
        }

        if (args[0].equalsIgnoreCase("Amount")) {
            if (args.length < 2) {
                ChatMessage.sendMessage(player, "Not-Enough-Args");
                return false;
            }
            Player target = (Bukkit.getPlayer(args[1]));
            if (target == null) {
                ChatMessage.sendMessage(player, "Admin-Target-Not-Found");
                return false;
            }

            /*
            TODO: Make a line in the messages.yml for this
             */
            if (!target.getPersistentDataContainer().has(blockKey, PersistentDataType.INTEGER)) {
                player.sendMessage(target.getName() + " Has not found any heads");
                return false;
            }
            player.sendMessage(target.getName() + " Has claimed " + target.getPersistentDataContainer().get(blockKey, PersistentDataType.INTEGER) + " heads");
            return false;
        }

        /*
        TODO: REMOVE THIS AFTER DEV TESTING
         */
        if (args[0].equalsIgnoreCase("Test")) {
            player.sendMessage("Admin commands are working");
            return false;
        }

        return false;
    }

}
