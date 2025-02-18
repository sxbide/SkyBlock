package eu.revanox.skyblock.listener;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import eu.revanox.skyblock.SkyBlockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();

        SkyBlockPlugin.instance().getTagManager().destroyTag(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerPostRespawnEvent event) {
        Player player = event.getPlayer();

        SkyBlockPlugin.instance().getTagManager().updateTag(player);
    }
}
