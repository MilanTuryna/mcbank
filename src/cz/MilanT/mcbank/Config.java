package cz.MilanT.mcbank;

import cz.MilanT.mcbank.constants.MoneyBag;
import cz.MilanT.mcbank.constants.Storage;
import cz.MilanT.mcbank.constants.Variable;
import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.storage.specific.mysql.MySQLStorage;
import cz.MilanT.mcbank.storage.specific.yaml.YAMLStorage;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Config {
    private final Plugin plugin;
    private final File file;
    private final FileConfiguration fileConfiguration;

    public Config(Plugin plugin, String fileName) throws IOException, InvalidConfigurationException {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), fileName);
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(file.getName(), false);
        }
        this.fileConfiguration = new YamlConfiguration();
        this.fileConfiguration.load(file);
    }

    public void save() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public String getTranslatedString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public String getString(String string) {
        return this.getTranslatedString(this.getConfig().getString(string)).replace(Variable.CURRENCY_SYMBOL, this.getCurrency());
    }

    public List<String> getList(String string) {
        return this.getConfig().getStringList(string);
    }

    public String getCurrency() {
        return this.getConfig().getString("currencySymbol"); // used getConfig() because using getString method would cause an infinite loop
    }

    public String getError(String error) {
        return this.getString("errorMessages." + error);
    }

    public ItemStack getMoneyBagItem() {
        return NBTEditor.getHead(this.getConfig().getString(MoneyBag.ITEM_URL));
    }

    public String getMessage(String message) {
        return this.getString("messages." + message);
    }

    public void reloadConfig() throws IOException, InvalidConfigurationException {
        this.fileConfiguration.load(this.file);
    }

    public FileConfiguration getConfig() {
        return this.fileConfiguration;
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

    public File getFile() {
        return file;
    }
}
