package cz.MilanT.mcbank.system.player;

import cz.MilanT.mcbank.db.Database;

public class AccountManager {
    private Database database;

    public AccountManager(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }
}
