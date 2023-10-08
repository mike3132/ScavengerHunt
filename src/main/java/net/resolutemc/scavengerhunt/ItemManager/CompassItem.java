package net.resolutemc.scavengerhunt.ItemManager;

import net.resolutemc.scavengerhunt.MessageManager.ColorTranslate;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class CompassItem {

    private ItemStack compass;

    public CompassItem() {
        this.createCompassItem();
    }

    public ItemStack getCompass() {
        return this.compass;
    }

    public void createCompassItem() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        String headName = ScavengerHunt.getInstance().getConfig().getString("Compass-Name");
        List<String> lore = new ArrayList<>();

        for (String realLore : ScavengerHunt.getInstance().getConfig().getStringList("Compass-Lore-List")) {
            lore.add(ColorTranslate.chatColor("" + realLore));
        }
        meta.setDisplayName(ColorTranslate.chatColor(headName));
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(ScavengerHunt.getInstance(), "ScavengerHunt-Compass-Key");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "ScavengerHunt-Compass-Item");

        item.setItemMeta(meta);

        compass = item;
    }

}
