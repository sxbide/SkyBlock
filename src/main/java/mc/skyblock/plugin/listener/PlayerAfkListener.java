package mc.skyblock.plugin.listener;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.util.ChatAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.*;

public class PlayerAfkListener implements Listener {

    static List<Player> afkPlayers;
    Map<UUID, Long> lastMove = new HashMap<>();
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
    public void onPlayerQuit(PlayerQuitEvent event) {
        afkPlayers.remove(event.getPlayer());
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
