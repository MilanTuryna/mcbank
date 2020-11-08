package cz.MilanT.mcbank;

import cz.MilanT.mcbank.managers.ConfigManager;
import cz.MilanT.mcbank.commands.AdminBankCommand;
import cz.MilanT.mcbank.commands.BankCommand;
import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.vault.EconomyAPI;
import cz.MilanT.mcbank.vault.Vault;
import cz.MilanT.mcbank.listeners.PlayerListener;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends JavaPlugin implements Listener {
    private IStorage storage;

    @Override
    public void onEnable() {
        ConfigManager configManager = new ConfigManager(this);
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        try {
            this.storage = configManager.getStorage();
            EconomyAPI economyAPI = new EconomyAPI(this.storage);

            Vault vault = new Vault(this, economyAPI);
            vault.registerEconomy();

            this.log("§aPlugin was enabled!");

            this.getCommand("mcbank").setExecutor(new BankCommand(this, configManager, economyAPI));
            this.getCommand("adminbank").setExecutor(new AdminBankCommand(configManager, economyAPI));

            this.getServer().getPluginManager().registerEvents(new PlayerListener(this, configManager, economyAPI, storage), this);
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
            this.log("§cAn error occurred while connecting to the MySQL database, McBank plugin will be disabled. §a§l>> Check logs for get solution.");
            this.getPluginLoader().disablePlugin(this);
        } catch (IOException | InvalidConfigurationException fileException) {
            fileException.printStackTrace();
            this.log("§cAn error occurred while connecting to the local file database (YAML), McBank plugin will be disabled. §a§l>> Check logs for get solution.");
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if(this.storage != null) {
            try {
                this.storage.onDisable();
            } catch (IOException exception) {
                exception.printStackTrace();
                this.log("§cUnable to save player data files (YAML)");
            }
        }

        this.saveConfig();
    }

    public void log(String message) {
        this.getServer().getConsoleSender().sendMessage(message);
    }
}
