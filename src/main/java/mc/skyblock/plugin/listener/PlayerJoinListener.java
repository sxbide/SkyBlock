package mc.skyblock.plugin.listener;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.location.model.Location;
import mc.skyblock.plugin.util.ChatAction;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

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

        PlayerAfkListener.afkPlayers.remove(player);

        event.joinMessage(Component.empty());
        SkyBlockPlugin.instance().getUserManager().loadUser(player.getUniqueId());
        SkyBlockPlugin.instance().getScoreboardManager().createScoreboard(player);
        SkyBlockPlugin.instance().getTablistManager().setTablist(player);

        SkyBlockPlugin.instance().getIslandManager().loadIsland(player);
        SkyBlockPlugin.instance().getTagManager().updateTag(player);
        SkyBlockPlugin.instance().getCosmeticManager().updateCosmetics(player);


        if (player.getClientBrandName() != null && player.getClientBrandName().equals("labymod")) {
            player.sendMessage(ChatAction.of("Danke, dass du LabyMod benutzt!"));
        }

        player.getLocation().getWorld().setThundering(false);
        player.getLocation().getWorld().setTime(0);
    }
}
