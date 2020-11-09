package cz.MilanT.mcbank.system.events.admin;

/**
 * Event for handling action after Administrator remove money from Player bank account
 */
public class RemoveMoneyRelationEvent extends AdminMoneyRelationEvent {
    public RemoveMoneyRelationEvent(String administratorName, String targetName, double amount) {
        super(administratorName, targetName, amount);
    }
}
