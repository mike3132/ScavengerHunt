package net.resolutemc.scavengerhunt.EventManager;

import com.jeff_media.customblockdata.CustomBlockData;
import net.resolutemc.scavengerhunt.Animation.ClaimAnimation;
import net.resolutemc.scavengerhunt.PDCManager.LocationHolder;
import net.resolutemc.scavengerhunt.PDCManager.LocationType;
import net.resolutemc.scavengerhunt.PDCManager.UUIDHolder;
import net.resolutemc.scavengerhunt.MessageManager.ChatMessage;
import net.resolutemc.scavengerhunt.PDCManager.BlockUUID;
import net.resolutemc.scavengerhunt.RewardManager.ClaimReward;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.SetManager.AdminSet;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.persistence.PersistentDataType;

public class PlayerEvent implements Listener {

    NamespacedKey blockKey = new NamespacedKey(ScavengerHunt.getInstance(), "Block-Key");
    NamespacedKey compassKey = new NamespacedKey(ScavengerHunt.getInstance(), "ScavengerHunt-Compass-Key");
    boolean playerMessage = ScavengerHunt.getInstance().getConfig().getBoolean("Player-Messages-Enabled");
    boolean playerCompass = ScavengerHunt.getInstance().getConfig().getBoolean("Compass-Item-Enabled");
    boolean infiniteCompass = ScavengerHunt.getInstance().getConfig().getBoolean("Infinite-Compass");
    boolean animationEnabled = ScavengerHunt.getInstance().getConfig().getBoolean("Armorstand-Animation");
    int distanceToHead = ScavengerHunt.getInstance().getConfig().getInt("Compass-Distance");

    // Block break event
    @EventHandler
    public void onBlockBreak(BlockBreakEvent bpe) {
        Player player = bpe.getPlayer();
        Block block = bpe.getBlock();

        if (!block.getType().equals(Material.PLAYER_HEAD)) return;
        CustomBlockData blockData = new CustomBlockData(block, ScavengerHunt.getInstance());
        if (!blockData.has(blockKey, new BlockUUID())) return;
        if (!AdminSet.getAdmins().contains(player.getUniqueId())) {
            ChatMessage.sendMessage(player, "Head-Break-Not-Admin");
            bpe.setCancelled(true);
        }
    }

    // Block click event
    @EventHandler
    public void onPlayerLeftClick(PlayerInteractEvent pie) {
        Player player = pie.getPlayer();
        Action action = pie.getAction();
        Block block = pie.getClickedBlock();

        if (action == Action.LEFT_CLICK_BLOCK || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if (player.getGameMode().equals(GameMode.CREATIVE)) return;
            if (!block.getType().equals(Material.PLAYER_HEAD)) return;
            CustomBlockData blockData = new CustomBlockData(block, ScavengerHunt.getInstance());
            if (!blockData.has(blockKey, new BlockUUID())) return;
            UUIDHolder holder = blockData.get(blockKey, new BlockUUID());

            if (holder.getUUIDs().contains(player.getUniqueId())) {
                ChatMessage.sendMessage(player, "Player-Already-Claimed");
                return;
            }

            holder.getUUIDs().add(player.getUniqueId());
            blockData.set(blockKey, new BlockUUID(), holder);

            if (!player.getPersistentDataContainer().has(blockKey, PersistentDataType.INTEGER)) {
                player.getPersistentDataContainer().set(blockKey, PersistentDataType.INTEGER, 0);
            }
            player.getPersistentDataContainer().set(blockKey, PersistentDataType.INTEGER,
                    player.getPersistentDataContainer().get(blockKey, PersistentDataType.INTEGER) + 1);



            if (!playerMessage) return;
            ChatMessage.sendMessage(player, "Player-Click-Message");

            if (!animationEnabled) return;
            ClaimAnimation.playerClaim(block, player);

            ClaimReward.everyHeadCommand(player);
            ClaimReward.everHeadRandom(player);
            ClaimReward.allHeadsFoundCommand(player.getWorld(), player);
        }

    }

    // Compass event
    @EventHandler
    public void onPlayerClick(PlayerInteractEvent pie) {
        Player player = pie.getPlayer();
        World world = player.getWorld();

        LocationHolder holder = world.getPersistentDataContainer().get(blockKey, new LocationType());

        if (!playerCompass) return;

        if (pie.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) return;
        if (!player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(compassKey, PersistentDataType.STRING)) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        CompassMeta compassMeta = (CompassMeta) item.getItemMeta();

        for (Location location : holder.getLocations()) {
            double distance = player.getLocation().distance(location);

            if (distance <= distanceToHead) {
                if (!infiniteCompass) {
                    item.setAmount(item.getAmount() -1);
                }
                compassMeta.setLodestone(location);
                compassMeta.setLodestoneTracked(false);
                item.setItemMeta(compassMeta);
                ChatMessage.sendMessage(player, "Player-Compass-Head-Near");
            }
        }

    }

}
