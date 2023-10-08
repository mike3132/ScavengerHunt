package net.resolutemc.scavengerhunt.Animation;

import net.resolutemc.scavengerhunt.ItemManager.HeadItem;
import net.resolutemc.scavengerhunt.MessageManager.ColorTranslate;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ClaimAnimation {

    static boolean soundEnabled = ScavengerHunt.getInstance().getConfig().getBoolean("Armorstand-Sound");
    public static void playerClaim(Block block, Player player) {
        Location blockLocation = block.getLocation();
        Location blockCenter = blockLocation.add(0.5, 0, 0.5);
        World world = blockLocation.getWorld();
        HeadItem headItem = new HeadItem();
        ItemStack head = headItem.getHead();


        ArmorStand armorStand = block.getWorld().spawn(blockCenter, ArmorStand.class);
        armorStand.setHelmet(head);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setCustomName(ColorTranslate.chatColor("" + ScavengerHunt.getInstance().getConfig().getString("Armorstand-Message")));
        armorStand.setCustomNameVisible(true);

        if (!soundEnabled) return;
        player.playSound(armorStand.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);
        world.spawnParticle(Particle.REDSTONE, blockLocation, 1, new Particle.DustOptions(Color.ORANGE, 5));

        new BukkitRunnable() {
            int time = 0;
            @Override
            public void run() {
                if (time > 30) {
                    armorStand.remove();
                    this.cancel();
                    return;
                }
                if (time > 10) {
                    Location secondLocation = armorStand.getLocation().add(0, 0.2, 0);
                    secondLocation.setYaw(secondLocation.getYaw() + 10f);
                    armorStand.teleport(secondLocation);
                }
                armorStand.getWorld().spawnParticle(Particle.CLOUD, armorStand.getLocation().clone().add(0, 0.5, 0), 3,0 ,0,0,0);
                time ++;
            }
        }.runTaskTimer(ScavengerHunt.getInstance(), 0, 1L);

    }


}
