package cz.MilanT.mcbank.commands;

import cz.MilanT.mcbank.libraries.Vault;
import cz.MilanT.mcbank.managers.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminBankCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final Vault vault;

    public AdminBankCommand(ConfigManager configManager, Vault vault) {
        this.configManager = configManager;
        this.vault = vault;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        return true;
    }

    public boolean checkPermission(Player player, String node) {
        return player.hasPermission(node) || player.isOp();
    }
}
