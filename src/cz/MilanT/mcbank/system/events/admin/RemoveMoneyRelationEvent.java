package cz.MilanT.mcbank.system.events.admin;

public class RemoveMoneyRelationEvent extends AdminMoneyRelationEvent {
    public RemoveMoneyRelationEvent(String administratorName, String targetName, double amount) {
        super(administratorName, targetName, amount);
    }
}
