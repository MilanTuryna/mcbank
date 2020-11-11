package cz.MilanT.mcbank.listeners;

import cz.MilanT.mcbank.Config;
import cz.MilanT.mcbank.constants.Message;
import cz.MilanT.mcbank.constants.Permission;
import cz.MilanT.mcbank.system.events.plugin.ReloadConfigurationEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.stream.Stream;

public class PluginListener implements Listener {
    private final Plugin plugin;
    private final Config configManager;

    public PluginListener(Plugin plugin, Config config) {
        this.plugin = plugin;
        this.configManager = config;
    }

    private Stream<? extends Player> getOnlineAdministrators() {
        return this.plugin.getServer().getOnlinePlayers().stream().filter(p -> p.hasPermission(Permission.ADMIN));
    }

    @EventHandler
    public void onReloadConfiguration(ReloadConfigurationEvent event) {
        String administratorName = event.getAdministratorName();
        this.getOnlineAdministrators().filter(p -> !p.getName().equalsIgnoreCase(administratorName)).forEach(admin ->
            admin.sendMessage(configManager.getMessage(Message.ADMIN_NOTIFY_RELOADCONFIGURATION).replace("%administrator%", administratorName))
        );
    }
}
