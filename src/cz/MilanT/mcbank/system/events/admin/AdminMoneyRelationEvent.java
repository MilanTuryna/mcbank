package cz.MilanT.mcbank.system.events.admin;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

abstract class AdminMoneyRelationEvent extends Event implements Cancellable {
    private final String administratorName;
    private final String targetName;
    private final double amount;

    private boolean isCancelled;

    public AdminMoneyRelationEvent(String administratorName, String targetName, double amount) {
        this.administratorName = administratorName;
        this.targetName = targetName;
        this.amount = amount;
    }

    public String getAdministratorName() {
        return administratorName;
    }

    public String getTargetName() {
        return targetName;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
