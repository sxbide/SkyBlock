package mc.skyblock.plugin.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerTeleportListener implements Listener {

    @EventHandler
    public void onPlayerPreWorldChange(PlayerTeleportEvent event) {
        Player player = event.getPlayer();

        if (event.getFrom().getWorld() != event.getTo().getWorld()) {
            Bukkit.getLogger().info("PRE: " + player.getName() + " is about to switch worlds.");

            //SkyBlockPlugin.instance().getTagManager().destroyTag(player);
        }
    }

    @EventHandler
    public void onPlayerPostWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        Bukkit.getLogger().info("POST: " + player.getName() + " has switched to " + player.getWorld().getName());

        //SkyBlockPlugin.instance().getTagManager().updateTag(player);
    }
}
