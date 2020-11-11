package cz.MilanT.mcbank;

import cz.MilanT.mcbank.listeners.PluginListener;
import cz.MilanT.mcbank.listeners.RelationListener;
import cz.MilanT.mcbank.commands.AdminBankCommand;
import cz.MilanT.mcbank.commands.BankCommand;
import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.vault.EconomyAPI;
import cz.MilanT.mcbank.vault.Vault;
import cz.MilanT.mcbank.listeners.PlayerListener;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.sql.SQLException;

public class Main extends JavaPlugin implements Listener {
    private IStorage storage;
    private Config config;

    private void stopPlugin(String message) {
        this.log(message);
        this.getPluginLoader().disablePlugin(this);
    }

    @Override
    public void onEnable() {
        PluginManager pluginManager = this.getServer().getPluginManager();

        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        try {
            this.config = new Config(this,"config.yml");
            this.storage = config.getStorage();

            EconomyAPI economyAPI = new EconomyAPI(this.storage);

            Vault vault = new Vault(this, economyAPI);
            vault.registerEconomy();

            this.log(" ");
            this.log("§a########################################");
            this.log("§a#            §f§lMcBank Economy            §a#");
            this.log("§a# §7A plugin for adding bank and economy §a#");
            this.log("§a# §7system to Minecraft spigot servers.  §a#");
            this.log("§a#                                      #");
            this.log("§a#§7 Use §e/mcbank§7 - for players            §a#");
            this.log("§a#§7 Use §e/adminbank§7 - for admins          §a#");
            this.log("§a#                                      §a#");
            this.log("§a# §eDeveloped by §aMilanT                  §a#");
            this.log("§a# §fhttps://github.com/MilanTuryna       §a#");
            this.log("§a#                                      #");
            this.log("§a########################################");
            this.log(" ");

            this.getCommand("mcbank").setExecutor(new BankCommand(this, config, economyAPI));
            this.getCommand("adminbank").setExecutor(new AdminBankCommand(config, economyAPI, this));

            pluginManager.registerEvents(new PluginListener(this, config), this);
            pluginManager.registerEvents(new PlayerListener(this, config, economyAPI, storage), this);
            pluginManager.registerEvents(new RelationListener(this, config), this);
        } catch(SQLException exception) {
            exception.printStackTrace();
            this.stopPlugin("§cAn error occurred while connecting to the MySQL database, McBank plugin will be disabled. §a§l>> Check logs for get solution.");
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
            this.stopPlugin("§cAn error occurred while getting config file, McBank plugin will be disabled. §a§l>> Check logs for get solution.");
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

        this.config.save();
    }

    public void log(String message) {
        this.getServer().getConsoleSender().sendMessage(message);
    }
}
