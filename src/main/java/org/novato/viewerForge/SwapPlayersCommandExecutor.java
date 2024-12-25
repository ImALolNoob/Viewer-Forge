package org.novato.viewerForge;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwapPlayersCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("viewer") && args.length == 1 && args[0].equalsIgnoreCase("swapplayers")) {
            swapPlayers();
            sender.sendMessage("All players have been swapped in a random order.");
            return true;
        }
        return false;
    }

    private void swapPlayers() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        List<Location> locations = new ArrayList<>();

        for (Player player : players) {
            locations.add(player.getLocation());
        }

        Collections.shuffle(locations);

        for (int i = 0; i < players.size(); i++) {
            players.get(i).teleport(locations.get(i));
        }
    }
}