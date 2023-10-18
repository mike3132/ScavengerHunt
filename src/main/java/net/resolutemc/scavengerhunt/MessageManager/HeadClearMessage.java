package net.resolutemc.scavengerhunt.MessageManager;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class HeadClearMessage {

    public static void sendClearClickMessage(Player player) {
        player.sendMessage(ColorTranslate.chatColor("&4WARNING: &cThis will remove all heads and their data from the world"));
        player.sendMessage(ColorTranslate.chatColor("&4WARNING: &cIt's recommended to manually remove all heads using the /heads placed command"));

        TextComponent message = new TextComponent(ColorTranslate.chatColor("&4WARNING: &cIf you wish to proceed please click this message"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorTranslate.chatColor("&bAre you sure? There is no going back")).create()));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hunt SCAVENGERHUNTINTERNALCLICKCOMMAND"));
        player.spigot().sendMessage(message);
    }



}
