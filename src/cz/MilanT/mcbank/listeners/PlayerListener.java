package cz.MilanT.mcbank.listeners;

import cz.MilanT.mcbank.constants.Variables;
import cz.MilanT.mcbank.managers.ConfigManager;
import cz.MilanT.mcbank.vault.EconomyAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final ConfigManager configManager;
    private final EconomyAPI economyAPI;

    public PlayerListener(ConfigManager configManager, EconomyAPI economyAPI) {
        this.configManager = configManager;
        this.economyAPI = economyAPI;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration configuration = this.configManager.getConfig();

        double actualDeposit = configuration.getDouble("events.joinEvent.deposit");
        double actualWithdraw = configuration.getDouble("events.joinEvent.withdraw");

        player.sendMessage(this.configManager.getString("events.joinEvent.message")
                .replace(Variables.PLAYER, player.getName())
                .replace(Variables.CURRENCY_SYMBOL, this.configManager.getCurrency())
                .replace(Variables.BALANCE, String.valueOf(economyAPI.getBalance(player)))
                .replace(Variables.ACTUAL_DEPOSIT, String.valueOf(actualDeposit))
                .replace(Variables.ACTUAL_WITHDRAW, String.valueOf(actualWithdraw)));

        economyAPI.depositPlayer(player, actualDeposit);
        economyAPI.withdrawPlayer(player, actualWithdraw);
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FileConfiguration configuration = this.configManager.getConfig();

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
