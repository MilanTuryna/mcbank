package cz.MilanT.mcbank.managers;

import cz.MilanT.mcbank.constants.Storage;
import cz.MilanT.mcbank.db.mysql.Database;
import cz.MilanT.mcbank.storage.specific.MySQLStorage;
import cz.MilanT.mcbank.storage.specific.YAMLStorage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import cz.MilanT.mcbank.storage.IStorage;

import java.io.IOException;
import java.sql.SQLException;


public class ConfigManager {
    private final Plugin plugin;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getString(String string) {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(string)).replace("%currencySymbol%", this.getCurrency());
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
    public IStorage getStorage() throws SQLException {
        String activatedStorage = this.getString("storage.active");
        if(activatedStorage.equalsIgnoreCase(Storage.MYSQL)) {
            String db = this.getString("storage.mysql.db");
            String name = this.getString("storage.mysql.name");
            String password = this.getString("storage.mysql.password");

            Database database = new Database(db, name, password);
            return new MySQLStorage(database);
        }

        return new YAMLStorage(this.plugin);
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
