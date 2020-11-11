package cz.MilanT.mcbank.system.player;

public class Account {
    private final String nickname;
    private final double balance;

    public Account(String nickname, double balance) {
        this.nickname = nickname.toLowerCase();
        this.balance = balance;
    }

    public String getNickname() {
        return nickname;
    }

    public double getBalance() {
        return this.balance;
    }
}
