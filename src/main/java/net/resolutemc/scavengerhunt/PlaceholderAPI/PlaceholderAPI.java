package net.resolutemc.scavengerhunt.PlaceholderAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.resolutemc.scavengerhunt.PDCManager.LocationHolder;
import net.resolutemc.scavengerhunt.PDCManager.LocationType;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlaceholderAPI extends PlaceholderExpansion {

    NamespacedKey blockKey = new NamespacedKey(ScavengerHunt.getInstance(), "Block-Key");

    @Override
    public @NotNull String getIdentifier() {
        return "ScavengerHunt";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Mike3132";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String getRequiredPlugin() {
        return "ScavengerHunt";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String string) {
        if (player == null) return "";
        if (string.equalsIgnoreCase("heads_found")) {
            return String.valueOf(player.getPlayer().getPersistentDataContainer().get(blockKey, PersistentDataType.INTEGER));
        }
        if (string.equalsIgnoreCase("heads_total_world")) {
            LocationHolder holder =  player.getPlayer().getWorld().getPersistentDataContainer().get(blockKey, new LocationType());
            return String.valueOf(holder.getLocations().size());
        }


        return null;
    }
}
