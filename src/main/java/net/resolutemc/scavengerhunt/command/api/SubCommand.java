package net.resolutemc.scavengerhunt.command.api;

import net.resolutemc.scavengerhunt.ScavengerHunt;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    protected final ScavengerHunt plugin;

    public SubCommand(ScavengerHunt plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the name of the sub command.
     * @param sender The person to send the message to.
     * @param args The arguments of the command.
     */
    public abstract void execute(CommandSender sender, String[] args);

}
