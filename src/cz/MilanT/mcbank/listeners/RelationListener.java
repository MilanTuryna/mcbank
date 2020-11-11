package cz.MilanT.mcbank.listeners;

import cz.MilanT.mcbank.Config;
import cz.MilanT.mcbank.constants.Message;
import cz.MilanT.mcbank.constants.Permission;
import cz.MilanT.mcbank.constants.Variable;
import cz.MilanT.mcbank.system.events.admin.AddMoneyRelationEvent;
import cz.MilanT.mcbank.system.events.admin.RemoveMoneyRelationEvent;
import cz.MilanT.mcbank.system.events.player.PayRelationEvent;
import cz.MilanT.mcbank.system.events.player.SponsorRelationEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.stream.Stream;

public class RelationListener implements Listener {
    private final Plugin plugin;
    private final Config configManager;

    public RelationListener(Plugin plugin, Config config) {
        this.plugin = plugin;
        this.configManager = config;
    }

    private Stream<? extends Player> getOnlineAdministrators() {
        return this.plugin.getServer().getOnlinePlayers().stream().filter(p -> p.hasPermission(Permission.ADMIN));
    }

    @EventHandler
    public void onPayRelation(PayRelationEvent event) {
        this.getOnlineAdministrators().forEach(admin -> admin.sendMessage(configManager.getMessage(Message.ADMIN_NOTIFY_PAY_RELATION)
                .replace(Variable.DONATOR, event.getDonator().getName())
                .replace(Variable.TARGET, event.getTarget().getName())
                .replace(Variable.AMOUNT, String.valueOf(event.getAmount()))));
    }

    @EventHandler
    public void onSponsorRelation(SponsorRelationEvent event) {
        this.getOnlineAdministrators().forEach(admin -> admin.sendMessage(configManager.getMessage(Message.ADMIN_NOTIFY_SPONSOR_RELATION)
                .replace(Variable.DONATOR, event.getSponsor().getName())
                .replace(Variable.AMOUNT, String.valueOf(event.getAmount()))));
    }

    @EventHandler
    public void onAddMoneyRelation(AddMoneyRelationEvent event) {
        String administrator = event.getAdministratorName();
        this.getOnlineAdministrators().filter(p -> !p.getName().equalsIgnoreCase(administrator)).forEach(admin ->
                admin.sendMessage(configManager.getMessage(Message.ADMIN_NOTIFY_ADDMONEY_RELATION)
                        .replace(Variable.ADMINISTRATOR, event.getAdministratorName())
                        .replace(Variable.TARGET, event.getTargetName())
                        .replace(Variable.AMOUNT, String.valueOf(event.getAmount()))));
    }

    @EventHandler
    public void onRemoveMoneyRelation(RemoveMoneyRelationEvent event) {
        String administrator = event.getAdministratorName();
        this.getOnlineAdministrators().filter(p -> !p.getName().equalsIgnoreCase(administrator)).forEach(admin ->
                admin.sendMessage(configManager.getMessage(Message.ADMIN_NOTIFY_REMOVEMONEY_RELATION)
                        .replace(Variable.ADMINISTRATOR, event.getAdministratorName())
                        .replace(Variable.TARGET, event.getTargetName())
                        .replace(Variable.AMOUNT, String.valueOf(event.getAmount()))));
    }
}
