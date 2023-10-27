package net.resolutemc.scavengerhunt.data.holder;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocationHolder implements Serializable {

    private final List<Map<String, Object>> locations = new ArrayList<>();

    public void addLocation(Location location) {
        this.locations.add(location.serialize());
    }

    public void removeLocation(Location location) {
        this.locations.remove(location.serialize());
    }

    public List<Location> getLocations() {
        return this.locations.stream().map(Location::deserialize).toList();
    }

}
