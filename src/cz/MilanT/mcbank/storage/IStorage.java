package cz.MilanT.mcbank.storage;

import cz.MilanT.mcbank.system.player.Account;

import java.io.IOException;
import java.sql.SQLException;

public interface IStorage {
    public Account getPlayerAccount(String name) throws SQLException;
    public boolean hasPlayerAccount(String name) throws SQLException;
    public boolean createPlayerAccount(Account account) throws IOException, SQLException;
    public void setPlayerBalance(String name, double balance) throws SQLException;

    public void onPlayerQuit(String nick) throws IOException;
    public void onDisable() throws IOException;
}
