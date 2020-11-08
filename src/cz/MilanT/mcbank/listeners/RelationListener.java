package cz.MilanT.mcbank.listeners;

import cz.MilanT.mcbank.constants.Message;
import cz.MilanT.mcbank.constants.Permission;
import cz.MilanT.mcbank.managers.ConfigManager;
import cz.MilanT.mcbank.system.events.PayRelationEvent;
import cz.MilanT.mcbank.system.events.SponsorRelationEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.stream.Stream;

public class RelationListener implements Listener {
    private final Plugin plugin;
    private final ConfigManager configManager;

    public RelationListener(Plugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    private Stream<? extends Player> getOnlineAdministrators() {
        return this.plugin.getServer().getOnlinePlayers().stream().filter(p -> p.hasPermission(Permission.ADMIN));
    }

    @EventHandler
    public void onPayRelation(PayRelationEvent event) {
        this.getOnlineAdministrators().forEach(admin -> admin.sendMessage(configManager.getMessage(Message.ADMIN_NOTIFY_PAY_RELATION)
                .replace("%donator%", event.getDonator().getName())
                .replace("%target%", event.getTarget().getName())
                .replace("%amount", String.valueOf(event.getAmount()))));
    }

    @EventHandler
    public void onSponsorRelation(SponsorRelationEvent event) {
        this.getOnlineAdministrators().forEach(admin -> admin.sendMessage(configManager.getMessage(Message.ADMIN_NOTIFY_SPONSOR_RELATION)
                .replace("%donator%", event.getSponsor().getName())
                .replace("%amount%", String.valueOf(event.getAmount()))));
    }
}
