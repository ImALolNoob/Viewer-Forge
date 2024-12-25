package org.novato.viewerForge;

import org.bukkit.plugin.java.JavaPlugin;

public final class ViewerForge extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("ViewerForge plugin enabled");
        getCommand("viewerforge").setExecutor(new ViewerForgeCommandExecutor(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("ViewerForge plugin disabled");
    }
}