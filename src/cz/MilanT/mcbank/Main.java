package cz.MilanT.mcbank;

import cz.MilanT.mcbank.commands.BankCommand;
import cz.MilanT.mcbank.libraries.Vault;
import cz.MilanT.mcbank.listeners.PlayerListener;
import cz.MilanT.mcbank.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    public boolean vaultActive = false; // variable for checking if vault working

    @Override
    public void onEnable() {
        Vault vault = new Vault(this);
        ConfigManager configManager = new ConfigManager(this);
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        if(vault.setupEconomy()) {
            this.log("Plugin was enabled and VaultAPI is perfectly working.");
            this.vaultActive = true;

            this.getCommand("mcbank").setExecutor(new BankCommand(this, configManager, vault));
            this.getServer().getPluginManager().registerEvents(new PlayerListener(configManager, vault), this);
        } else {
            this.log("§cVault not found, plugin will be disabled.");
            this.log("§aFor good work is needed: §ehttps://github.com/MilkBowl/Vault");
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
