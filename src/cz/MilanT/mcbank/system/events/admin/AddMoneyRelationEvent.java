package cz.MilanT.mcbank.system.events.admin;

/**
 * Event for handling action after Administrator add money to player bank account
 */
public class AddMoneyRelationEvent extends AdminMoneyRelationEvent {
    public AddMoneyRelationEvent(String administratorName, String targetName, double amount) {
        super(administratorName, targetName, amount);
    }
}
