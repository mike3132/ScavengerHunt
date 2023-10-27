package net.resolutemc.scavengerhunt.command.player;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.command.api.SubCommand;
import net.resolutemc.scavengerhunt.data.DataKeys;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import net.resolutemc.scavengerhunt.util.StringPlaceholders;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class FoundCommand extends SubCommand {

    public FoundCommand(ScavengerHunt plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (!player.hasPermission("ScavengerHunt.Command.Heads.Found")) {
            ChatMessage.send(player, "Player-No-Permission-Message");
            return;
        }

        Integer amount = player.getPersistentDataContainer().get(DataKeys.BLOCK_KEY, PersistentDataType.INTEGER);
        if (amount == null) {
            ChatMessage.send(player, "Player-List-NeverClaimed-Message");
            return;
        }

        ChatMessage.send(player, "Player-Message-Head-Found-Amount", StringPlaceholders.of(
                "amount", String.valueOf(amount)
        ));
    }


}
