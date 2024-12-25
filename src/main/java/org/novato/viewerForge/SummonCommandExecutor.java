package org.novato.viewerForge;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class SummonCommandExecutor implements CommandExecutor {

    private final JavaPlugin plugin;
    private final Map<String, List<String>> mobs;

    public SummonCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
        this.mobs = loadMobs();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("viewerforge") && args.length == 2 && args[0].equalsIgnoreCase("summon")) {
            String mobName = args[1];
            if (isValidMob(mobName)) {
                summonMob(mobName, sender);
                sender.sendMessage("Summoned " + mobName + " for all players.");
                return true;
            } else {
                sender.sendMessage("Invalid mob name. Please enter a valid mob name.");
                return false;
            }
        }
        return false;
    }

    private boolean isValidMob(String mobName) {
        for (List<String> mobList : mobs.values()) {
            if (mobList.contains(mobName)) {
                return true;
            }
        }
        return false;
    }

    private void summonMob(String mobName, CommandSender sender) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            EntityType entityType = EntityType.valueOf(mobName.toUpperCase().replace(" ", "_"));
            location.getWorld().spawnEntity(location, entityType);
        }
    }

    private Map<String, List<String>> loadMobs() {
        try (InputStreamReader reader = new InputStreamReader(plugin.getResource("mobs.json"))) {
            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            return new Gson().fromJson(reader, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}