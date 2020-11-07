package cz.MilanT.mcbank.storage;

import cz.MilanT.mcbank.system.player.Account;

import java.io.IOException;

public interface IStorage {
    public Account getPlayerAccount(String name);
    public boolean hasPlayerAccount(String name);
    public boolean createPlayerAccount(Account account) throws IOException;
    public void setPlayerBalance(String name, double balance);
    public void onDisable();
}
