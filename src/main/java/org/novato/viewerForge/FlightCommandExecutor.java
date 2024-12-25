package org.novato.viewerForge;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class FlightCommandExecutor implements CommandExecutor {

    private final ViewerForge plugin;

    public FlightCommandExecutor(ViewerForge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.getLogger().info("FlightCommandExecutor received command: " + command.getName() + " with args: " + String.join(" ", args));

        if (command.getName().equalsIgnoreCase("viewerforge") && args.length == 2 && args[0].equalsIgnoreCase("flight")) {
            try {
                int flightTime = Integer.parseInt(args[1]);
                plugin.getLogger().info("Giving flight to all players for " + flightTime + " seconds");
                giveFlightToAllPlayers(flightTime);
                sender.sendMessage("All players have been given flight for " + flightTime + " seconds.");
                return true;
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid time format: " + args[1]);
                sender.sendMessage("Invalid time format. Please enter a valid number of seconds.");
                return false;
            }
        }
        return false;
    }

    private void giveFlightToAllPlayers(int seconds) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            plugin.getLogger().info("Enabling flight for player: " + player.getName());
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    plugin.getLogger().info("Disabling flight for player: " + player.getName());
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }
            }
        }.runTaskLater(plugin, seconds * 20L); // Convert seconds to ticks (20 ticks = 1 second)
    }
}