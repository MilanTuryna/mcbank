package cz.MilanT.mcbank.managers;

import cz.MilanT.mcbank.db.Database;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;

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
    public Database getDatabase() throws SQLException {
        String db = this.getString("mysql.db");
        String name = this.getString("mysql.name");
        String password = this.getString("mysql.password");

        return new Database(db, name, password);
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
