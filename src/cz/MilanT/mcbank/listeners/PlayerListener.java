package cz.MilanT.mcbank.listeners;

import cz.MilanT.mcbank.libraries.Vault;
import cz.MilanT.mcbank.managers.ConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final ConfigManager configManager;
    private final Vault vault;

    public PlayerListener(ConfigManager configManager, Vault vault) {
        this.configManager = configManager;
        this.vault = vault;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Economy economy = vault.getEcon();
        FileConfiguration configuration = this.configManager.getConfig();

        double actualDeposit = configuration.getDouble("events.joinEvent.deposit");
        double actualWithdraw = configuration.getDouble("events.joinEvent.withdraw");

        player.sendMessage(this.configManager.getString("events.joinEvent.message")
                .replace("%player%", player.getName())
                .replace("%balance%", String.valueOf(economy.getBalance(player)))
                .replace("%actualDeposit%", String.valueOf(actualDeposit))
                .replace("%actualWithdraw%", String.valueOf(actualWithdraw)));

        economy.depositPlayer(player, actualDeposit);
        economy.withdrawPlayer(player, actualWithdraw);
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Economy economy = vault.getEcon();
        FileConfiguration configuration = this.configManager.getConfig();

        economy.depositPlayer(player, configuration.getDouble("events.quitEvent.deposit"));
        economy.withdrawPlayer(player, configuration.getDouble("events.quitEvent.withdraw"));
    }

    public Vault getVault() {
        return vault;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
