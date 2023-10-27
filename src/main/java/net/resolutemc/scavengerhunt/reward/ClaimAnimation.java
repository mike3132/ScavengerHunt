package net.resolutemc.scavengerhunt.reward;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class ClaimAnimation {

    private final ScavengerHunt plugin;
    private final boolean soundEnabled;
    private final String armorstandName;

    public ClaimAnimation(ScavengerHunt plugin) {
        this.plugin = plugin;
        this.soundEnabled = this.plugin.getConfig().getBoolean("Armorstand-Sound");
        this.armorstandName = this.plugin.getConfig().getString("Armorstand-Message");
    }

    @SuppressWarnings("deprecation")
    public void claim(Player player, Block block) {
        Location center = block.getLocation().clone().add(0.5, 0, 0.5).clone();
        World world = center.getWorld();
        ItemStack head = this.plugin.getItemManager().getHeadItem();
        if (world == null) return;

        ArmorStand armorStand = world.spawn(center, ArmorStand.class, stand -> {
            stand.setHelmet(head);
            stand.setInvisible(true);
            stand.setInvulnerable(true);
            stand.setCustomName(ChatMessage.color(this.armorstandName));
            stand.setCustomNameVisible(true);
        });

        if (soundEnabled) {
            player.playSound(armorStand.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }

        world.spawnParticle(Particle.REDSTONE, center, 1, new Particle.DustOptions(Color.ORANGE, 5));

        // Levitate the armor stand
        BukkitTask levitateTask = Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
            Location newLocation = armorStand.getLocation().clone().add(0, 0.2, 0);
            newLocation.setYaw(newLocation.getYaw() + 10f);
            armorStand.teleport(newLocation);

            world.spawnParticle(Particle.CLOUD, armorStand.getLocation().clone().add(0, 0.5, 0), 3, 0, 0, 0, 0);
        }, 0, 1L);

        // Remove the armor stand after 30 ticks (1.5 seconds)
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            levitateTask.cancel();
            armorStand.remove();
        }, 30L);
    }


}
