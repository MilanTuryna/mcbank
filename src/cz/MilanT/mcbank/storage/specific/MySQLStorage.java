package cz.MilanT.mcbank.storage.specific;

import cz.MilanT.mcbank.db.mysql.Database;
import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.system.player.Account;

import java.io.IOException;

public class MySQLStorage implements IStorage {
    private final Database database;

    public MySQLStorage(Database database) {
        this.database = database;
    }

    @Override
    public Account getPlayerAccount(String name) {
        return null;
    }

    @Override
    public boolean hasPlayerAccount(String name) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(Account account) {
        if(!hasPlayerAccount(account.getNickname())) return false;
        return true;
    }

    @Override
    public void setPlayerBalance(String name, double balance) {

    }

    @Override
    public void onPlayerQuit(String nick) throws IOException {

    }


    @Override
    public void onDisable() {
        //Method is a blank, because MySQL storage don't need onDisable callback.
    }
}
