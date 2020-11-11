package cz.MilanT.mcbank.listeners;

import cz.MilanT.mcbank.Config;
import cz.MilanT.mcbank.constants.Message;
import cz.MilanT.mcbank.constants.MoneyBag;
import cz.MilanT.mcbank.constants.Variable;
import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.vault.EconomyAPI;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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

        player.sendMessage(this.configManager.getString("events.joinEvent.message")
                .replace(Variable.PLAYER, player.getName())
                .replace(Variable.CURRENCY_SYMBOL, this.configManager.getCurrency())
                .replace(Variable.BALANCE, String.valueOf(playerBalance)
                        .replace(Variable.ACTUAL_DEPOSIT, String.valueOf(actualDeposit))
                        .replace(Variable.ACTUAL_WITHDRAW, String.valueOf(actualWithdraw))));

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
                if(NBTEditor.contains(item, MoneyBag.NBT_TAG)) {
                    Inventory inventory = player.getInventory();

                    EconomyResponse economyResponse = economyAPI.depositPlayer(player, 50);
                    if(economyResponse.transactionSuccess()) {
                        inventory.removeItem(item);

                        player.sendMessage(configManager.getMessage(Message.MONEY_BAG_USE)
                                .replace(Variable.BALANCE, String.valueOf(50)));
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
