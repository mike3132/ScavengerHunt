package net.resolutemc.scavengerhunt.EventManager;

import com.jeff_media.customblockdata.CustomBlockData;
import net.resolutemc.scavengerhunt.PDCManager.LocationHolder;
import net.resolutemc.scavengerhunt.PDCManager.UUIDHolder;
import net.resolutemc.scavengerhunt.MessageManager.ChatMessage;
import net.resolutemc.scavengerhunt.PDCManager.BlockUUID;
import net.resolutemc.scavengerhunt.PDCManager.LocationType;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.SetManager.AdminSet;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;

public class AdminEvent implements Listener {

    NamespacedKey key = new NamespacedKey(ScavengerHunt.getInstance(), "Scavenger-Head-Key");
    NamespacedKey blockKey = new NamespacedKey(ScavengerHunt.getInstance(), "Block-Key");

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent bpe) {
        Player player = bpe.getPlayer();
        Block block = bpe.getBlock();
        World world = block.getWorld();

        if (!AdminSet.getAdmins().contains(player.getUniqueId())) return;
        if (block.getType() != Material.PLAYER_HEAD) return;
        if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            ChatMessage.sendMessage(player, "Admin-Head-Place");

            CustomBlockData blockData = new CustomBlockData(block, ScavengerHunt.getInstance());
            blockData.set(blockKey, PersistentDataType.BOOLEAN, true);
            blockData.set(blockKey, new BlockUUID(), new UUIDHolder());

            if (!world.getPersistentDataContainer().has(blockKey, new LocationType())) {
                world.getPersistentDataContainer().set(blockKey, new LocationType(), new LocationHolder());
            }

            LocationHolder holder = world.getPersistentDataContainer().get(blockKey, new LocationType());
            holder.addLocation(block.getLocation());
            world.getPersistentDataContainer().set(blockKey, new LocationType(), holder);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent bpe) {
        Player player = bpe.getPlayer();
        Block block = bpe.getBlock();
        World world = block.getWorld();

        if (block.getType() != Material.PLAYER_HEAD) return;
        if (!AdminSet.getAdmins().contains(player.getUniqueId())) {
            ChatMessage.sendMessage(player, "Head-Break-Not-Admin");
            return;
        }

        ChatMessage.sendMessage(player, "Admin-Head-Break");

        if (!world.getPersistentDataContainer().has(blockKey, new LocationType())) return;

        LocationHolder holder = world.getPersistentDataContainer().get(blockKey, new LocationType());
        holder.removeLocation(block.getLocation());
        world.getPersistentDataContainer().set(blockKey, new LocationType(), holder);

    }

}
