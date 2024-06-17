package me.betanow.welcomewave;

import me.betanow.welcomewave.command.WWCommand;
import me.betanow.welcomewave.command.WelcomeCommand;
import me.betanow.welcomewave.listener.PlayerJoinListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The main class of the plugin
 */
public final class WelcomeWave extends JavaPlugin {

    /**
     * The economy instance
     */
    private static Economy economy = null;

    /**
     * The list of new players
     */
    private final ArrayList<Player> newPlayers = new ArrayList<>();

    /**
     * The method that runs when the plugin is enabled
     */
    @Override
    public void onEnable () {
        saveDefaultConfig(); // Save the default config if it doesn't exist
        LanguageLoader languageLoader = new LanguageLoader(this); // Load the language files
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register the PlayerJoinListener
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, languageLoader), this);
        Objects.requireNonNull(getCommand("welcome")).setExecutor(new WelcomeCommand(this, languageLoader));
        Objects.requireNonNull(getCommand("welcomewave")).setExecutor(new WWCommand(this, languageLoader));
    }

    /**
     * The method that runs when the plugin is disabled
     */
    @Override
    public void onDisable () {
        getLogger().info("WelcomeWave disabled!");
    }

    /**
     * Set up the economy
     * Source: <a href="https://github.com/milkbowl/Vault">Vault Plugin Repo</a>
     *
     * @return true if the economy is set up, false otherwise
     */
    private boolean setupEconomy () {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true; // rsp.getProvider() always != null;
    }

    /**
     * Add a new player to the list
     *
     * @param player the player to add
     */
    public void addNewPlayer (Player player) {
        // Add the player to the list
        newPlayers.add(player);

        // Remove the player after (configurable) seconds
        int waveTime = getConfig().getInt("wave-time");
        getServer().getScheduler().runTaskLater(this, ()->newPlayers.remove(player), waveTime * 20L);
    }

    /**
     * Get the instance of the plugin
     *
     * @return the instance of the plugin
     */
    public static WelcomeWave getInstance () {
        return getPlugin(WelcomeWave.class);
    }

    /**
     * Get the list of new players
     *
     * @return the list of new players
     */
    public ArrayList<Player> getNewPlayers () {
        return newPlayers;
    }

    /**
     * Get the list of welcome messages
     *
     * @return the list of welcome messages
     */
    public List<String> getWelcomesMessages () {
        return getConfig().getStringList("welcome-messages");
    }

    /**
     * Get the list of rewards
     *
     * @return the list of rewards
     */
    public List<String> getRewards () {
        return getConfig().getStringList("rewards");
    }

    /**
     * Get the economy instance
     *
     * @return the economy instance
     */
    public static Economy getEconomy () {
        return economy;
    }
}
