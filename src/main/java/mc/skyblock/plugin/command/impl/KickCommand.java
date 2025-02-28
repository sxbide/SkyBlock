package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KickCommand extends AbstractCommand {

    public KickCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "kick", "skyblock.command.kick");
    }

    @Override
    public void run(Player player, String[] args) {

        // /Kick <Player> <Optional String>

        if (args.length == 1) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);

            if (targetPlayer == null) {
                player.sendMessage(ChatAction.getOffline());
                return;
            }

            SkyBlockPlugin.instance().getPunishManager().kick(targetPlayer.getUniqueId(), player.getUniqueId(), "Kein Grund angegeben");
            player.sendMessage(ChatAction.of("Der Spieler " + targetPlayer.getName() + " wurde vom Server geworfen."));
            return;
        }

        if (args.length > 1) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            StringBuilder reason = new StringBuilder();

            if (targetPlayer == null) {
                player.sendMessage(ChatAction.getOffline());
                return;
            }

            for (int i = 1; i < args.length; i++) {
                reason.append(args[i]).append(" ");
            }

            SkyBlockPlugin.instance().getPunishManager().kick(targetPlayer.getUniqueId(), player.getUniqueId(), reason.toString());
            player.sendMessage(ChatAction.of("Der Spieler " + targetPlayer.getName() + " wurde vom Server geworfen."));
            return;
        }
        player.sendMessage(ChatAction.failure("/Kick <Spielername> <Optionaler Grund>"));

    }
}
