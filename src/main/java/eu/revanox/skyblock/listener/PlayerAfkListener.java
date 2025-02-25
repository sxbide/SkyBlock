package eu.revanox.skyblock.listener;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.util.ChatAction;
import io.github.rysefoxx.inventory.plugin.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.*;

public class PlayerAfkListener implements Listener {

    Map<UUID, Long> lastMove = new HashMap<>();
    List<Player> afkPlayers;
    BukkitTask afkTask;

    public PlayerAfkListener() {
        afkPlayers = new ArrayList<>();
        afkTask = Bukkit.getScheduler().runTaskTimer(SkyBlockPlugin.instance(), () -> {
            for (UUID uuid : lastMove.keySet()) {
                if (System.currentTimeMillis() - lastMove.get(uuid) > Duration.ofSeconds(60).toMillis() && !afkPlayers.contains(Bukkit.getPlayer(uuid))) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) {
                        continue;
                    }
                    player.sendMessage(ChatAction.info("Du bist nun AFK."));
                    afkPlayers.add(player);
                }
            }
        }, 0, 20);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (afkPlayers.contains(event.getPlayer())) {
            afkPlayers.remove(event.getPlayer());
            event.getPlayer().sendMessage(ChatAction.info("Du bist nun nicht mehr AFK."));
            long afkTime = System.currentTimeMillis() - lastMove.get(event.getPlayer().getUniqueId());
            event.getPlayer().sendMessage(ChatAction.info("Du warst AFK für " + Duration.ofMillis(afkTime).getSeconds() + " Sekunden."));
            //TODO: Add time utils
        }
        lastMove.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

}
