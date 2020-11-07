package cz.MilanT.mcbank.system.player;

import org.bukkit.entity.Player;

/**
 * Class for better manipulation with Accounts from AccountManager
 */
public class Account {
    private final Player player;
    private final AccountManager accountManager;

    public Account(Player player, AccountManager accountManager) {
        this.player = player;
        this.accountManager = accountManager;
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public Player getPlayer() {
        return player;
    }
}
