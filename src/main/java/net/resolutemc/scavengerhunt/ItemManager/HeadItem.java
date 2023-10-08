package net.resolutemc.scavengerhunt.ItemManager;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.resolutemc.scavengerhunt.MessageManager.ColorTranslate;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class HeadItem {

    private ItemStack head;

    public HeadItem() {
        this.createHeadItem();
    }

    public ItemStack getHead() {
        return this.head;
    }


    public void createHeadItem() {
        HeadDatabaseAPI hdbAPI = new HeadDatabaseAPI();
        String headId = ScavengerHunt.getInstance().getConfig().getString("Hdb-Head-Id");
        String headName = ScavengerHunt.getInstance().getConfig().getString("Head-Name-Inventory");
        List<String> lore = new ArrayList<>();

        ItemStack item = hdbAPI.getItemHead(headId);
        ItemMeta meta = item.getItemMeta();

        for (String realLore : ScavengerHunt.getInstance().getConfig().getStringList("Head-Lore-List")) {
            lore.add(ColorTranslate.chatColor(realLore));
        }

        meta.setDisplayName(ColorTranslate.chatColor(headName));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(ScavengerHunt.getInstance(), "ScavengerHunt-Head-Key");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "ScavengerHunt-Head-Item");

        item.setItemMeta(meta);

        head = item;
    }
}
