package net.resolutemc.scavengerhunt.data.type;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.data.holder.LocationHolder;
import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LocationType implements PersistentDataType<byte[], LocationHolder> {

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<LocationHolder> getComplexType() {
        return LocationHolder.class;
    }

    @Override
    public byte[] toPrimitive(LocationHolder complex, PersistentDataAdapterContext persistentDataAdapterContext) {
        return SerializationUtils.serialize(complex);
    }

    @Override
    public LocationHolder fromPrimitive(byte[] bytes, PersistentDataAdapterContext persistentDataAdapterContext) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            return (LocationHolder) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            ScavengerHunt.getInstance().getLogger().warning("Container not found");
        }

        return new LocationHolder();
    }
}