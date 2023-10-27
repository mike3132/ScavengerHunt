package net.resolutemc.scavengerhunt.manager;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.data.DataKeys;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ItemManager {

    private final ScavengerHunt plugin;
    private final HeadDatabaseAPI headDatabase = new HeadDatabaseAPI();

    private ItemStack headItem, compassItem;

    public ItemManager(ScavengerHunt plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        this.loadHead();
        this.loadCompass();
    }

    /**
     * Load the head item from the config.
     */
    private void loadHead() {
        String headId = ScavengerHunt.getInstance().getConfig().getString("Hdb-Head-Id");
        String headName = ScavengerHunt.getInstance().getConfig().getString("Head-Name-Inventory");

        final ItemStack item = this.headDatabase.getItemHead(headId).clone();
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        meta.setDisplayName(ChatMessage.color(headName));
        meta.setLore(new ArrayList<>(this.plugin.getConfig().getStringList("Head-Lore-List"))
                .stream().map(ChatMessage::color)
                .collect(Collectors.toList()));

        meta.getPersistentDataContainer().set(DataKeys.ITEM_KEY, PersistentDataType.STRING, "ScavengerHunt-Head-Item");
        item.setItemMeta(meta);

        this.headItem = item;
    }

    /**
     * Load the compass item from the config.
     */
    private void loadCompass() {
        final ItemStack item = new ItemStack(Material.COMPASS);
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        String headName = ScavengerHunt.getInstance().getConfig().getString("Compass-Name");
        meta.setDisplayName(ChatMessage.color(headName));
        meta.setLore(new ArrayList<>(this.plugin.getConfig().getStringList("Compass-Lore-List"))
                .stream().map(ChatMessage::color)
                .collect(Collectors.toList()));

        meta.getPersistentDataContainer().set(DataKeys.COMPASS_KEY, PersistentDataType.STRING, "ScavengerHunt-Compass-Item");
        item.setItemMeta(meta);

        this.compassItem = item;
    }

    public ItemStack getHeadItem() {
        return this.headItem.clone();
    }

    public ItemStack getCompassItem() {
        return this.compassItem.clone();
    }

}
