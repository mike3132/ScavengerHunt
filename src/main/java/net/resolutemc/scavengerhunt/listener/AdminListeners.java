package net.resolutemc.scavengerhunt.listener;

import com.jeff_media.customblockdata.CustomBlockData;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.data.DataKeys;
import net.resolutemc.scavengerhunt.data.holder.LocationHolder;
import net.resolutemc.scavengerhunt.data.holder.UUIDHolder;
import net.resolutemc.scavengerhunt.data.type.BlockUUIDType;
import net.resolutemc.scavengerhunt.data.type.LocationType;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class AdminListeners implements Listener {

    private final ScavengerHunt plugin;

    public AdminListeners(ScavengerHunt plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        World world = block.getWorld();

        if (block.getType() != Material.PLAYER_HEAD) return;

        if (!this.plugin.getDataManager().getAdminSet().contains(player.getUniqueId())) return;

        final ItemStack item = player.getInventory().getItemInMainHand();
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        final PersistentDataContainer container = meta.getPersistentDataContainer();
        if (!container.has(DataKeys.ITEM_KEY, PersistentDataType.STRING)) return;
        ChatMessage.send(player, "Admin-Head-Place");

        CustomBlockData blockData = new CustomBlockData(block, ScavengerHunt.getInstance());
        blockData.set(DataKeys.BLOCK_KEY, PersistentDataType.BOOLEAN, true);
        blockData.set(DataKeys.BLOCK_KEY, new BlockUUIDType(), new UUIDHolder());

        LocationHolder holder = world.getPersistentDataContainer().getOrDefault(
                DataKeys.BLOCK_KEY,
                new LocationType(),
                new LocationHolder()
        );

        holder.addLocation(block.getLocation());
        world.getPersistentDataContainer().set(DataKeys.BLOCK_KEY, new LocationType(), holder);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        World world = block.getWorld();

        if (block.getType() != Material.PLAYER_HEAD) return;

        if (!this.plugin.getDataManager().getAdminSet().contains(player.getUniqueId())) {
            ChatMessage.send(player, "Head-Break-Not-Admin");
            return;
        }

        ChatMessage.send(player, "Admin-Head-Break");

        LocationHolder holder = world.getPersistentDataContainer().get(
                DataKeys.BLOCK_KEY,
                new LocationType()
        );

        if (holder == null || holder.getLocations().isEmpty()) return;
        holder.removeLocation(block.getLocation());
        world.getPersistentDataContainer().set(DataKeys.BLOCK_KEY, new LocationType(), holder);
    }

}
