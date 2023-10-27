package net.resolutemc.scavengerhunt.listener;

import com.jeff_media.customblockdata.CustomBlockData;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.reward.ClaimAnimation;
import net.resolutemc.scavengerhunt.data.DataKeys;
import net.resolutemc.scavengerhunt.data.holder.LocationHolder;
import net.resolutemc.scavengerhunt.data.holder.UUIDHolder;
import net.resolutemc.scavengerhunt.data.type.BlockUUIDType;
import net.resolutemc.scavengerhunt.data.type.LocationType;
import net.resolutemc.scavengerhunt.reward.ClaimReward;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListeners implements Listener {

    private final ScavengerHunt plugin;
    private final boolean playerMessage, playerCompass, infiniteCompass, animationEnabled;
    private final int distanceToHead;

    public PlayerListeners(ScavengerHunt plugin) {
        this.plugin = plugin;

        this.playerMessage = this.plugin.getConfig().getBoolean("Player-Messages-Enabled");
        this.playerCompass = this.plugin.getConfig().getBoolean("Compass-Item-Enabled");
        this.infiniteCompass = this.plugin.getConfig().getBoolean("Infinite-Compass");
        this.animationEnabled = this.plugin.getConfig().getBoolean("Armorstand-Animation");
        this.distanceToHead = this.plugin.getConfig().getInt("Compass-Distance");
    }

    // Block break event
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onHeadBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!block.getType().equals(Material.PLAYER_HEAD)) return;
        CustomBlockData blockData = new CustomBlockData(block, ScavengerHunt.getInstance());

        if (!blockData.has(DataKeys.BLOCK_KEY, new BlockUUIDType())) return;

        if (!this.plugin.getDataManager().getAdminSet().contains(player.getUniqueId())) {
            ChatMessage.send(player, "Head-Break-Not-Admin");
            event.setCancelled(true);
        }

    }

    // Block click event
    @EventHandler
    public void onHeadFind(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();

        if (block == null || action == Action.PHYSICAL) return;
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        if (!block.getType().equals(Material.PLAYER_HEAD)) return;
        if (event.getHand() != EquipmentSlot.HAND) return;

        CustomBlockData blockData = new CustomBlockData(block, ScavengerHunt.getInstance());
        UUIDHolder holder = blockData.getOrDefault(DataKeys.BLOCK_KEY, new BlockUUIDType(), new UUIDHolder());

        if (holder.getUUIDs().contains(player.getUniqueId())) {
            ChatMessage.send(player, "Player-Already-Claimed");
            return;
        }

        holder.getUUIDs().add(player.getUniqueId());
        blockData.set(DataKeys.BLOCK_KEY, new BlockUUIDType(), holder);

        Integer amount = player.getPersistentDataContainer().getOrDefault(DataKeys.BLOCK_KEY, PersistentDataType.INTEGER, 0);
        player.getPersistentDataContainer().set(DataKeys.BLOCK_KEY, PersistentDataType.INTEGER, amount + 1);

        if (this.playerMessage) {
            ChatMessage.send(player, "Player-Click-Message");
        }

        if (this.animationEnabled) {
            final ClaimAnimation animation = new ClaimAnimation(ScavengerHunt.getInstance());
            animation.claim(player, block);
        }

        final ClaimReward reward = new ClaimReward(ScavengerHunt.getInstance());
        reward.runEveryHead(player);
        reward.runRandomHead(player);
        reward.runAllHeadsFound(player);
    }

    // Compass event
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (!this.playerCompass) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        final ItemStack item = player.getInventory().getItemInMainHand();
        if (!(item.getItemMeta() instanceof CompassMeta meta)) return;
        if (!meta.getPersistentDataContainer().has(DataKeys.COMPASS_KEY, PersistentDataType.STRING)) return;

        LocationHolder holder = world.getPersistentDataContainer().get(DataKeys.BLOCK_KEY, new LocationType());
        if (holder == null || holder.getLocations().isEmpty()) return;

        Location shortestDistance = null;

        for (Location location : holder.getLocations()) {
            if (shortestDistance == null) {
                shortestDistance = location;
                continue;
            }

            double distance = player.getLocation().distance(location);
            if (distance < player.getLocation().distance(shortestDistance) && distance <= this.distanceToHead) {
                shortestDistance = location;
            }
        }

        if (shortestDistance == null) return;

        if (!infiniteCompass) {
            item.setAmount(item.getAmount() - 1);
        }

        meta.setLodestone(shortestDistance);
        meta.setLodestoneTracked(false);
        item.setItemMeta(meta);
        ChatMessage.send(player, "Player-Compass-Head-Near");
    }

}
