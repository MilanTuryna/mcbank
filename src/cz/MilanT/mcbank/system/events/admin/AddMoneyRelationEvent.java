package cz.MilanT.mcbank.system.events.admin;

public class AddMoneyRelationEvent extends AdminMoneyRelationEvent {
    public AddMoneyRelationEvent(String administratorName, String targetName, double amount) {
        super(administratorName, targetName, amount);
    }
}
