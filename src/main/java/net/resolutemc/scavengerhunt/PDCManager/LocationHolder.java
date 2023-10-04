package net.resolutemc.scavengerhunt.PDCManager;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocationHolder implements Serializable {

    private final List<Map<String, Object>> locations;

    public void addLocation(Location location) {
        locations.add(location.serialize());
    }

    public void removeLocation(Location location) {
        locations.remove(location.serialize());
    }

    public LocationHolder() {
        this.locations = new ArrayList<>();
    }

    public List<Location> getLocations() {
        return locations.stream().map(Location::deserialize).toList();
    }

}
