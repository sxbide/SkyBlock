package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.island.model.SkyBlockIsland;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.SoundAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VisitCommand extends AbstractCommand {

    public VisitCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "visit", null, "besuchen", "visitieren", "goto");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length > 0) {

            String playerName = args[0];
            Player targetPlayer = Bukkit.getPlayer(playerName);

            if (targetPlayer == player) {
                player.sendMessage(ChatAction.failure("§cDu kannst deine eigene Insel nicht besuchen."));
                SoundAction.playTaskFailed(player);
                return;
            }

            if (targetPlayer == null) {
                player.sendMessage(ChatAction.getOffline());
                return;
            }

            SkyBlockIsland skyBlockIsland = SkyBlockPlugin.instance().getIslandManager().getIslandByPlayer(targetPlayer);

            if (skyBlockIsland == null) {
                player.sendMessage(ChatAction.failure("§cDieser Spieler besitzt keine Insel."));
                SoundAction.playTaskFailed(player);
                return;
            }

            if (skyBlockIsland.getWarpLocation() == null) {
                player.sendMessage(ChatAction.failure("§cDieser Spieler besitzt keinen Insel Warp."));
                SoundAction.playTaskFailed(player);
                return;
            }

            player.teleportAsync(skyBlockIsland.getWarpLocation());
            player.sendMessage(ChatAction.of("§aDu hast die Insel von " + targetPlayer.getName() + " betreten."));
            SoundAction.playNotification(player);

            for (Player islandPlayer : skyBlockIsland.getWarpLocation().getWorld().getPlayers()) {
                if (islandPlayer == player) continue;
                islandPlayer.sendMessage(ChatAction.of("§a" + player.getName() + " §7hat deine Insel betreten."));
            }
            return;
        }
        player.sendMessage(ChatAction.failure("Verwende dazu: /visit <spielername>"));
        return;
    }
}
