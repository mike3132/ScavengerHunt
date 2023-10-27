package net.resolutemc.scavengerhunt.data.holder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * TODO: Make this a placeholder to be used other places.
 */
public class UUIDHolder implements Serializable {

    private final List<UUID> uuids = new ArrayList<>();

    public List<UUID> getUUIDs() {
        return uuids;
    }

}
