package me.betanow.welcomewave.command;

import me.betanow.welcomewave.LanguageLoader;
import me.betanow.welcomewave.WelcomeWave;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * The command that administers the plugin
 */
public class WWCommand implements CommandExecutor {

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
    public WWCommand (WelcomeWave plugin, LanguageLoader languageLoader) {
        this.plugin = plugin;
        this.languageLoader = languageLoader;
    }

    /**
     * The method that runs when the command is executed
     *
     * @param sender  - the sender
     * @param command - the command
     * @param label   - the label
     * @param args    - the arguments
     * @return true if the command is successful, false otherwise
     */
    @Override
    public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender has the permission
        if (!sender.hasPermission("welcomewave.admin")) {
            sender.sendMessage(languageLoader.getString("no-permission"));
            return true;
        }

        // Check if the command has arguments
        if (args.length == 0) {
            sender.sendMessage(languageLoader.getString("usage"));
            return true;
        }

        // Check if the command is reload
        if (args[0].equalsIgnoreCase("reload")) {
            // Check if the sender has the permission
            if (!sender.hasPermission("welcomewave.reload")) {
                sender.sendMessage(languageLoader.getString("no-permission"));
                return true;
            }

            // Reload the config and the languages
            plugin.reloadConfig();
            LanguageLoader.reloadLanguages();
            sender.sendMessage(languageLoader.getString("reload"));
            return true;
        }

        // Send the usage message
        sender.sendMessage(languageLoader.getString("usage"));
        return true;
    }
}
