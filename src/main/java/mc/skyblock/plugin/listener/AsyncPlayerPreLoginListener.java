package mc.skyblock.plugin.listener;

import mc.skyblock.plugin.SkyBlockPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class AsyncPlayerPreLoginListener implements Listener {

    @EventHandler
    public void onPlayerSpawn(AsyncPlayerPreLoginEvent event) {
        UUID uniqueId = event.getUniqueId();
        if (SkyBlockPlugin.instance().getWhitelistManager().isEnabled() && !SkyBlockPlugin.instance().getWhitelistManager().isWhitelisted(uniqueId)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, SkyBlockPlugin.instance().getWhitelistManager().getKickMessage());
            return;
        }
        event.allow();
    }
}
