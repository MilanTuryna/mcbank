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
        return this.getString("currency"); }

    public FileConfiguration getConfig() {
        return this.plugin.getConfig();
    }
}
