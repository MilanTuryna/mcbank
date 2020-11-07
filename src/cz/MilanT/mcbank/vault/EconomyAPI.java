package cz.MilanT.mcbank.vault;

import cz.MilanT.mcbank.system.player.AccountManager;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

import java.util.ArrayList;
import java.util.List;

public class EconomyAPI implements Economy {
    private final AccountManager accountManager;
    private final String name;

    public EconomyAPI(AccountManager accountManager) {
        this.accountManager = accountManager;
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
        return 0;
    }

    @Override
    public String format(double amount) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
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
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return 0;
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
        return false;
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return false;
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
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return null;
    }

    /**
     * Function for creating EconomyResponse which is returned if another plugin call Banks.
     * @return EconomyResponse
     */
    private EconomyResponse banksNotSupported() {
        return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Plugin McBank not support this banks functions.");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return this.banksNotSupported();
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return this.banksNotSupported();
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return this.banksNotSupported();
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return this.banksNotSupported();
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return this.banksNotSupported();
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return this.banksNotSupported();
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return this.banksNotSupported();
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return this.banksNotSupported();
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return this.banksNotSupported();
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return this.banksNotSupported();
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return this.banksNotSupported();
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return false;
    }
}
