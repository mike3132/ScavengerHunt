package net.resolutemc.scavengerhunt.CommandManager;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminTabComplete implements TabCompleter {

    public AdminTabComplete() {
        ScavengerHunt.getInstance().getCommand("Hunt").setTabCompleter(this);
    }

    List<String> arguments = new ArrayList<>();

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("Reload");
            arguments.add("Admin");
            arguments.add("Placed");
            arguments.add("Total");
            arguments.add("Compass");
            arguments.add("Amount");
            arguments.add("Reset");
            arguments.add("Clear");
        }
        List<String> result = new ArrayList<>();

        if (args.length == 1) {
            for (String string : arguments) {
                if (string.toLowerCase().startsWith(args[0].toLowerCase())) result.add(string);
            }
            return result;
        }
        arguments.clear();
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("AMOUNT")) {
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) return null;
                arguments.add(target.getName());
                for (String string : arguments) {
                    if (string.toLowerCase().startsWith(args[0].toLowerCase())) result.add(string);
                }
                return result;
            }
            if (args[1].equalsIgnoreCase("RESET")) {
                Player target = Bukkit.getPlayer(args[2]);
                if (target == null) return null;
                arguments.add(target.getName());
                for (String string : arguments) {
                    if (string.toLowerCase().startsWith(args[0].toLowerCase())) result.add(string);
                }
                return result;
            }
        }
        return null;
    }

}
