package cz.MilanT.mcbank.managers;

import cz.MilanT.mcbank.constants.Storage;
import cz.MilanT.mcbank.constants.Variable;
import cz.MilanT.mcbank.storage.specific.mysql.MySQLStorage;
import cz.MilanT.mcbank.storage.specific.yaml.YAMLStorage;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import cz.MilanT.mcbank.storage.IStorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConfigManager {
    private final Plugin plugin;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getString(String string) {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString(string)).replace(Variable.CURRENCY_SYMBOL, this.getCurrency());
    }

    public String getCurrency() {
        return this.getConfig().getString("currencySymbol"); // used getConfig() because using getString method would cause an infinite loop
    }

    public String getError(String error) {
        return this.getString("errorMessages." + error);
    }

    public String getMessage(String message) {
        return this.getString("messages." + message);
    }

    public void reloadConfig() {
        this.plugin.reloadConfig();
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

            Connection connection = DriverManager.getConnection( "jdbc:mysql://localhost/" + db, name, password);
            return new MySQLStorage(connection);
        }

        return new YAMLStorage(this.plugin);
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
