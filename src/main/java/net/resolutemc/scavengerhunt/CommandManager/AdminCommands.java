package net.resolutemc.scavengerhunt.CommandManager;

import net.resolutemc.scavengerhunt.ItemManager.CompassItem;
import net.resolutemc.scavengerhunt.ItemManager.HeadItem;
import net.resolutemc.scavengerhunt.MessageManager.*;
import net.resolutemc.scavengerhunt.PDCManager.LocationHolder;
import net.resolutemc.scavengerhunt.PDCManager.LocationType;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.SetManager.AdminSet;
import org.bukkit.*;
import org.bukkit.block.Block;
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

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            ConsoleMessage.sendMessage(sender, "Not-Enough-Args-Admin");
            return false;
        }
        if (!sender.hasPermission("ScavengerHunt.Command.ScavengerHunt")) {
            ConsoleMessage.sendMessage(sender, "No-Permission");
            return false;
        }
        Player player = (Player) sender;

        switch (args[0].toUpperCase()) {
            case "RELOAD":
                ScavengerHunt.getInstance().reloadConfig();
                sender.sendMessage(ColorTranslate.chatColor("&2Config reload in &a" + (System.currentTimeMillis() - 1 + "&2ms")));
                sender.sendMessage(ColorTranslate.chatColor("&4*NOTICE* &cReloading the config will not modify certain aspects of the plugin"));
                sender.sendMessage(ColorTranslate.chatColor("&cThis is due to the block data being stored in the world."));
                sender.sendMessage(ColorTranslate.chatColor("&cPlease restart your server for reloads to take full effect"));
                break;
            case "ADMIN":
                if (!player.hasPermission("ScavengerHunt.Command.Admin")) {
                    ChatMessage.sendMessage(player, "No-Admin-Mode-Permission");
                    return false;
                }
                if (AdminSet.getAdmins().contains(player.getUniqueId())) {
                    AdminSet.removeAdmins(player.getUniqueId());
                    ChatMessage.sendMessage(player, "Admin-Mode-Deactivated");
                    for (ItemStack item : player.getInventory().getContents()) {
                        NamespacedKey itemKey = new NamespacedKey(ScavengerHunt.getInstance(), "ScavengerHunt-Head-Key");
                        if (item == null) continue;
                        if (item.getItemMeta().getPersistentDataContainer().has(itemKey, PersistentDataType.STRING)) {
                            item.setAmount(0);
                        }
                    }
                    return false;
                }
                player.sendMessage(ColorTranslate.chatColor("&4WARNING: &cDo not place heads on the side of walls or at odd heights/directions"));
                player.sendMessage(ColorTranslate.chatColor("&cThe plugin see's the heads as solid blocks. Not player heads."));
                ChatMessage.sendMessage(player, "Admin-Mode-Activated");
                AdminSet.addAdmins(player.getUniqueId());
                HeadItem headItem = new HeadItem();
                ItemStack head = headItem.getHead();
                player.getInventory().addItem(head);
                break;
            case "PLACED": {
                World world = player.getWorld();
                if (!player.hasPermission("ScavengerHunt.Command.Admin.Placed")) {
                    ChatMessage.sendMessage(player, "Admin-Head-List-Permission");
                    return false;
                }
                if (!world.getPersistentDataContainer().has(blockKey, new LocationType())) {
                    ChatMessage.sendMessage(player, "Admin-No-Heads");
                    return false;
                }
                LocationMessage.sendMessage(world, player);
                break;
            }
            case "TOTAL": {
                World world = player.getWorld();
                if (!player.hasPermission("ScavengerHunt.Command.Admin.Total")) {
                    ChatMessage.sendMessage(player, "Admin-Head-List-Permission");
                    return false;
                }
                if (!world.getPersistentDataContainer().has(blockKey, new LocationType())) {
                    ChatMessage.sendMessage(player, "Admin-No-Heads");
                    return false;
                }
                LocationHolder holder = player.getPlayer().getWorld().getPersistentDataContainer().get(blockKey, new LocationType());
                String amount = String.valueOf(holder.getLocations().size());
                ChatMessage.sendHeadsTotalPlacedPlaceholderMessage(player, "Admin-Head-Total-World", amount);
                break;
            }
            case "COMPASS": {
                if (args.length < 2) {
                    ChatMessage.sendMessage(player, "Not-Enough-Args-Admin");
                    return false;
                }
                if (!player.hasPermission("ScavengerHunt.Command.Admin.Compass")) {
                    ChatMessage.sendMessage(player, "Admin-Head-List-Permission");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    ChatMessage.sendMessage(player, "Admin-Target-Not-Found");
                    return false;
                }
                CompassItem compassItem = new CompassItem();
                ItemStack compass = compassItem.getCompass();
                target.getInventory().addItem(compass);
                ChatMessage.sendMessage(target, "Admin-Compass-Item-Given-Player");
                ChatMessage.sendPlayerPlaceholderMessage(player, "Admin-Compass-Item-Given-Admin", target.getName());
                break;
            }
            case "AMOUNT": {
                if (args.length < 2) {
                    ChatMessage.sendMessage(player, "Not-Enough-Args-Admin");
                    return false;
                }
                if (!player.hasPermission("ScavengerHunt.Command.Admin.ListOther")) {
                    ChatMessage.sendMessage(player, "Admin-Head-ListOther-Permission");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    ChatMessage.sendMessage(player, "Admin-Target-Not-Found");
                    return false;
                }

                if (!target.getPersistentDataContainer().has(blockKey, PersistentDataType.INTEGER)) {
                    ChatMessage.sendPlayerPlaceholderMessage(player, "Admin-Head-ListOther-NeverClaimed-Message", target.getName());
                    return false;
                }
                String amount = String.valueOf(target.getPersistentDataContainer().get(blockKey, PersistentDataType.INTEGER));
                ChatMessage.sendHeadsFoundPlaceholderMessage(target, "Admin-Head-ListOther-Amount-Claimed", amount);
                break;
            }
            case "RESET":
                if (args.length < 2) {
                    ChatMessage.sendMessage(player, "Not-Enough-Args-Admin");
                    return false;
                }
                if (!player.hasPermission("ScavengerHunt.Command.Admin.Reset")) {
                    ChatMessage.sendMessage(player, "Admin-Head-Reset-Permission");
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    ChatMessage.sendMessage(player, "Admin-Target-Not-Found");
                    return false;
                }
                if (!target.getPersistentDataContainer().has(blockKey, PersistentDataType.INTEGER)) {
                    ChatMessage.sendPlayerPlaceholderMessage(player, "Admin-Head-Reset-NeverClaimed-Message", target.getName());
                    return false;
                }
                ChatMessage.sendPlayerPlaceholderMessage(player, "Admin-Head-Reset-Admin-Message", target.getName());
                target.getPersistentDataContainer().set(blockKey, PersistentDataType.INTEGER, 0);
                return false;
            case "CLEAR": {
                World world = player.getWorld();
                if (!player.hasPermission("ScavengerHunt.Command.Admin.Clear")) {
                    ChatMessage.sendMessage(player, "Admin-Head-Clear-Permission");
                    return false;
                }
                if (!world.getPersistentDataContainer().has(blockKey, new LocationType())) {
                    ChatMessage.sendMessage(player, "Admin-No-Heads");
                    return false;
                }
                HeadClearMessage.sendClearClickMessage(player);
                break;
            }
            case "SCAVENGERHUNTINTERNALCLICKCOMMAND":
                World world = player.getWorld();
                LocationHolder holder = player.getWorld().getPersistentDataContainer().get(blockKey, new LocationType());

                if (!player.hasPermission("ScavengerHunt.Command.Admin.Clear")) {
                    ChatMessage.sendMessage(player, "Admin-Head-Clear-Permission");
                    return false;
                }
                for (Location location : holder.getLocations()) {
                    Block block = location.getBlock();
                    holder.removeLocation(block.getLocation());
                    world.getPersistentDataContainer().set(blockKey, new LocationType(), holder);
                    block.setType(Material.AIR);
                }
                ChatMessage.sendMessage(player, "Admin-Head-Clear-Message");
                break;


            default:
                ChatMessage.sendMessage(player, "No-Valid-Args-Found");
        }


        return false;
    }
}
