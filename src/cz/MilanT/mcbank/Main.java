package cz.MilanT.mcbank;

import cz.MilanT.mcbank.db.Database;
import cz.MilanT.mcbank.managers.ConfigManager;
import cz.MilanT.mcbank.commands.AdminBankCommand;
import cz.MilanT.mcbank.commands.BankCommand;
import cz.MilanT.mcbank.system.player.AccountManager;
import cz.MilanT.mcbank.vault.EconomyAPI;
import cz.MilanT.mcbank.vault.Vault;
import cz.MilanT.mcbank.listeners.PlayerListener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        ConfigManager configManager = new ConfigManager(this);
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        try {
            Database database = configManager.getDatabase();

            AccountManager accountManager = new AccountManager(database);
            EconomyAPI economyAPI = new EconomyAPI(accountManager);
            Vault vault = new Vault(this, economyAPI);

            if(vault.isLoaded()) {
                vault.registerEconomy();
                this.log("§aVault founded! Plugin will be work with Vault :)");
            } else {
                this.log("§cVault not found, plugin will be work, but without VaultAPI.");
            }

            this.log("§aPlugin was enabled!");

            this.getCommand("mcbank").setExecutor(new BankCommand(this, configManager, economyAPI));
            this.getCommand("adminbank").setExecutor(new AdminBankCommand(configManager, economyAPI));

            this.getServer().getPluginManager().registerEvents(new PlayerListener(configManager, economyAPI), this);
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
            this.log("§cAn error occurred while connecting to the database, McBank plugin will be disabled. &a&l>> Check logs for solution.");
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }
}
