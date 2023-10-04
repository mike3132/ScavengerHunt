package net.resolutemc.scavengerhunt.EventManager;

import com.jeff_media.customblockdata.CustomBlockData;
import net.resolutemc.scavengerhunt.PDCManager.UUIDHolder;
import net.resolutemc.scavengerhunt.MessageManager.ChatMessage;
import net.resolutemc.scavengerhunt.PDCManager.BlockUUID;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.SetManager.AdminSet;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerEvent implements Listener {


    boolean playerMessage = ScavengerHunt.getInstance().getConfig().getBoolean("Player-Messages-Enabled");
    NamespacedKey blockKey = new NamespacedKey(ScavengerHunt.getInstance(), "Block-Key");

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
        }

    }

}
