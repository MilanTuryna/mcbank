package cz.MilanT.mcbank.commands;

import cz.MilanT.mcbank.libraries.Vault;
import cz.MilanT.mcbank.managers.ConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final Vault vault;

    public BankCommand(ConfigManager configManager, Vault vault) {
        this.configManager = configManager;
        this.vault = vault;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player)sender;

            if(args.length == 0) {
                player.sendMessage("/bank");
                player.sendMessage("/bank status");
                player.sendMessage("/bank pay <player> <amount>");
            }

            if(args.length > 1) {
                Economy economy = vault.getEcon();
                if(args[0].equalsIgnoreCase("status") && this.checkPermission(player, "mcbank.status")) {
                    player.sendMessage("§e" + player.getName() + "§7, your current account balance is §a" + economy.getBalance(player));
                }
            }
        } else {
            sender.sendMessage("§cThis command is enabled only for players!");
        }

        return true;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    /**
     * Checking if player has permission if playerPermissions boolean configured to true in configuratiuon
     */
    public boolean checkPermission(Player player, String node) {
        return !this.configManager.getConfig().getBoolean("playerPermissions") || player.hasPermission(node);
    }

    public Vault getVault() {
        return vault;
    }
}
