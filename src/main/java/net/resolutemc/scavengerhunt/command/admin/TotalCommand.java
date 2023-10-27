package net.resolutemc.scavengerhunt.command.admin;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.command.api.SubCommand;
import net.resolutemc.scavengerhunt.data.DataKeys;
import net.resolutemc.scavengerhunt.data.holder.LocationHolder;
import net.resolutemc.scavengerhunt.data.type.LocationType;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import net.resolutemc.scavengerhunt.util.StringPlaceholders;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TotalCommand extends SubCommand {

    public TotalCommand(ScavengerHunt plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;


        if (!player.hasPermission("ScavengerHunt.Command.Admin.Total")) {
            ChatMessage.send(player, "Admin-Head-List-Permission");
            return;
        }

        LocationHolder holder = player.getWorld().getPersistentDataContainer().get(DataKeys.BLOCK_KEY, new LocationType());
        if (holder == null || holder.getLocations().isEmpty()) {
            ChatMessage.send(player, "Admin-No-Heads");
            return;
        }

        ChatMessage.send(player, "Admin-Head-Total-World", StringPlaceholders.of(
                "amount", String.valueOf(holder.getLocations().size())
        ));

    }

}
