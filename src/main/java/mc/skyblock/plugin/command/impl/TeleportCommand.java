package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.SoundAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TeleportCommand extends AbstractCommand {

    public TeleportCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "teleport", "skyblock.command.teleport", "tp");
    }

    @Override
    public void run(Player player, String[] args) {

        if(args.length == 1) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);

            if(targetPlayer == null) {
                player.sendMessage(ChatAction.getOffline());
                return;
            }

            SkyBlockPlugin.instance().getTagManager().teleportPlayer(player, targetPlayer.getLocation());
            player.sendMessage(ChatAction.of("Du wurdest zu " + targetPlayer.getName() + " teleportiert."));
            SoundAction.playNotification(player);
            return;
        }

        if(args.length == 2) {
            Player playerToTeleport = Bukkit.getPlayer(args[0]);
            Player targetPlayer = Bukkit.getPlayer(args[1]);

            if(targetPlayer == null || playerToTeleport == null) {
                player.sendMessage(ChatAction.getOffline());
                return;
            }

            SkyBlockPlugin.instance().getTagManager().teleportPlayer(playerToTeleport, targetPlayer.getLocation());
            player.sendMessage(ChatAction.of(playerToTeleport.getName() + " wurde zu " + targetPlayer.getName() + " teleportiert."));
            SoundAction.playNotification(player);
            return;
        }

        player.sendMessage(ChatAction.failure("/teleport <spielername> <spielername>"));
    }
}
