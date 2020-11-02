package cz.MilanT.mcbank.commands;

import cz.MilanT.mcbank.libraries.Vault;
import cz.MilanT.mcbank.managers.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BankCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final Vault vault;

    public BankCommand(ConfigManager configManager, Vault vault) {
        this.configManager = configManager;
        this.vault = vault;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return true;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Vault getVault() {
        return vault;
    }
}
