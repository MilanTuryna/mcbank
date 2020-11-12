package cz.MilanT.mcbank.commands;

import cz.MilanT.mcbank.Config;
import cz.MilanT.mcbank.constants.Error;
import cz.MilanT.mcbank.constants.Message;
import cz.MilanT.mcbank.constants.Permission;
import cz.MilanT.mcbank.constants.Variable;
import cz.MilanT.mcbank.system.events.admin.AddMoneyRelationEvent;
import cz.MilanT.mcbank.system.events.admin.RemoveMoneyRelationEvent;
import cz.MilanT.mcbank.system.events.plugin.ReloadConfigurationEvent;
import cz.MilanT.mcbank.vault.EconomyAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.io.IOException;

public class AdminBankCommand implements CommandExecutor {
    private final Config configManager;
    private final EconomyAPI economyAPI;
    private final Plugin plugin;

    public AdminBankCommand(Config config, EconomyAPI economyAPI, Plugin plugin) {
        this.configManager = config;
        this.economyAPI = economyAPI;
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        /* Used CommandSender instead of Player, because admin commands is will be working for console. */
        PluginManager pluginManager = this.plugin.getServer().getPluginManager();
        if(this.checkPermission(sender, Permission.ADMIN)) {
            if(args.length == 0) {
                boolean notifyrelations = configManager.getConfig().getBoolean("notifyrelations");

                sender.sendMessage("/adminbank addmoney <player> <amount>");
                sender.sendMessage("/adminbank removemoney <player> <amount>");
                sender.sendMessage("/adminbank balance <player>");
                sender.sendMessage("/adminbank notifyrelations <true|false> (now: " + notifyrelations + ")");
                sender.sendMessage("/adminbank reload");
            }

            if(args.length > 0) {
                if(args[0].equalsIgnoreCase("notifyrelations")) {
                    if(args.length == 2) {
                        boolean parsedBoolean = Boolean.parseBoolean(args[1]);
                        configManager.getConfig().set("notifyrelations", parsedBoolean);
                        sender.sendMessage(configManager.getMessage(Message.ADMIN_CONFIGURATION_CHANGED)
                                .replace(Variable.KEY, "NOTIFY_RELATIONS")
                                .replace(Variable.VALUE, String.valueOf(parsedBoolean))
                        );
                    } else {
                        sender.sendMessage(configManager.getError(Error.ADMIN_BAD_ARGUMENT));
                    }
                } else if(args[0].equalsIgnoreCase("balance")) {
                    if(args.length == 2) {
                        String playerName = args[1];
                        if(economyAPI.hasAccount(playerName)) {
                            double balance = economyAPI.getBalance(playerName);
                            sender.sendMessage(configManager.getMessage(Message.ADMIN_CHECK_BALANCE)
                                    .replace(Variable.PLAYER, args[1])
                                    .replace(Variable.BALANCE, String.valueOf(balance)));
                        } else {
                            sender.sendMessage(configManager.getError(Error.PLAYER_ACCOUNT_NOT_FOUND)
                                    .replace(Variable.PLAYER, args[1]));
                        }
                    } else {
                        sender.sendMessage(configManager.getError(Error.ADMIN_BAD_ARGUMENT));
                    }
                } else if(args[0].equalsIgnoreCase("addmoney")) {
                    if(args.length == 3) {
                        String playerName = args[1];
                        int payAmount;
                        try {
                            payAmount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException exception) {
                            sender.sendMessage(this.configManager.getError(Error.INVALID_NUMBER));
                            return true;
                        }

                        if(payAmount > 0) {
                            if(economyAPI.hasAccount(playerName)) {
                                EconomyResponse economyResponse = economyAPI.depositPlayer(playerName, payAmount);
                                if(economyResponse.transactionSuccess()) {
                                    sender.sendMessage(configManager.getMessage(Message.ADMIN_SUCCESS_ADD)
                                            .replace(Variable.TARGET, playerName)
                                            .replace(Variable.AMOUNT, String.valueOf(payAmount)));
                                    AddMoneyRelationEvent addMoneyRelationEvent = new AddMoneyRelationEvent(sender.getName(), playerName, payAmount);
                                    pluginManager.callEvent(addMoneyRelationEvent);
                                }
                            } else {
                                sender.sendMessage(this.configManager.getError(Error.PLAYER_ACCOUNT_NOT_FOUND)
                                        .replace(Variable.PLAYER, playerName));
                            }
                        } else {
                            sender.sendMessage(configManager.getError(Error.INVALID_NUMBER));
                        }
                    } else {
                        sender.sendMessage(configManager.getError(Error.ADMIN_BAD_ARGUMENT));
                    }
                } else if(args[0].equalsIgnoreCase("removemoney")) {
                    if(args.length == 3) {
                        String playerName = args[1];
                        int payAmount;
                        try {
                            payAmount = Integer.parseInt(args[2]);
                        } catch (NumberFormatException exception) {
                            sender.sendMessage(this.configManager.getError(Error.INVALID_NUMBER));
                            return true;
                        }

                        if(payAmount > 0) {
                            if(economyAPI.hasAccount(playerName)) {
                                if(economyAPI.has(playerName, payAmount)) {
                                    EconomyResponse economyResponse = economyAPI.withdrawPlayer(playerName, payAmount);
                                    if(economyResponse.transactionSuccess()) {
                                        sender.sendMessage(Message.ADMIN_SUCCESS_REMOVE
                                                .replace(Variable.TARGET, playerName)
                                                .replace(Variable.AMOUNT, String.valueOf(payAmount)));
                                        RemoveMoneyRelationEvent removeMoneyRelationEvent = new RemoveMoneyRelationEvent(sender.getName(), playerName, payAmount);
                                        pluginManager.callEvent(removeMoneyRelationEvent);
                                    }
                                } else {
                                    sender.sendMessage(this.configManager.getError(Error.BIGGER_AMOUNT).
                                            replace(Variable.PAY_AMOUNT, String.valueOf(payAmount)));
                                }
                            } else {
                                sender.sendMessage(this.configManager.getError(Error.PLAYER_ACCOUNT_NOT_FOUND).replace("%player%", playerName));
                            }
                        } else {
                            sender.sendMessage(configManager.getError(Error.INVALID_NUMBER));
                        }
                    } else {
                        sender.sendMessage(configManager.getError(Error.ADMIN_BAD_ARGUMENT));
                    }
                } else if(args[0].equalsIgnoreCase("reload")) {
                    try {
                        this.configManager.reloadConfig();
                        sender.sendMessage(configManager.getMessage(Message.ADMIN_CONFIGURATION_RELOADED));
                        ReloadConfigurationEvent reloadConfigurationEvent = new ReloadConfigurationEvent(sender.getName());
                        pluginManager.callEvent(reloadConfigurationEvent);
                    } catch (IOException | InvalidConfigurationException exception) {
                        exception.printStackTrace();
                        sender.sendMessage(configManager.getError(Error.RELOAD_NOT_WORKING));
                    }
                } else {
                    sender.sendMessage(configManager.getError(Error.ADMIN_BAD_ARGUMENT));
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

    public Config getConfigManager() {
        return configManager;
    }

    public EconomyAPI getEconomyAPI() {
        return economyAPI;
    }
}
