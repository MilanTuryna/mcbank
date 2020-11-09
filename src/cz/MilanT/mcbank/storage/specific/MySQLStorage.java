package cz.MilanT.mcbank.storage.specific;

import cz.MilanT.mcbank.storage.IStorage;
import cz.MilanT.mcbank.system.player.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLStorage implements IStorage {
    private final Connection connection;

    private void createTable() throws SQLException {
        connection.createStatement()
                .executeQuery("CREATE TABLE IF NOT EXISTS `mcbank` (`nickname` varchar(16) NOT NULL,`balance` double NOT NULL) ENGINE='InnoDB'");
    }

    public MySQLStorage(Connection connection) throws SQLException {
        this.connection = connection;
        this.createTable();
    }

    @Override
    public Account getPlayerAccount(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM mcbank WHERE nick = ?");
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();

        return resultSet.next() ? new Account(resultSet.getString("nick"), resultSet.getDouble("balance")) : null;
    }

    @Override
    public boolean hasPlayerAccount(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT nick FROM mcbank WHERE nick = ?");
        preparedStatement.setString(1,name);
        return preparedStatement.execute();
    }

    @Override
    public boolean createPlayerAccount(Account account) throws SQLException {
        String nickname = account.getNickname();
        if(!hasPlayerAccount(account.getNickname())) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO mcbank (nick, balance) VALUES (?, ?)");
            preparedStatement.setString(1, nickname);
            preparedStatement.setDouble(2, account.getBalance());
            return preparedStatement.execute();
        }
        return false;
    }

    @Override
    public void setPlayerBalance(String name, double balance) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE mcbank SET balance = ? WHERE nick = ?");
        preparedStatement.setDouble(1, balance);
        preparedStatement.setString(2, name);
        preparedStatement.execute();
    }

    @Override
    public void onPlayerQuit(String nick) {//Method is a blank, because MySQL storage don't need onDisable callback.
    }

    @Override
    public void onDisable() {//Method is a blank, because MySQL storage don't need onDisable callback.
    }
}
