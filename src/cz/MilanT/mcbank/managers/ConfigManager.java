package cz.MilanT.mcbank.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
    private final Plugin plugin;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getString(String string) {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(string));
    }

    public String getCurrency() {
        return this.getString("currencySymbol");
    }

    public String getError(String error) {
        return this.getString("errorMessages." + error);
    }

    public String getMessage(String message) {
        return this.getString("messages." + message);
    }

    public FileConfiguration getConfig() {
        return this.plugin.getConfig();
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
