package cz.MilanT.mcbank.commands;

import cz.MilanT.mcbank.constants.Errors;
import cz.MilanT.mcbank.constants.Messages;
import cz.MilanT.mcbank.constants.Permissions;
import cz.MilanT.mcbank.constants.Variables;
import cz.MilanT.mcbank.libraries.Vault;
import cz.MilanT.mcbank.managers.ConfigManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BankCommand implements CommandExecutor {
    private final Plugin plugin;
    private final ConfigManager configManager;
    private final Vault vault;

    public BankCommand(Plugin plugin, ConfigManager configManager, Vault vault) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.vault = vault;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player)sender;

            if(this.checkPermission(player, Permissions.COMMAND_HELP)) {
                if(args.length == 0) {
                    player.sendMessage("/mcbank");
                    player.sendMessage("/mcbank status");
                    player.sendMessage("/mcbank sponsor <amount>");
                    player.sendMessage("/mcbank pay <player> <amount>");
                }

                if(args.length > 1) {
                    Economy economy = vault.getEcon();
                    if(args[0].equalsIgnoreCase("status")) {
                        if(!this.checkPermission(player, Permissions.COMMAND_STATUS)) {
                            player.sendMessage(configManager.getError(Errors.NO_PERMISSION));
                            return true;
                        }

                        player.sendMessage(configManager.getMessage(Messages.STATUS_COMMAND)
                                .replace(Variables.PLAYER, player.getName())
                                .replace(Variables.CURRENCY_SYMBOL, configManager.getCurrency())
                                .replace(Variables.BALANCE, String.valueOf(economy.getBalance(player))));
                    }

                    if(args[0].equalsIgnoreCase("pay")) {
                        if(!this.checkPermission(player, Permissions.COMMAND_PAY)) {
                            player.sendMessage(configManager.getError(Errors.NO_PERMISSION));
                            return true;
                        }

                        if (args.length == 3) {
                            @SuppressWarnings("deprecation") Player donatedPlayer = this.plugin.getServer().getPlayer(args[0]);

                            if(donatedPlayer != null) {
                                int payAmount;
                                try {
                                    payAmount = Integer.parseInt(args[2]);
                                } catch (NumberFormatException exception) {
                                    player.sendMessage(this.configManager.getError(Errors.INVALID_NUMBER));
                                    return true;
                                }

                                double playerBalance = economy.getBalance(player);
                                double donatedPlayerBalance = economy.getBalance(donatedPlayer);

                                if(playerBalance >= payAmount) {
                                    economy.withdrawPlayer(player, payAmount);
                                    economy.depositPlayer(donatedPlayer, payAmount);

                                    player.sendMessage(configManager.getMessage(Messages.SUCCESSFULLY_SENT)
                                            .replace(Variables.PLAYER, player.getName())
                                            .replace(Variables.DONATED_PLAYER, donatedPlayer.getName())
                                            .replace(Variables.CURRENCY_SYMBOL, this.configManager.getCurrency())
                                            .replace(Variables.BALANCE, String.valueOf(playerBalance))
                                            .replace(Variables.PAY_AMOUNT, String.valueOf(payAmount)));
                                    donatedPlayer.sendMessage(configManager.getMessage(Messages.RECEIVED_FROM_PLAYER)
                                            .replace(Variables.DONATED_PLAYER, donatedPlayer.getName())
                                            .replace(Variables.CURRENCY_SYMBOL, this.configManager.getCurrency())
                                            .replace(Variables.DONATED_PLAYER_BALANCE, String.valueOf(donatedPlayerBalance))
                                            .replace(Variables.PAY_AMOUNT, String.valueOf(payAmount))
                                            .replace(Variables.PLAYER, player.getName()));
                                } else {
                                    player.sendMessage(configManager.getError(Errors.BIGGER_AMOUNT)
                                            .replace(Variables.CURRENCY_SYMBOL, String.valueOf(configManager.getCurrency()))
                                            .replace(Variables.PAY_AMOUNT, String.valueOf(payAmount))
                                            .replace(Variables.BALANCE, String.valueOf(playerBalance))
                                    );
                                }
                            } else {
                                player.sendMessage(configManager.getError(Errors.PAY_TO_OFFLINE));
                            }
                        } else {
                            player.sendMessage(configManager.getError(Errors.BAD_ARGUMENT));
                        }
                    } else if(args[0].equalsIgnoreCase("sponsor")) {
                        if(!this.checkPermission(player, Permissions.COMMAND_SPONSOR)) {
                            player.sendMessage(configManager.getError(Errors.NO_PERMISSION));
                            return true;
                        }

                        if(args.length == 2) {
                            int payAmount;
                            try {
                                payAmount = Integer.parseInt(args[1]);
                            } catch (NumberFormatException exception) {
                                player.sendMessage(this.configManager.getError(Errors.INVALID_NUMBER));
                                return true;
                            }

                            double playerBalance = economy.getBalance(player);

                            if(playerBalance >= payAmount) {
                                economy.withdrawPlayer(player, payAmount);
                                player.sendMessage(this.configManager.getMessage(Messages.PM_THANKS_TO_SPONSOR));
                                plugin.getServer().broadcastMessage(this.configManager.getMessage(Messages.BC_THANKS_TO_SPONSOR));
                            } else {
                                player.sendMessage(configManager.getError(Errors.BIGGER_AMOUNT)
                                        .replace(Variables.CURRENCY_SYMBOL, String.valueOf(configManager.getCurrency()))
                                        .replace(Variables.PAY_AMOUNT, String.valueOf(payAmount))
                                        .replace(Variables.BALANCE, String.valueOf(playerBalance))
                                );
                            }
                        } else {
                            player.sendMessage(configManager.getError(Errors.BAD_ARGUMENT));
                        }
                    }
                }
            } else {
                player.sendMessage(configManager.getError(Errors.NO_PERMISSION));
            }
        } else {
            sender.sendMessage(configManager.getError(Errors.NO_CONSOLE));
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
