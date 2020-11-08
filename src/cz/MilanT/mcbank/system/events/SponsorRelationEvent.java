package cz.MilanT.mcbank.system.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SponsorRelationEvent extends Event implements Cancellable {
    private final Player sponsor;
    private final double amount;

    private boolean isCancelled;

    public SponsorRelationEvent(Player sponsor, double amount) {
        this.sponsor = sponsor;
        this.amount = amount;
    }

    public Player getSponsor() {
        return sponsor;
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
