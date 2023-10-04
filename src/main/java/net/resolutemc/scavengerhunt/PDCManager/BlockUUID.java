package net.resolutemc.scavengerhunt.PDCManager;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.apache.commons.lang3.SerializationUtils;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class BlockUUID implements PersistentDataType<byte[], UUIDHolder> {

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<UUIDHolder> getComplexType() {
        return UUIDHolder.class;
    }

    @Override
    public byte[] toPrimitive(UUIDHolder complex, PersistentDataAdapterContext persistentDataAdapterContext) {
        return SerializationUtils.serialize(complex);
    }

    @Override
    public UUIDHolder fromPrimitive(byte[] bytes, PersistentDataAdapterContext persistentDataAdapterContext) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            return (UUIDHolder) objectInputStream.readObject();
        }
        catch (IOException | ClassNotFoundException exception) {
            ScavengerHunt.getInstance().getLogger().warning("Container not found");
        }

        return new UUIDHolder();
    }
}
