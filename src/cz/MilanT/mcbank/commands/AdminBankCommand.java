package cz.MilanT.mcbank.commands;

import cz.MilanT.mcbank.constants.Errors;
import cz.MilanT.mcbank.constants.Permissions;
import cz.MilanT.mcbank.managers.ConfigManager;
import cz.MilanT.mcbank.vault.EconomyHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminBankCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final EconomyHandler economyHandler;

    public AdminBankCommand(ConfigManager configManager, EconomyHandler vault) {
        this.configManager = configManager;
        this.economyHandler = vault;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        /* Used CommandSender instead of Player, because admin commands is will be working for console. */
        if(this.checkPermission(sender, Permissions.ADMIN)) {
            if(args.length == 0) {
                boolean notifyrelations = configManager.getConfig().getBoolean("notifyrelations");

                sender.sendMessage("/adminbank addmoney <player> <amount>");
                sender.sendMessage("/adminbank removemoney <player> <amount>");
                sender.sendMessage("/adminbank balance <player>");
                sender.sendMessage("/adminbank notifyrelations <true|false> (now: " + notifyrelations + ")");
            }

            if(args.length > 1) {
                if(args[0].equalsIgnoreCase("notifyrelations")) {
                    if(args.length == 2) {
                        configManager.getConfig().set("notifyrelations", Boolean.parseBoolean(args[1]));
                        sender.sendMessage("uspesne jsi to zmenil");
                    } else {
                        sender.sendMessage("test this condition -1");
                    }
                } else {
                    sender.sendMessage("test this condition");
                }
            }
        } else {
            sender.sendMessage(configManager.getError(Errors.NO_PERMISSION));
        }

        return true;
    }

    public boolean checkPermission(CommandSender sender, String node) {
        return sender.hasPermission(node) || sender.isOp();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public EconomyHandler getEconomyHandler() {
        return economyHandler;
    }
}
