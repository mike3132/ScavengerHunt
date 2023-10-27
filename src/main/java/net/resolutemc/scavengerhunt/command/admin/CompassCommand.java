package net.resolutemc.scavengerhunt.command.admin;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.command.api.SubCommand;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import net.resolutemc.scavengerhunt.util.StringPlaceholders;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompassCommand extends SubCommand {

    public CompassCommand(ScavengerHunt plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            ChatMessage.send(sender, "Not-Enough-Args-Admin");
            return;
        }

        if (!sender.hasPermission("ScavengerHunt.Command.Admin.Compass")) {
            ChatMessage.send(sender, "Admin-Head-List-Permission");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            ChatMessage.send(sender, "Admin-Target-Not-Found");
            return;
        }

        target.getInventory().addItem(this.plugin.getItemManager().getCompassItem());
        ChatMessage.send(target, "Admin-Compass-Item-Given-Player");
        ChatMessage.send(sender, "Admin-Compass-Item-Given-Admin", StringPlaceholders.of(
                "target", target.getName()
        ));

    }

}
