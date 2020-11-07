package cz.MilanT.mcbank.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

public class Vault {
    private final Plugin plugin;
    private final Economy economy;

    public Vault(Plugin plugin, Economy economy) {
        this.plugin = plugin;
        this.economy = economy;
    }

    public void registerEconomy() {
        ServicesManager servicesManager = this.plugin.getServer().getServicesManager();
        servicesManager.register(Economy.class, this.economy, this.plugin, ServicePriority.Highest);
    }
}
