package eu.revanox.skyblock.listener;

import eu.revanox.skyblock.SkyBlockPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        event.quitMessage(Component.empty());
        SkyBlockPlugin.instance().getUserManager().saveUser(player.getUniqueId());
        SkyBlockPlugin.instance().getScoreboardManager().destroyScoreboard(player);

        SkyBlockPlugin.instance().getTagManager().destroyTag(player);

    }
}
