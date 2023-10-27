package net.resolutemc.scavengerhunt.command.admin;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.command.api.SubCommand;
import net.resolutemc.scavengerhunt.data.DataKeys;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class AdminCommand extends SubCommand {

    public AdminCommand(ScavengerHunt plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (!player.hasPermission("ScavengerHunt.Command.Admin")) {
            ChatMessage.send(player, "No-Admin-Mode-Permission");
            return;
        }

        // Only triggers when a player was removed from the set.
        if (this.plugin.getDataManager().getAdminSet().remove(player.getUniqueId())) {
            ChatMessage.send(player, "Admin-Mode-Deactivated");

            for (ItemStack item : player.getInventory().getContents()) {
                if (item == null || item.getItemMeta() == null) continue;
                if (item.getItemMeta().getPersistentDataContainer().has(DataKeys.ITEM_KEY, PersistentDataType.STRING)) {
                    item.setAmount(0);
                }
            }

            return;
        }

        ChatMessage.send(player, "Admin-Mode-Activated");
        ChatMessage.custom(sender, "&4WARNING: &cDo not place heads on the side of walls or at odd heights/directions");
        ChatMessage.custom(sender, "&cThe plugin see's the heads as solid blocks. Not player heads.");

        this.plugin.getDataManager().getAdminSet().add(player.getUniqueId());
        player.getInventory().addItem(this.plugin.getItemManager().getHeadItem());
    }

}
