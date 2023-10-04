package net.resolutemc.scavengerhunt.SetManager;

import java.util.HashSet;
import java.util.UUID;

public class AdminSet {

    private final static HashSet<UUID> admins = new HashSet<>();

    public static HashSet<UUID> getAdmins() {
        return admins;
    }

    public static void addAdmins(UUID player) {
        getAdmins().add(player);
    }

    public static void removeAdmins(UUID player) {
        getAdmins().remove(player);
    }
}
