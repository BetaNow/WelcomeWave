package me.betanow.welcomewave.listener;

import me.betanow.welcomewave.LanguageLoader;
import me.betanow.welcomewave.WelcomeWave;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * The listener that listens for player join events
 */
public class PlayerJoinListener implements Listener {

    /**
     * The plugin instance
     */
    private final WelcomeWave plugin;

    /**
     * The language loader
     */
    private final LanguageLoader languageLoader;

    /**
     * The constructor
     *
     * @param plugin         - the plugin
     * @param languageLoader - the language loader
     */
    public PlayerJoinListener (WelcomeWave plugin, LanguageLoader languageLoader) {
        this.plugin = plugin;
        this.languageLoader = languageLoader;
    }

    /**
     * The method that runs when a player joins
     *
     * @param event - the event
     */
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPlayedBefore()) {
            player.sendMessage(languageLoader.getPrefixedString("first-join-message").replace("{player}", player.getName()));
            plugin.addNewPlayer(player);

            // Send the welcome wave by sending a message to all players
            for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                onlinePlayer.sendMessage(languageLoader.getPrefixedString("new-player").replace("{player}", player.getName()));
            }
        }
    }

}
