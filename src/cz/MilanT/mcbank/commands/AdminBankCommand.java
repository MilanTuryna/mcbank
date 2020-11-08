package cz.MilanT.mcbank.commands;

import cz.MilanT.mcbank.constants.Error;
import cz.MilanT.mcbank.constants.Message;
import cz.MilanT.mcbank.constants.Permission;
import cz.MilanT.mcbank.managers.ConfigManager;
import cz.MilanT.mcbank.vault.EconomyAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminBankCommand implements CommandExecutor {
    private final ConfigManager configManager;
    private final EconomyAPI economyAPI;

    public AdminBankCommand(ConfigManager configManager, EconomyAPI economyAPI) {
        this.configManager = configManager;
        this.economyAPI = economyAPI;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        /* Used CommandSender instead of Player, because admin commands is will be working for console. */
        if(this.checkPermission(sender, Permission.ADMIN)) {
            if(args.length == 0) {
                boolean notifyrelations = configManager.getConfig().getBoolean("notifyrelations");

                sender.sendMessage("/adminbank addmoney <player> <amount>");
                sender.sendMessage("/adminbank removemoney <player> <amount>");
                sender.sendMessage("/adminbank balance <player>");
                sender.sendMessage("/adminbank notifyrelations <true|false> (now: " + notifyrelations + ")");
            }

            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("notifyrelations")) {
                    if(args.length == 2) {
                        boolean parsedBoolean = Boolean.parseBoolean(args[1]);
                        configManager.getConfig().set("notifyrelations", parsedBoolean);
                        sender.sendMessage(configManager.getMessage(Message.ADMIN_CONFIGURATION_CHANGED)
                                .replace("%key%", "NOTIFY_RELATIONS")
                                .replace("%value%", String.valueOf(parsedBoolean))
                        );
                    } else {
                        sender.sendMessage("test this condition -1");
                    }
                } else {
                    sender.sendMessage("test this condition");
                }
            }
        } else {
            sender.sendMessage(configManager.getError(Error.NO_PERMISSION));
        }

        return true;
    }

    public boolean checkPermission(CommandSender sender, String node) {
        return sender.hasPermission(node) || sender.isOp();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public EconomyAPI getEconomyAPI() {
        return economyAPI;
    }
}
