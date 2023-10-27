package net.resolutemc.scavengerhunt.command;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.command.api.SubCommand;
import net.resolutemc.scavengerhunt.command.player.FoundCommand;
import net.resolutemc.scavengerhunt.command.player.TotalCommand;
import net.resolutemc.scavengerhunt.data.holder.LocationHolder;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerCommands implements TabExecutor {

    private final Map<String, SubCommand> subCommands;

    public PlayerCommands(ScavengerHunt plugin) {
        this.subCommands = new HashMap<>();

        PluginCommand command = plugin.getCommand("Heads");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);

            this.subCommands.put("found", new FoundCommand(plugin));
            this.subCommands.put("total", new TotalCommand(plugin));
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            ChatMessage.send(sender, "Console-Not-Player");
            return true;
        }

        if (!sender.hasPermission("ScavengerHunt.Command.Heads")) {
            ChatMessage.send(sender, "No-Permission");
            return true;
        }

        if (args.length == 0) {
            ChatMessage.send(player, "Not-Enough-Args-Player");
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

        if (args.length == 0) {
            return arguments;
        }

        return arguments.stream()
                .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }

}
