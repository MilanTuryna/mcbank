package cz.MilanT.mcbank.vault;

import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.system.player.Account;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EconomyAPI implements Economy {
    private final String name;
    private IStorage storage;

    public EconomyAPI(IStorage storage) {
        this.storage = storage;
        this.name = "McBank Economy";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        return String.valueOf(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return "";
    }

    @Override
    public boolean hasAccount(String playerName) {
        try {
            return storage.hasPlayerAccount(playerName);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        try {
            return storage.hasPlayerAccount(player.getName());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    @Override
    public double getBalance(String playerName) {
        try {
            return storage.getPlayerAccount(playerName).getBalance();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return this.getBalance(player.getName());
    }

    @Override
    public double getBalance(String playerName, String world) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return 0;
    }

    @Override
    public boolean has(String playerName, double amount) {
        try {
            return storage.getPlayerAccount(playerName).getBalance() >= amount;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return this.has(player.getName(), amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        try {
            if(!hasAccount(playerName)) return new EconomyResponse(0,0, ResponseType.FAILURE, "Player don't have a account");
            if(amount < 0) return new EconomyResponse(0,0, ResponseType.FAILURE, "Cannot deposit negative amount number");

            Account account = this.storage.getPlayerAccount(playerName);
            double result = account.getBalance() - amount;
            if(result < 0) return new EconomyResponse(0,0, ResponseType.FAILURE, "Player don't have a balance for this withdraw");
            this.storage.setPlayerBalance(playerName, result);
            return new EconomyResponse(amount, result, ResponseType.SUCCESS, null);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return new EconomyResponse(0,0, ResponseType.FAILURE, "Database Storage error");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return this.withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        try {
            if(!hasAccount(playerName)) return new EconomyResponse(0,0, ResponseType.FAILURE, "Player don't have a account");
            if(amount < 0) return new EconomyResponse(0,0, ResponseType.FAILURE, "Cannot deposit negative amount number");

            Account account = this.storage.getPlayerAccount(playerName);
            double result = amount + account.getBalance();
            this.storage.setPlayerBalance(playerName, result);
            return new EconomyResponse(amount, result, ResponseType.SUCCESS, null);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return new EconomyResponse(0,0, ResponseType.FAILURE, "Database Storage error");
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return this.depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return this.notImplemented();
    }

    /**
     * Function for creating EconomyResponse which is returned if another plugin call Banks.
     * @return EconomyResponse
     */
    private EconomyResponse notImplemented() {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Plugin McBank not support this function");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return this.notImplemented();
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return this.notImplemented();
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        Account account = new Account(playerName, 0);
        try {
            return storage.createPlayerAccount(account);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return this.createPlayerAccount(player.getName());
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }

    public void setStorage(IStorage storage) {
        this.storage = storage;
    }
}
