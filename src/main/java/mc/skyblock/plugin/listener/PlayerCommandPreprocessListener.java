package mc.skyblock.plugin.listener;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.punish.model.mute.Mute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Comparator;

public class PlayerCommandPreprocessListener implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().split(" ")[0];
        if (SkyBlockPlugin.instance().getPunishManager().hasActiveMutes(player.getUniqueId()) && SkyBlockPlugin.instance().getPunishConfiguration().getBlockedCommands().contains(message)) {
            Mute latestMute = SkyBlockPlugin.instance().getPunishManager().getMutes(player.getUniqueId()).stream().max(Comparator.comparing(Mute::getMutedAt)).orElse(null);
            if (latestMute != null) {
                event.setCancelled(true);
                player.sendMessage(SkyBlockPlugin.instance().getPunishManager().getMuteScreen(latestMute));
            }
        }
    }

}
