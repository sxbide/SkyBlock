package mc.skyblock.plugin.listener;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.punish.model.ban.Ban;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Comparator;
import java.util.UUID;

public class AsyncPlayerPreLoginListener implements Listener {

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        UUID uniqueId = event.getUniqueId();
        if (SkyBlockPlugin.instance().getWhitelistManager().isEnabled() && !SkyBlockPlugin.instance().getWhitelistManager().isWhitelisted(uniqueId)) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, SkyBlockPlugin.instance().getWhitelistManager().getKickMessage());
            return;
        }
        if (SkyBlockPlugin.instance().getPunishManager().hasActiveBans(uniqueId)) {
            Ban latestBan = SkyBlockPlugin.instance().getPunishManager().getBans(uniqueId).stream().max(Comparator.comparing(Ban::getBannedAt)).orElse(null);
            if (latestBan != null) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, SkyBlockPlugin.instance().getPunishManager().getBanScreen(latestBan));
                return;
            }
        }
        event.allow();
    }
}
