package cz.MilanT.mcbank.commands;

import cz.MilanT.mcbank.Config;
import cz.MilanT.mcbank.constants.*;
import cz.MilanT.mcbank.constants.Error;
import cz.MilanT.mcbank.system.events.player.PayRelationEvent;
import cz.MilanT.mcbank.system.events.player.SponsorRelationEvent;
import cz.MilanT.mcbank.vault.EconomyAPI;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.List;
import java.util.stream.Collectors;

public class BankCommand implements CommandExecutor {
    private final Plugin plugin;
    private final Config configManager;
    private final EconomyAPI economyAPI;

    public BankCommand(Plugin plugin, Config config, EconomyAPI economyAPI) {
        this.plugin = plugin;
        this.configManager = config;
        this.economyAPI = economyAPI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player)sender;
            PluginManager pluginManager = this.plugin.getServer().getPluginManager();

            if(this.checkPermission(player, Permission.COMMAND_HELP)) {
                if(args.length == 0) {
                    player.sendMessage("/mcbank");
                    player.sendMessage("/mcbank status");
                    player.sendMessage("/mcbank sponsor <amount>");
                    player.sendMessage("/mcbank pay <player> <amount>");
                    player.sendMessage("/mcbank deposit <amount>");
                }

                if(args.length > 0) {
                    if(args[0].equalsIgnoreCase("status")) {
                        if(!this.checkPermission(player, Permission.COMMAND_STATUS)) {
                            player.sendMessage(configManager.getError(Error.NO_PERMISSION));
                            return true;
                        }

                        player.sendMessage(configManager.getMessage(Message.STATUS_COMMAND)
                                .replace(Variable.PLAYER, player.getName())
                                .replace(Variable.CURRENCY_SYMBOL, configManager.getCurrency())
                                .replace(Variable.BALANCE, String.valueOf(economyAPI.getBalance(player))));

                        return true;
                    }

                    if(args[0].equalsIgnoreCase("pay")) {
                        if(!this.checkPermission(player, Permission.COMMAND_PAY)) {
                            player.sendMessage(configManager.getError(Error.NO_PERMISSION));
                            return true;
                        }

                        if (args.length == 3) {
                            @SuppressWarnings("deprecation") Player donatedPlayer = this.plugin.getServer().getPlayer(args[0]);

                            if(donatedPlayer != null) {
                                int payAmount;
                                try {
                                    payAmount = Integer.parseInt(args[2]);
                                } catch (NumberFormatException exception) {
                                    player.sendMessage(this.configManager.getError(Error.INVALID_NUMBER));
                                    return true;
                                }

                                double playerBalance = economyAPI.getBalance(player);
                                double donatedPlayerBalance = economyAPI.getBalance(donatedPlayer);

                                if(playerBalance >= payAmount) {
                                    economyAPI.withdrawPlayer(player, payAmount);
                                    economyAPI.depositPlayer(donatedPlayer, payAmount);

                                    player.sendMessage(configManager.getMessage(Message.SUCCESSFULLY_SENT)
                                            .replace(Variable.PLAYER, player.getName())
                                            .replace(Variable.DONATED_PLAYER, donatedPlayer.getName())
                                            .replace(Variable.BALANCE, String.valueOf(playerBalance))
                                            .replace(Variable.PAY_AMOUNT, String.valueOf(payAmount)));
                                    donatedPlayer.sendMessage(configManager.getMessage(Message.RECEIVED_FROM_PLAYER)
                                            .replace(Variable.DONATED_PLAYER, donatedPlayer.getName())
                                            .replace(Variable.DONATED_PLAYER_BALANCE, String.valueOf(donatedPlayerBalance))
                                            .replace(Variable.PAY_AMOUNT, String.valueOf(payAmount))
                                            .replace(Variable.PLAYER, player.getName()));

                                    PayRelationEvent payRelationEvent = new PayRelationEvent(player, donatedPlayer, payAmount);
                                    pluginManager.callEvent(payRelationEvent);

                                    return true;
                                } else {
                                    player.sendMessage(configManager.getError(Error.BIGGER_AMOUNT)
                                            .replace(Variable.PAY_AMOUNT, String.valueOf(payAmount))
                                    );

                                    return true;
                                }
                            } else {
                                player.sendMessage(configManager.getError(Error.PAY_TO_OFFLINE));
                                return true;
                            }
                        } else {
                            player.sendMessage(configManager.getError(Error.BAD_ARGUMENT));
                            return true;
                        }
                    } else if(args[0].equalsIgnoreCase("sponsor")) {
                        if(!this.checkPermission(player, Permission.COMMAND_SPONSOR)) {
                            player.sendMessage(configManager.getError(Error.NO_PERMISSION));
                            return true;
                        }

                        if(args.length == 2) {
                            int payAmount;
                            try {
                                payAmount = Integer.parseInt(args[1]);
                            } catch (NumberFormatException exception) {
                                player.sendMessage(this.configManager.getError(Error.INVALID_NUMBER));
                                return true;
                            }

                            double playerBalance = economyAPI.getBalance(player);

                            if(playerBalance >= payAmount) {
                                economyAPI.withdrawPlayer(player, payAmount);
                                player.sendMessage(this.configManager.getMessage(Message.PM_THANKS_TO_SPONSOR));
                                plugin.getServer().broadcastMessage(this.configManager.getMessage(Message.BC_THANKS_TO_SPONSOR));
                                SponsorRelationEvent sponsorRelationEvent = new SponsorRelationEvent(player, payAmount);
                                pluginManager.callEvent(sponsorRelationEvent);
                            } else {
                                player.sendMessage(configManager.getError(Error.BIGGER_AMOUNT)
                                        .replace(Variable.CURRENCY_SYMBOL, String.valueOf(configManager.getCurrency()))
                                        .replace(Variable.PAY_AMOUNT, String.valueOf(payAmount))
                                        .replace(Variable.BALANCE, String.valueOf(playerBalance)));
                            }

                            return true;
                        } else {
                            player.sendMessage(configManager.getError(Error.BAD_ARGUMENT));
                            return true;
                        }
                    } else if(args[0].equalsIgnoreCase("deposit")) {
                        if(!this.checkPermission(player, Permission.COMMAND_DEPOSIT)) {
                            player.sendMessage(configManager.getError(Error.NO_PERMISSION));
                            return true;
                        }

                        if(args.length == 2) {
                            int amount;
                            try {
                                amount = Integer.parseInt(args[1]);
                            } catch (NumberFormatException exception) {
                                player.sendMessage(this.configManager.getError(Error.INVALID_NUMBER));
                                return true;
                            }

                            double playerBalance = economyAPI.getBalance(player);

                            if(playerBalance >= amount && playerBalance > 0) {
                                String stringAmount = String.valueOf(amount);
                                String playerName = player.getName();
                                ItemStack moneyItem = configManager.getMoneyBagItem();

                                EconomyResponse economyResponse = economyAPI.withdrawPlayer(player, amount);
                                if(!economyResponse.transactionSuccess()) {
                                    player.sendMessage(economyResponse.errorMessage);
                                    return true;
                                }

                                moneyItem = NBTEditor.set(moneyItem, amount, MoneyBag.NBT_TAG_MONEY);
                                moneyItem = NBTEditor.set(moneyItem, playerName, MoneyBag.NBT_TAG_FIRSTOWNER);
                                ItemMeta itemMeta = moneyItem.getItemMeta();
                                List<String> lore = this.configManager.getList(MoneyBag.LORE)
                                        .stream()
                                        .map(e -> configManager.getTranslatedString(e)
                                                .replace(Variable.AMOUNT, stringAmount)
                                                .replace(Variable.FIRST_OWNER, playerName)
                                                .replace(Variable.CURRENCY_SYMBOL, this.configManager.getCurrency()))
                                        .collect(Collectors.toList());
                                itemMeta.setDisplayName(this.configManager.getConfig().getString(MoneyBag.NAME));
                                itemMeta.setLore(lore);
                                moneyItem.setItemMeta(itemMeta);
                                player.getInventory().addItem(moneyItem);
                                player.sendMessage(this.configManager.getMessage(Message.MONEY_BAG_GET).replace(Variable.WITHDRAW, stringAmount));
                            } else {
                                player.sendMessage(configManager.getError(Error.BIGGER_AMOUNT)
                                        .replace(Variable.PAY_AMOUNT, String.valueOf(amount))
                                        .replace(Variable.BALANCE, String.valueOf(playerBalance)));

                            }
                            return true;
                        }
                    } else {
                        player.sendMessage(configManager.getError(Error.BAD_ARGUMENT));
                        return true;
                    }
                }
            } else {
                player.sendMessage(configManager.getError(Error.NO_PERMISSION));
                return true;
            }
        } else {
            sender.sendMessage(configManager.getError(Error.NO_CONSOLE));
        }

        return true;
    }

    /**
     * Checking if player has permission if playerPermissions boolean configured to true in configuratiuon
     */
    public boolean checkPermission(Player player, String node) {
        return !this.configManager.getConfig().getBoolean("playerPermissions") || player.hasPermission(node);
    }

    public EconomyAPI getEconomyAPI() {
        return economyAPI;
    }

    public Config getConfigManager() {
        return configManager;
    }
}
