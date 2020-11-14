package cz.MilanT.mcbank.listeners;

import cz.MilanT.mcbank.Config;
import cz.MilanT.mcbank.constants.Message;
import cz.MilanT.mcbank.constants.MoneyBag;
import cz.MilanT.mcbank.constants.Variable;
import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.vault.EconomyAPI;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.IOException;

public class PlayerListener implements Listener {
    private final Plugin plugin;
    private final Config configManager;
    private final EconomyAPI economyAPI;
    private final IStorage storage;

    public PlayerListener(Plugin plugin, Config config, EconomyAPI economyAPI, IStorage storage) {
        this.plugin = plugin;
        this.configManager = config;
        this.economyAPI = economyAPI;
        this.storage = storage;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        economyAPI.createPlayerAccount(event.getPlayer());

        Player player = event.getPlayer();
        ConsoleCommandSender consoleCommandSender = this.plugin.getServer().getConsoleSender();
        FileConfiguration configuration = this.configManager.getConfig();

        double actualDeposit = configuration.getDouble("events.joinEvent.deposit");
        double actualWithdraw = configuration.getDouble("events.join" + "Event.withdraw");
        double playerBalance = economyAPI.getBalance(player);

        if(actualDeposit < 0 || actualWithdraw < 0) {
            actualDeposit = 0;
            actualWithdraw = 0;

            consoleCommandSender.sendMessage("§cDon't use negatives numbers (withdraw&deposit) in config.yml. Withdraw and Deposit amount will be set to 0");
        }

        String playerName = player.getName();
        double finalActualDeposit = actualDeposit;
        double finalActualWithdraw = actualWithdraw;
        String currencySymbol = this.configManager.getCurrency();
        this.configManager.getList("events.joinEvent.message").forEach(msg -> {
            // used equals("") instead of isEmpty for administrator adding new lines by " "
            if(!msg.equals("")) player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.replace(Variable.PLAYER, playerName)
                    .replace(Variable.CURRENCY_SYMBOL, currencySymbol)
                    .replace(Variable.BALANCE, String.valueOf(playerBalance))
                    .replace(Variable.WITHDRAW, String.valueOf(finalActualWithdraw))
                    .replace(Variable.ACTUAL_DEPOSIT, String.valueOf(finalActualDeposit))));
        });

        economyAPI.depositPlayer(player, actualDeposit);
        if(playerBalance >= actualWithdraw) economyAPI.withdrawPlayer(player, actualWithdraw);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        Action action = playerInteractEvent.getAction();
        ItemStack item = playerInteractEvent.getItem();

        if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            Material headMaterial = Material.getMaterial("SKULL_ITEM");
            if (headMaterial == null) {
                headMaterial = Material.getMaterial("PLAYER_HEAD");
            }

            if(item != null && item.getType() == headMaterial) {
                if(NBTEditor.contains(item, MoneyBag.NBT_TAG_MONEY)) {
                    Inventory inventory = player.getInventory();
                    double amount = NBTEditor.getDouble(item, MoneyBag.NBT_TAG_MONEY);

                    EconomyResponse economyResponse = economyAPI.depositPlayer(player, amount);
                    if(economyResponse.transactionSuccess()) {
                        inventory.removeItem(item);

                        player.sendMessage(configManager.getMessage(Message.MONEY_BAG_USE)
                                .replace(Variable.BALANCE, String.valueOf(amount)));
                        playerInteractEvent.setCancelled(true);
                    } else {
                        player.sendMessage(economyResponse.errorMessage);
                    }
                } else {
                    player.sendMessage("toto neni spravny item");
                }
            }
        }
    }

    private void onPlayerKill(Player player, Player target) {
        String killEventMessage = configManager.getString("events.killEvent.message");
        FileConfiguration config = configManager.getConfig();
        double withdraw = config.getDouble("events.deathEvent.withdraw");
        double deposit = config.getDouble("events.deathEvent.deposit");

        EconomyResponse depositEconomyResponse = economyAPI.depositPlayer(player, deposit);
        EconomyResponse withdrawEconomyResponse = economyAPI.withdrawPlayer(player, withdraw);

        if(depositEconomyResponse.transactionSuccess() && withdrawEconomyResponse.transactionSuccess()
                && !killEventMessage.isEmpty()) {
            player.sendMessage(killEventMessage
                    .replace(Variable.WITHDRAW, String.valueOf(withdraw))
                    .replace(Variable.DEPOSIT, String.valueOf(deposit))
                    .replace(Variable.PLAYER, player.getName())
                    .replace(Variable.TARGET, target.getName()));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();
        Player killer = player.getKiller();

        if(killer == null) {
            String deathEventMessage = configManager.getString("events.deathEvent.message");
            FileConfiguration config = configManager.getConfig();
            double withdraw = config.getDouble("events.deathEvent.withdraw");
            double deposit = config.getDouble("events.deathEvent.deposit");

            EconomyResponse depositEconomyResponse = economyAPI.depositPlayer(player, deposit);
            EconomyResponse withdrawEconomyResponse = economyAPI.withdrawPlayer(player, withdraw);

            if(depositEconomyResponse.transactionSuccess() && withdrawEconomyResponse.transactionSuccess()
                    && !deathEventMessage.isEmpty()) {
                player.sendMessage(deathEventMessage
                        .replace(Variable.PLAYER, player.getName())
                        .replace(Variable.WITHDRAW, String.valueOf(withdraw))
                        .replace(Variable.DEPOSIT, String.valueOf(deposit)));
            }
        } else {
            this.onPlayerKill(killer, player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        FileConfiguration configuration = this.configManager.getConfig();

        try {
            storage.onPlayerQuit(player.getName());
        } catch (IOException exception) {
            exception.printStackTrace();
            plugin.getServer().getConsoleSender().sendMessage("§cUnable to save player data file: " + player.getName() + ".yml");
        }

        double withdrawAmount = configuration.getDouble("events.quitEvent.withdraw");

        if(withdrawAmount < 0) {
            withdrawAmount = 0;

            plugin.getServer().getConsoleSender().sendMessage("§cDon't use negatives numbers (withdraw&deposit) in config.yml. Withdraw and Deposit amount will be set to 0");
        }

        economyAPI.depositPlayer(player, configuration.getDouble("events.quitEvent.deposit"));
        if(economyAPI.getBalance(player) >= withdrawAmount) economyAPI.withdrawPlayer(player, configuration.getDouble("events.quitEvent.withdraw"));
    }

    public EconomyAPI getEconomyAPI() {
        return economyAPI;
    }

    public Config getConfigManager() {
        return configManager;
    }
}
