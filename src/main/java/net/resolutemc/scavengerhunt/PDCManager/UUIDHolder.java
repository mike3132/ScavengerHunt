package net.resolutemc.scavengerhunt.PDCManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UUIDHolder implements Serializable {

    /*TODO: Make this a placeholder to be used other places.

     */

    public UUIDHolder() {
        this.uuids = new ArrayList<>();
    }

    private final List<UUID> uuids;

    public UUIDHolder(List<UUID> playerUUID) {
        this.uuids = playerUUID;
    }

    public List<UUID> getUUIDs() {
        return uuids;
    }





}
