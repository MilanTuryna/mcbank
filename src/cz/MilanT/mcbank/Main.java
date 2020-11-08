package cz.MilanT.mcbank;

import cz.MilanT.mcbank.listeners.RelationListener;
import cz.MilanT.mcbank.managers.ConfigManager;
import cz.MilanT.mcbank.commands.AdminBankCommand;
import cz.MilanT.mcbank.commands.BankCommand;
import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.vault.EconomyAPI;
import cz.MilanT.mcbank.vault.Vault;
import cz.MilanT.mcbank.listeners.PlayerListener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends JavaPlugin implements Listener {
    private IStorage storage;

    @Override
    public void onEnable() {
        ConfigManager configManager = new ConfigManager(this);
        PluginManager pluginManager = this.getServer().getPluginManager();

        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        try {
            this.storage = configManager.getStorage();
            EconomyAPI economyAPI = new EconomyAPI(this.storage);

            Vault vault = new Vault(this, economyAPI);
            vault.registerEconomy();

            this.log(" ");
            this.log("§a########################################");
            this.log("§a#            §f§lMcBank Economy            §a#");
            this.log("§a# §7A plugin for adding bank and economy §a#");
            this.log("§a# §7system to Minecraft spigot servers.  §a#");
            this.log("§a#                                      §a#");
            this.log("§a# §eDeveloped by §aMilanT                  §a#");
            this.log("§a# §fhttps://github.com/MilanTuryna       §a#");
            this.log("§a#                                      #");
            this.log("§a########################################");
            this.log(" ");

            this.getCommand("mcbank").setExecutor(new BankCommand(this, configManager, economyAPI));
            this.getCommand("adminbank").setExecutor(new AdminBankCommand(configManager, economyAPI, this));

            pluginManager.registerEvents(new PlayerListener(this, configManager, economyAPI, storage), this);
            pluginManager.registerEvents(new RelationListener(this, configManager), this);
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
            this.log("§cAn error occurred while connecting to the MySQL database, McBank plugin will be disabled. §a§l>> Check logs for get solution.");
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
