package cz.MilanT.mcbank.listeners;

import cz.MilanT.mcbank.constants.Variable;
import cz.MilanT.mcbank.managers.ConfigManager;
import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.vault.EconomyAPI;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class PlayerListener implements Listener {
    private final Plugin plugin;
    private final ConfigManager configManager;
    private final EconomyAPI economyAPI;
    private final IStorage storage;

    public PlayerListener(Plugin plugin, ConfigManager configManager, EconomyAPI economyAPI, IStorage storage) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.economyAPI = economyAPI;
        this.storage = storage;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        economyAPI.createPlayerAccount(event.getPlayer());

        Player player = event.getPlayer();
        ConsoleCommandSender consoleCommandSender = this.plugin.getServer().getConsoleSender();
        FileConfiguration configuration = this.configManager.getConfig();

        double actualDeposit = configuration.getDouble("events.joinEvent.deposit");
        double actualWithdraw = configuration.getDouble("events.join" + "Event.withdraw");
        double playerBalance = economyAPI.getBalance(player);

        if(actualDeposit < 0 || actualWithdraw < 0) {
            actualDeposit = 0;
            actualWithdraw = 0;

            consoleCommandSender.sendMessage("§cDon't use negatives numbers (withdraw&deposit) in config.yml. Withdraw and Deposit amount will be set to 0");
        }

        player.sendMessage(this.configManager.getString("events.joinEvent.message")
                .replace(Variable.PLAYER, player.getName())
                .replace(Variable.CURRENCY_SYMBOL, this.configManager.getCurrency())
                .replace(Variable.BALANCE, String.valueOf(playerBalance)
                .replace(Variable.ACTUAL_DEPOSIT, String.valueOf(actualDeposit))
                .replace(Variable.ACTUAL_WITHDRAW, String.valueOf(actualWithdraw))));

        economyAPI.depositPlayer(player, actualDeposit);
        if(playerBalance >= actualWithdraw) economyAPI.withdrawPlayer(player, actualWithdraw);
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FileConfiguration configuration = this.configManager.getConfig();

        try {
            storage.onPlayerQuit(player.getName());
        } catch (IOException exception) {
            exception.printStackTrace();
            plugin.getServer().getConsoleSender().sendMessage("§cUnable to save player data file: " + player.getName() + ".yml");
        }
        economyAPI.depositPlayer(player, configuration.getDouble("events.quitEvent.deposit"));
        economyAPI.withdrawPlayer(player, configuration.getDouble("events.quitEvent.withdraw"));
    }

    public EconomyAPI getEconomyAPI() {
        return economyAPI;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
