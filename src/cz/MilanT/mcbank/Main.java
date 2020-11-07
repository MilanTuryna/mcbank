package cz.MilanT.mcbank;

import cz.MilanT.mcbank.db.Database;
import cz.MilanT.mcbank.managers.ConfigManager;

import cz.MilanT.mcbank.commands.AdminBankCommand;
import cz.MilanT.mcbank.commands.BankCommand;

import cz.MilanT.mcbank.system.player.AccountManager;
import cz.MilanT.mcbank.vault.EconomyAPI;
import cz.MilanT.mcbank.vault.EconomyHandler;
import cz.MilanT.mcbank.listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin implements Listener {
    public boolean vaultActive = false; // variable for checking if vault working

    @Override
    public void onEnable() {
        ConfigManager configManager = new ConfigManager(this);

        try {
            Database database = configManager.getDatabase();

            AccountManager accountManager = new AccountManager(database);
            EconomyAPI economyAPI = new EconomyAPI(accountManager);
            EconomyHandler economyHandler = new EconomyHandler(this, economyAPI);

            this.saveDefaultConfig();
            this.getConfig().options().copyDefaults(true);

            if(economyHandler.setupEconomy()) {
                this.log("Plugin was enabled and VaultAPI is perfectly working.");
                this.vaultActive = true;

                this.getCommand("mcbank").setExecutor(new BankCommand(this, configManager, economyHandler));
                this.getCommand("adminbank").setExecutor(new AdminBankCommand(configManager, economyHandler));

                this.getServer().getPluginManager().registerEvents(new PlayerListener(configManager, economyHandler), this);
            } else {
                this.log("§cVault not found, plugin will be disabled.");
                this.log("§aFor good work is needed: §ehttps://github.com/MilkBowl/Vault");
                this.getPluginLoader().disablePlugin(this);
            }
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
            this.log("§cAn error occurred while connecting to the database, McBank plugin will be disabled. &a&l>> Check logs for solution.");
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    public boolean isVaultActive() {
        return vaultActive;
    }
}
