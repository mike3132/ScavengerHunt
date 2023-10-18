package net.resolutemc.scavengerhunt.RewardManager;

import net.resolutemc.scavengerhunt.PDCManager.LocationHolder;
import net.resolutemc.scavengerhunt.PDCManager.LocationType;
import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class ClaimReward {

    static boolean everyClick = ScavengerHunt.getInstance().getConfig().getBoolean("EveryClickRewardBoolean");
    static boolean allHeadsFound = ScavengerHunt.getInstance().getConfig().getBoolean("AllHeadsFoundBoolean");


    public static void everyHeadCommand(Player player) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        if (everyClick) {
            for (String configList : ScavengerHunt.getInstance().getConfig().getStringList("EveryClickReward")) {
                String command = configList.replace("%playerName%", player.getName());
                Bukkit.dispatchCommand(console, command);
            }
        }
    }


    public static void allHeadsFoundCommand(World world, Player player) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        NamespacedKey blockKey = new NamespacedKey(ScavengerHunt.getInstance(), "Block-Key");
        LocationHolder holder = world.getPersistentDataContainer().get(blockKey, new LocationType());

        if (allHeadsFound) {
            if (!player.getPersistentDataContainer().has(blockKey, PersistentDataType.INTEGER)) return;
            if (player.getPersistentDataContainer().get(blockKey, PersistentDataType.INTEGER) == holder.getLocations().size()) {
                player.sendMessage("You have found all the heads");
                for (String configList : ScavengerHunt.getInstance().getConfig().getStringList("AllHeadsFoundReward")) {
                    String command = configList.replace("%playerName%", player.getName());
                    Bukkit.dispatchCommand(console, command);
                }
            }
        }
    }

}
