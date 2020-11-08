package cz.MilanT.mcbank.system.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PayRelationEvent extends Event implements Cancellable {
    private final Player donator;
    private final Player target;
    private final double amount;

    private boolean isCancelled;

    public PayRelationEvent(Player donator, Player target, double amount) {
        this.donator = donator;
        this.target = target;
        this.amount = amount;
    }

    public Player getDonator() {
        return donator;
    }

    public Player getTarget() {
        return target;
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
