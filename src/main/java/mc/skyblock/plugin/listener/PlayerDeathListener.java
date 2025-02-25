package mc.skyblock.plugin.listener;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import mc.skyblock.plugin.SkyBlockPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

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
