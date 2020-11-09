package cz.MilanT.mcbank.system.events.plugin;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ReloadConfigurationEvent extends Event implements Cancellable {
    private final String administratorName;

    private boolean isCancelled;

    public ReloadConfigurationEvent(String administratorName) {
        this.administratorName = administratorName;
    }

    public String getAdministratorName() {
        return administratorName;
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
