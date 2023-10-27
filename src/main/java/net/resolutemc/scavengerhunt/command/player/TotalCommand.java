package net.resolutemc.scavengerhunt.command.player;

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

        if (!player.hasPermission("ScavengerHunt.Command.Heads.Total")) {
            ChatMessage.send(player, "Player-No-Permission-Message");
            return;
        }

        LocationHolder holder = ((Player) sender).getWorld().getPersistentDataContainer().getOrDefault(
                DataKeys.BLOCK_KEY,
                new LocationType(),
                new LocationHolder()
        );

        ChatMessage.send(player, "Player-Head-Total-World", StringPlaceholders.of(
                "amount", String.valueOf(holder.getLocations().size())
        ));

    }


}
