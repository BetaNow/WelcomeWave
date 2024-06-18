package me.betanow.welcomewave;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * This class is used to check for updates on the plugin
 */
public class UpdateChecker {

    /**
     * The plugin instance
     */
    private final WelcomeWave plugin;

    /**
     * The resource id of the plugin
     */
    private final int resourceId = 117273;

    /**
     * Constructor
     *
     * @param plugin     - The plugin instance
     */
    public UpdateChecker (WelcomeWave plugin) {
        this.plugin = plugin;
    }

    /**
     * Check for updates
     *
     * @param consumer - The consumer
     */
    public void getVersion (final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, ()->{
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/~").openStream(); Scanner scann = new Scanner(is)) {
                if (scann.hasNext()) {
                    consumer.accept(scann.next());
                }
            } catch (IOException e) {
                plugin.getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        });
    }

}
