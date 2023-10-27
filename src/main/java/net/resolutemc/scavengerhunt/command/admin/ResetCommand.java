package net.resolutemc.scavengerhunt.command.admin;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.command.api.SubCommand;
import net.resolutemc.scavengerhunt.data.DataKeys;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import net.resolutemc.scavengerhunt.util.StringPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class ResetCommand extends SubCommand {

    public ResetCommand(ScavengerHunt plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length < 2) {
            ChatMessage.send(player, "Not-Enough-Args-Admin");
            return;
        }

        if (!player.hasPermission("ScavengerHunt.Command.Admin.Reset")) {
            ChatMessage.send(player, "Admin-Head-ListOther-Permission");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            ChatMessage.send(player, "Admin-Target-Not-Found");
            return;
        }

        Integer amount = target.getPersistentDataContainer().get(DataKeys.BLOCK_KEY, PersistentDataType.INTEGER);
        if (amount == null) {
            ChatMessage.send(player, "Admin-Head-Reset-NeverClaimed-Message", StringPlaceholders.of("target", target.getName()));
            return;
        }

        target.getPersistentDataContainer().set(DataKeys.BLOCK_KEY, PersistentDataType.INTEGER, 0);

        ChatMessage.send(player, "Admin-Head-Reset-Admin-Message", StringPlaceholders.of(
                "target", target.getName(),
                "amount", String.valueOf(amount)
        ));

    }
}
