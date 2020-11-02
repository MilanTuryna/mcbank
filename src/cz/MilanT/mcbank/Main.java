package cz.MilanT.mcbank;

import cz.MilanT.mcbank.libraries.Vault;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public boolean vaultActive = false; // variable for checking if vault working

    @Override
    public void onEnable() {
        Vault vault = new Vault(this);
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        if(vault.setupEconomy()) {
            this.log("Plugin was enabled and VaultAPI is perfectly working.");
            this.vaultActive = true;
        } else {
            this.log("Plugin will be disabled, because Vault isn't working.");
            this.getPluginLoader().disablePlugin(this);
        }
    }

    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }
}
