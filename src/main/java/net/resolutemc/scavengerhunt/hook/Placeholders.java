package net.resolutemc.scavengerhunt.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.resolutemc.scavengerhunt.data.DataKeys;
import net.resolutemc.scavengerhunt.data.holder.LocationHolder;
import net.resolutemc.scavengerhunt.data.type.LocationType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Placeholders extends PlaceholderExpansion {

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) return null;

        return switch (params.toLowerCase()) {
            case "heads_found" -> String.valueOf(player.getPersistentDataContainer().getOrDefault(DataKeys.BLOCK_KEY, PersistentDataType.INTEGER, 0));
            case "heads_total_world" -> {
                LocationHolder holder = player.getWorld().getPersistentDataContainer().get(DataKeys.BLOCK_KEY, new LocationType());
                if (holder == null || holder.getLocations().isEmpty())
                    yield "0";

                yield String.valueOf(holder.getLocations().size());
            }

            default -> null;
        };
    }


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

}
