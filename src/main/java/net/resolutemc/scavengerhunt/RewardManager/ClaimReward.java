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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClaimReward {

    static boolean everyClick = ScavengerHunt.getInstance().getConfig().getBoolean("EveryClickRewardBoolean");
    static boolean everyClickAll = ScavengerHunt.getInstance().getConfig().getBoolean("EveryClickRewardAllBoolean");
    static boolean everyClickRandom = ScavengerHunt.getInstance().getConfig().getBoolean("EveryClickRewardRandomBoolean");
    static boolean allHeadsFound = ScavengerHunt.getInstance().getConfig().getBoolean("AllHeadsFoundBoolean");
    static boolean everyClickBroadcast = ScavengerHunt.getInstance().getConfig().getBoolean("EveryClickBroadcast");


    public static void everyHeadCommand(Player player) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        if (!everyClick) return;
        if (!everyClickBroadcast) return;
        if (everyClickAll) {
            for (String configList : ScavengerHunt.getInstance().getConfig().getStringList("EveryClickReward")) {
                String command = configList.replace("%playerName%", player.getName());
                Bukkit.dispatchCommand(console, command);
            }
        }
        String rawCommand = ScavengerHunt.getInstance().getConfig().getString("EveryClickBroadcastMessage");
        String command = rawCommand.replace("%playerName%", player.getName());
        Bukkit.dispatchCommand(console, command);
    }

    public static void everHeadRandom(Player player) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        if (!everyClick) return;
        if (!everyClickBroadcast) return;
        if (everyClickRandom) {
            List<String> randomReward = new ArrayList<>();
            for (String configList : ScavengerHunt.getInstance().getConfig().getStringList("EveryClickReward")) {
                String command = configList.replace("%playerName%", player.getName());
                randomReward.add(command);
            }
            Random random = new Random();
            int randomCommand = random.nextInt(randomReward.size());
            String command = randomReward.get(randomCommand);
            Bukkit.dispatchCommand(console, command);
            return;
        }
        String rawCommand = ScavengerHunt.getInstance().getConfig().getString("EveryClickBroadcastMessage");
        String command = rawCommand.replace("%playerName%", player.getName());
        Bukkit.dispatchCommand(console, command);
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
