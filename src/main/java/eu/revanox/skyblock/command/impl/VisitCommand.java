package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.island.model.SkyBlockIsland;
import eu.revanox.skyblock.seller.menu.SellerMenu;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.SoundAction;
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
                return;
            }

            if (targetPlayer == null) {
                player.sendMessage(ChatAction.getOffline());
                return;
            }

            SkyBlockIsland skyBlockIsland = SkyBlockPlugin.instance().getIslandManager().getIslandByPlayer(targetPlayer);

            if (skyBlockIsland == null) {
                player.sendMessage(ChatAction.failure("§cDieser Spieler besitzt keine Insel."));
                return;
            }

            if (skyBlockIsland.getWarpLocation() == null) {
                player.sendMessage(ChatAction.failure("§cDieser Spieler besitzt keinen Insel Warp."));
                return;
            }

            player.teleportAsync(skyBlockIsland.getWarpLocation());
            player.sendMessage(ChatAction.of("§aDu hast die Insel von " + targetPlayer.getName() + " betreten."));
            SoundAction.playGoodWork(player);

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
