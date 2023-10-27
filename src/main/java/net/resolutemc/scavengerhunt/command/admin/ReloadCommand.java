package net.resolutemc.scavengerhunt.command.admin;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.command.api.SubCommand;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {

    public ReloadCommand(ScavengerHunt plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        long start = System.currentTimeMillis();
        this.plugin.reload();
        ChatMessage.custom(sender, "&2Config reload in &a" + (System.currentTimeMillis() - start + " &2ms"));
        ChatMessage.custom(sender, "&4*NOTICE* &cReloading the config will not modify certain aspects of the plugin");
        ChatMessage.custom(sender, "&cThis is due to the block data being stored in the world.");
        ChatMessage.custom(sender, "&cPlease restart your server for reloads to take full effect");
    }

}
