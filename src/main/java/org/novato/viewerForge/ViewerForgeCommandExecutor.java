package org.novato.viewerForge;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ViewerForgeCommandExecutor implements CommandExecutor {

    private final JavaPlugin plugin;

    public ViewerForgeCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getLogger().info("Command received: " + command.getName() + " with args: " + String.join(" ", args));

        if (args.length == 0) {
            sender.sendMessage("Usage: /viewerforge <subcommand>");
            return false;
        }

        String subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "flight":
                plugin.getLogger().info("Executing flight subcommand");
                return new FlightCommandExecutor((ViewerForge) plugin).onCommand(sender, command, label, args);
            case "swapplayers":
                plugin.getLogger().info("Executing swapplayers subcommand");
                return new SwapPlayersCommandExecutor().onCommand(sender, command, label, args);
            case "summon":
                plugin.getLogger().info("Executing summon subcommand");
                return new SummonCommandExecutor(plugin).onCommand(sender, command, label, args);
            default:
                sender.sendMessage("Unknown subcommand. Usage: /viewerforge <subcommand>");
                return false;
        }
    }
}