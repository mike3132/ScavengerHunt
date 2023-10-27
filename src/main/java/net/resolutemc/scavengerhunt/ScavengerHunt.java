package net.resolutemc.scavengerhunt;

import net.resolutemc.scavengerhunt.command.AdminCommands;
import net.resolutemc.scavengerhunt.hook.Placeholders;
import net.resolutemc.scavengerhunt.listener.AdminListeners;
import net.resolutemc.scavengerhunt.listener.PlayerListeners;
import net.resolutemc.scavengerhunt.manager.DataManager;
import net.resolutemc.scavengerhunt.manager.ItemManager;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class ScavengerHunt extends JavaPlugin {

    private static ScavengerHunt instance;
    private DataManager dataManager;
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        instance = this;

        // Load the config files.
        this.getConfig(); // config.yml
        ChatMessage.reload(); // messages.yml

        // Load the info file.
        final File infoFile = new File(getDataFolder(), "info.yml");
        if (!infoFile.exists()) {
            this.saveResource("info.yml", false);
        }

        // Register Managers
        this.dataManager = new DataManager(this);
        this.itemManager = new ItemManager(this);

        // Register Commands
        new AdminCommands(this);

        // Register Events
        Bukkit.getPluginManager().registerEvents(new AdminListeners(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(this), this);

        // Register PlaceholderAPI
        new Placeholders().register();
        this.getServer().getConsoleSender().sendMessage(ChatMessage.color("&2Enabled"));
    }

    /**
     * Reload the config files.
     */
    public void reload() {
        this.reloadConfig();
        ChatMessage.reload();
        this.itemManager.reload();
    }

    @Override
    public void onDisable() {
        this.getServer().getConsoleSender().sendMessage(ChatMessage.color("&4Disabled"));
    }

    public static ScavengerHunt getInstance() {
        return instance;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

}
