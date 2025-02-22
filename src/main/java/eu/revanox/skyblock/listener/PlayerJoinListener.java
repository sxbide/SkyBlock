package eu.revanox.skyblock.listener;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.location.model.Location;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.List;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerSpawn(PlayerSpawnLocationEvent event) {
        Location location = SkyBlockPlugin.instance().getLocationManager().getPosition("spawn");
        Player player = event.getPlayer();

        if (location != null) {
            event.setSpawnLocation(location.getLocation());
        } else {
            event.setSpawnLocation(Bukkit.getWorld("world").getSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.joinMessage(Component.empty());
        SkyBlockPlugin.instance().getUserManager().loadUser(player.getUniqueId());
        SkyBlockPlugin.instance().getScoreboardManager().createScoreboard(player);
        SkyBlockPlugin.instance().getTablistManager().setTablist(player);

        player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 100, 0.7F);

        SkyBlockPlugin.instance().getIslandManager().loadIsland(player);
        SkyBlockPlugin.instance().getTagManager().updateTag(player);

    }
}
