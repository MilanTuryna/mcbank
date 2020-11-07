package cz.MilanT.mcbank.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyHandler {
    private final JavaPlugin plugin;
    private final EconomyAPI economyAPI;

    private Economy econ;

    public EconomyHandler(JavaPlugin plugin, EconomyAPI economyAPI) {
        this.plugin = plugin;
        this.economyAPI = economyAPI;
    }

    public boolean setupEconomy() {
        ServicesManager servicesManager = this.plugin.getServer().getServicesManager();

        if(this.plugin.getServer().getPluginManager().getPlugin("Vault") == null) return false;

        servicesManager.register(Economy.class, this.economyAPI, this.plugin, ServicePriority.Highest);
        RegisteredServiceProvider<Economy> rsp = servicesManager.getRegistration(Economy.class);
        if(rsp == null) return false;

        econ = rsp.getProvider();
        return econ != null;
    }

    public Economy getEcon() {
        return econ;
    }
}
