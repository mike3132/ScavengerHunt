package net.resolutemc.scavengerhunt.command.admin;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.command.api.SubCommand;
import net.resolutemc.scavengerhunt.data.DataKeys;
import net.resolutemc.scavengerhunt.data.holder.LocationHolder;
import net.resolutemc.scavengerhunt.data.type.LocationType;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("deprecation")
public class ClearCommand extends SubCommand {

    public ClearCommand(ScavengerHunt plugin) {
        super(plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (!player.hasPermission("ScavengerHunt.Command.Admin.Clear")) {
            ChatMessage.send(player, "Admin-Head-ListOther-Permission");
            return;
        }

        final LocationHolder holder = player.getWorld().getPersistentDataContainer().get(DataKeys.BLOCK_KEY, new LocationType());
        if (holder == null || holder.getLocations().isEmpty()) {
            ChatMessage.send(player, "Admin-No-Heads");
            return;
        }

        ChatMessage.custom(player, "&4WARNING: &cThis will remove all heads and their data from the world");
        ChatMessage.custom(player, "&4WARNING: &cIt's recommended to manually remove all heads using the /heads placed command");

        TextComponent message = new TextComponent(ChatMessage.color("&4WARNING: &cIf you wish to proceed please click this message"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatMessage.color("&bAre you sure? There is no going back")).create()));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hunt SCAVENGERHUNTINTERNALCLICKCOMMAND"));
        player.spigot().sendMessage(message);
    }

}