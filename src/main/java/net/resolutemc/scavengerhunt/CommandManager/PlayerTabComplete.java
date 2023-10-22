package net.resolutemc.scavengerhunt.CommandManager;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerTabComplete implements TabCompleter {

    public PlayerTabComplete() {
        ScavengerHunt.getInstance().getCommand("Heads").setTabCompleter(this);
    }

    List<String> arguments = new ArrayList<>();

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("Found");
            arguments.add("Total");
        }
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            for (String string : arguments) {
                if (string.toLowerCase().startsWith(args[0].toLowerCase())) result.add(string);
            }
            return result;
        }
        return null;
    }

}
