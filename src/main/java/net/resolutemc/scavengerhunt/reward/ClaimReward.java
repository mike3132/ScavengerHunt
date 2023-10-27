package net.resolutemc.scavengerhunt.reward;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import net.resolutemc.scavengerhunt.data.DataKeys;
import net.resolutemc.scavengerhunt.data.holder.LocationHolder;
import net.resolutemc.scavengerhunt.data.type.LocationType;
import net.resolutemc.scavengerhunt.util.ChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClaimReward {

    private final boolean everyClick, everyClickAll, everyClickRandom, allHeadsFound, everyClickBroadcast;

    public ClaimReward(ScavengerHunt plugin) {
        FileConfiguration config = plugin.getConfig();

        this.everyClick = config.getBoolean("EveryClickRewardBoolean");
        this.everyClickAll = config.getBoolean("EveryClickRewardAllBoolean");
        this.everyClickRandom = config.getBoolean("EveryClickRewardRandomBoolean");
        this.allHeadsFound = config.getBoolean("AllHeadsFoundBoolean");
        this.everyClickBroadcast = config.getBoolean("EveryClickBroadcast");
    }

    /**
     * Run all the commands for when a head is found.
     *
     * @param player The player who found the head.
     */
    public void runEveryHead(Player player) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        if (!this.everyClick) return;
        if (!this.everyClickBroadcast) return;
        if (this.everyClickAll) {
            for (String configList : ScavengerHunt.getInstance().getConfig().getStringList("EveryClickReward")) {
                String command = configList.replace("%playerName%", player.getName());
                Bukkit.dispatchCommand(console, command);
            }
        }

        String rawCommand = ScavengerHunt.getInstance().getConfig().getString("EveryClickBroadcastMessage");
        String command = rawCommand.replace("%playerName%", player.getName());
        Bukkit.dispatchCommand(console, command);
    }

    /**
     * Run a random command from the list of commands.
     *
     * @param player The player who found the head.
     */
    public void runRandomHead(Player player) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

        if (!this.everyClick) return;
        if (!this.everyClickBroadcast) return;
        if (this.everyClickRandom) {
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


    /**
     * Run the commands for when all heads are found.
     *
     * @param player The player who found the head.
     */
    public void runAllHeadsFound(Player player) {
        if (!this.allHeadsFound)
            return;

        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        LocationHolder holder = player.getWorld().getPersistentDataContainer().get(DataKeys.BLOCK_KEY, new LocationType());
        if (holder == null || holder.getLocations().isEmpty()) return;

        ChatMessage.custom(player, "You have found all the heads!");

        for (String configList : ScavengerHunt.getInstance().getConfig().getStringList("AllHeadsFoundReward")) {
            String command = configList.replace("%playerName%", player.getName());
            Bukkit.dispatchCommand(console, command);
        }

    }

}
