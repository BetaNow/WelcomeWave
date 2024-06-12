package me.betanow.welcomewave;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class is responsible for loading and managing language files.
 *
 * @author BetaNow
 * @version 1.0.2
 */
public class LanguageLoader {

    /**
     * The plugin instance.
     */
    private final WelcomeWave plugin;

    /**
     * The supported languages.
     */
    public static final String[] SUPPORTED_LANGUAGES = new String[]{ "en_US", "fr_FR" };

    /**
     * The prefix for all messages.
     */
    public static final String PREFIX = "&7[&6WelcomeWave&7] &r";

    /**
     * The map of languages.
     */
    private static final Map<String, FileConfiguration> LANGUAGES = new HashMap<>();

    /**
     * The current language.
     */
    private static String currentLang;

    /**
     * Constructs a new LanguageLoader.
     *
     * @param plugin - The plugin instance
     */
    public LanguageLoader (WelcomeWave plugin) {
        this.plugin = plugin;
        loadLanguages();
    }

    /**
     * Loads all language files.
     */
    public void loadLanguages () {
        // Save default language files if they don't exist
        saveDefaultLanguageFiles();

        // Get all language files
        File langFolder = new File(plugin.getDataFolder(), "lang");
        if (!langFolder.exists()) { // Create lang folder if it doesn't exist
            langFolder.mkdirs();
        }

        // Load all language files
        for (File file : Objects.requireNonNull(langFolder.listFiles())) {
            if (file.isFile() && file.getName().endsWith(".yml")) {  // Check if file is a .yml file
                String langName = file.getName().replace(".yml", "");
                FileConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
                LANGUAGES.put(langName, langConfig);
            }
        }

        // Set current language
        setLanguage();
    }

    /**
     * Saves the default language files.
     */
    private void saveDefaultLanguageFiles () {
        for (String language : SUPPORTED_LANGUAGES) {
            plugin.saveResource("lang/" + language + ".yml", true);
            plugin.getLogger().info("Saved default language file: " + language);
        }
    }

    /**
     * Sets the current language.
     */
    private void setLanguage () {
        // Set current language
        currentLang = plugin.getConfig().getString("lang");

        // Check if language is not null if it is set the default language
        if (currentLang == null) {
            plugin.getLogger().warning("Language is not set in config.yml, setting default language");
            currentLang = SUPPORTED_LANGUAGES[0];
        }
    }

    /**
     * Gets the current language.
     *
     * @param key  - The translation key
     * @return The current language
     */
    public String getString (String key) {
        // Get translation from current language
        String message = LANGUAGES.get(currentLang).getString(key);

        // Check if translation is not found
        if (message == null) {
            message = "&cTranslation not found for key: &f" + key;
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Gets the prefixed translation.
     *
     * @param key  - The translation key
     * @return The prefixed translation
     */
    public String getPrefixedString (String key) {
        return ChatColor.translateAlternateColorCodes('&', PREFIX) + getString(key);
    }

}
