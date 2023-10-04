package net.resolutemc.scavengerhunt;

import net.resolutemc.scavengerhunt.CommandManager.AdminCommands;
import net.resolutemc.scavengerhunt.CommandManager.PlayerCommands;
import net.resolutemc.scavengerhunt.ConfigManager.ConfigCreator;
import net.resolutemc.scavengerhunt.EventManager.AdminEvent;
import net.resolutemc.scavengerhunt.EventManager.PlayerEvent;
import net.resolutemc.scavengerhunt.MessageManager.ColorTranslate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScavengerHunt extends JavaPlugin {

    private static ScavengerHunt INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        // Plugin startup logic

        // Write a check and message for if head database is installed on the server.
        getServer().getConsoleSender().sendMessage(ColorTranslate.chatColor("&2Enabled"));

        // Command loaders
        registerAdminCommand();
        registerPlayerCommand();

        // Event loaders
        Bukkit.getPluginManager().registerEvents(new AdminEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEvent(), this);

        // Config loaders
        ConfigCreator.MESSAGES.create();
        saveDefaultConfig();
        getConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(ColorTranslate.chatColor("&4Disabled"));
    }

    // Command registers
    private void registerAdminCommand() {
        new AdminCommands();
    }

    private void registerPlayerCommand() {
        new PlayerCommands();
    }

    // Plugin instance register
    public static ScavengerHunt getInstance() {
        return INSTANCE;
    }
}
