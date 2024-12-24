package org.novato.viewerForge;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public final class ViewerForge extends JavaPlugin {

    private boolean running = true;
    private Thread serverThread;
    private String commandPassword;

    @Override
    public void onEnable() {
        // Save default config
        saveDefaultConfig();
        commandPassword = getConfig().getString("password");

        getLogger().info("ViewerForge is starting up...");
        startSocketListener();
    }

    @Override
    public void onDisable() {
        getLogger().info("ViewerForge is shutting down...");
        running = false;
        if (serverThread != null && serverThread.isAlive()) {
            serverThread.interrupt();
        }
    }

    private void startSocketListener() {
        serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(58431)) {
                getLogger().info("Listening for commands on port 58431...");
                while (running) {
                    try (Socket clientSocket = serverSocket.accept();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                        String input;
                        while ((input = reader.readLine()) != null) {
                            getLogger().info("Received input: " + input);

                            // Validate the password and extract the command
                            if (!input.contains(" ")) {
                                getLogger().warning("Invalid format. Expected 'password command'.");
                                continue;
                            }

                            String[] parts = input.split(" ", 2);
                            String password = parts[0];
                            String command = parts[1];

                            if (!password.equals(commandPassword)) {
                                getLogger().warning("Invalid password. Command rejected.");
                                continue;
                            }

                            // Run the command
                            getLogger().info("Executing command: " + command);
                            Bukkit.getScheduler().runTask(this, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
                        }
                    } catch (Exception e) {
                        if (running) {
                            getLogger().severe("Error handling client connection: " + e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                if (running) {
                    getLogger().severe("Error starting server socket: " + e.getMessage());
                }
            }
        });

        serverThread.start();
    }
}
