package cz.MilanT.mcbank;

import cz.MilanT.mcbank.commands.BankCommand;
import cz.MilanT.mcbank.libraries.Vault;
import cz.MilanT.mcbank.listeners.PlayerListener;
import cz.MilanT.mcbank.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    private ConfigManager configManager;

    public boolean vaultActive = false; // variable for checking if vault working

    @Override
    public void onEnable() {
        Vault vault = new Vault(this);
        this.configManager = new ConfigManager(this);
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        if(vault.setupEconomy()) {
            this.log("Plugin was enabled and VaultAPI is perfectly working.");
            this.vaultActive = true;

            this.getCommand("mcbank").setExecutor(new BankCommand(this.configManager, vault));
            this.getServer().getPluginManager().registerEvents(new PlayerListener(this.configManager, vault), this);
        } else {
            this.log("Plugin will be disabled, because Vault isn't working.");
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }
}
