package me.betanow.welcomewave;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

/**
 * The rewards that can be given to the players
 */
public enum Rewards {
    /**
     * The player will receive an item
     */
    ITEM,

    /**
     * The player will receive money
     */
    MONEY,

    /**
     * The player will execute a command
     */
    COMMAND;

    /**
     * Get the reward from a string
     *
     * @param string - the string
     * @return the reward
     */
    public static Rewards fromString (String string) {
        return Rewards.valueOf(string.toUpperCase());
    }

    /**
     * Give the reward to the player
     *
     * @param player - the player
     * @param reward - the reward
     * @param value  - the value
     * @param amount - the amount
     */
    public static void giveReward (Player player, Rewards reward, String value, int amount) {
        if (Objects.requireNonNull(reward) == Rewards.ITEM) {
            player.getInventory().addItem(new ItemStack(Objects.requireNonNull(Material.getMaterial(value)), amount));
        }
    }

    /**
     * Give the reward to the player
     *
     * @param player - the player
     * @param reward - the reward
     * @param value  - the value
     */
    public static void giveReward (Player player, Rewards reward, String value) {
        switch (reward) {
            case MONEY:
                Economy economy = WelcomeWave.getEconomy();
                economy.depositPlayer(player, Double.parseDouble(value));
                break;
            case COMMAND:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value.replace("{player}", player.getName()));
                break;
        }
    }

}
