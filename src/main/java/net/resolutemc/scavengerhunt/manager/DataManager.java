package net.resolutemc.scavengerhunt.manager;

import net.resolutemc.scavengerhunt.ScavengerHunt;

import java.util.HashSet;
import java.util.UUID;

/**
 * This class is used to handle cache / data saving throughout the whole plugin
 * <p>
 * Do not make individual classes to handle caches unless you are going to serialize it
 * to a file or database.
 */
public class DataManager {

    private final ScavengerHunt plugin;
    private final HashSet<UUID> admins; // Used for /hunt admin

    public DataManager(ScavengerHunt plugin) {
        this.plugin = plugin;
        this.admins = new HashSet<>();
    }

    public HashSet<UUID> getAdminSet() {
        return admins;
    }

}
