package net.resolutemc.scavengerhunt.command;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.command.admin.AdminCommand;
import net.resolutemc.scavengerhunt.command.admin.AmountCommand;
import net.resolutemc.scavengerhunt.command.admin.ClearCommand;
import net.resolutemc.scavengerhunt.command.admin.CompassCommand;
import net.resolutemc.scavengerhunt.command.admin.InternalCommand;
import net.resolutemc.scavengerhunt.command.admin.PlacedCommand;
import net.resolutemc.scavengerhunt.command.admin.ReloadCommand;
import net.resolutemc.scavengerhunt.command.admin.ResetCommand;
import net.resolutemc.scavengerhunt.command.admin.TotalCommand;
import net.resolutemc.scavengerhunt.command.api.SubCommand;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminCommands implements TabExecutor {

    private final Map<String, SubCommand> subCommands;

    public AdminCommands(ScavengerHunt plugin) {
        this.subCommands = new HashMap<>();

        // Register the base command.
        PluginCommand command = plugin.getCommand("Hunt");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);

            // Register the sub commands.
            this.subCommands.put("admin", new AdminCommand(plugin));
            this.subCommands.put("amount", new AmountCommand(plugin));
            this.subCommands.put("clear", new ClearCommand(plugin));
            this.subCommands.put("compass", new CompassCommand(plugin));
            this.subCommands.put("placed", new PlacedCommand(plugin));
            this.subCommands.put("reload", new ReloadCommand(plugin));
            this.subCommands.put("reset", new ResetCommand(plugin));
            this.subCommands.put("total", new TotalCommand(plugin));
            this.subCommands.put("SCAVENGERHUNTINTERNALCLICKCOMMAND", new InternalCommand(plugin));
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            ChatMessage.send(sender, "Not-Enough-Args-Admin");
            return true;
        }

        if (!sender.hasPermission("ScavengerHunt.Command.ScavengerHunt")) {
            ChatMessage.send(sender, "No-Permission");
            return true;
        }

        final SubCommand subCommand = this.subCommands.get(args[0].toLowerCase());
        if (subCommand == null) {
            ChatMessage.send(sender, "No-Valid-Args-Found");
            return true;
        }

        subCommand.execute(sender, args);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> arguments = new ArrayList<>(this.subCommands.keySet());
        arguments.remove("SCAVENGERHUNTINTERNALCLICKCOMMAND");

        if (args.length == 0) {
            return arguments;
        }

        if (args.length > 1)
            if (List.of("amount", "compass", "reset").contains(args[0].toLowerCase())) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }

        return arguments.stream()
                .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }

}
