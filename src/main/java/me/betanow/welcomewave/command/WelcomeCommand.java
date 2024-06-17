package me.betanow.welcomewave.command;

import me.betanow.welcomewave.LanguageLoader;
import me.betanow.welcomewave.Rewards;
import me.betanow.welcomewave.WelcomeWave;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The command that welcomes the new players
 */
public class WelcomeCommand implements CommandExecutor {

    /**
     * The list of players that have welcomed
     */
    private final ArrayList<Player> hasWelcome = new ArrayList<>();

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
    public WelcomeCommand (WelcomeWave plugin, LanguageLoader languageLoader) {
        this.plugin = plugin;
        this.languageLoader = languageLoader;
    }

    /**
     * The method that runs when the command is executed
     *
     * @param sender - the sender
     * @param cmd    - the command
     * @param msg    - the message
     * @param args   - the arguments
     * @return true if the command is successful, false otherwise
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        // Check if the sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(languageLoader.getPrefixedString("player-only"));
            return false;
        }

        // Check if the player has the permission
        if (!sender.hasPermission("welcomewave.welcome")) {
            sender.sendMessage(languageLoader.getPrefixedString("no-permission"));
            return false;
        }

        // Check if there are new players
        if (plugin.getNewPlayers().isEmpty()) {
            sender.sendMessage(languageLoader.getPrefixedString("no-new-players"));
            return false;
        }

        // Check if the player has already welcomed
        if (hasWelcome.contains(sender)) {
            sender.sendMessage(languageLoader.getPrefixedString("already-welcomed"));
            return false;
        }

        Player player = (Player) sender;
        sendRandomWelcomeMessage(player);
        giveReward(player);

        // Success command
        return true;
    }

    /**
     * Send a random welcome message to the player and add it to the list
     *
     * @param player - the player
     */
    private void sendRandomWelcomeMessage(Player player) {
        // Get the welcome messages then send a random one to the player
        List<String> welcomeMessages = plugin.getWelcomesMessages();
        for (Player target : plugin.getNewPlayers()) {
            // Get a random welcome message
            int randomIndex = (int) (Math.random() * welcomeMessages.size());
            // Send the message to the player
            String message = welcomeMessages.get(randomIndex).replace("{player}", target.getName());
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }

        // Add the player to the list
        hasWelcome.add(player);
        // Set a timer to remove the player from the list
        int waveTime = plugin.getConfig().getInt("wave-time");
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> hasWelcome.remove(player), waveTime * 20L);
    }

    /**
     * Give the reward to the player
     *
     * @param player - the player
     */
    private void giveReward(Player player) {
        // Let player have its reward
        List<String> rewards = plugin.getRewards();
        for (String reward : rewards) {
            // Get the reward type and value
            Rewards rewardType = Rewards.fromString(reward.split(";")[0]);
            String rewardValue = reward.split(";")[1];
            // If the reward is an item, get the amount else gives the reward
            if (rewardType == Rewards.ITEM) {
                int amount = Integer.parseInt(rewardValue.split(":")[1]);
                Rewards.giveReward(player, rewardType, rewardValue, amount);
            } else {
                Rewards.giveReward(player, rewardType, rewardValue);
            }
        }
    }

}
