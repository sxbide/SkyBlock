package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class WhitelistCommand extends AbstractCommand {

    public WhitelistCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "whitelist", "skyblock.whitelist", "wl", "maintenance", "wartung");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatAction.of("/Whitelist <on/off>"));
            player.sendMessage(ChatAction.of("/Whitelist add <Spielername>"));
            player.sendMessage(ChatAction.of("/Whitelist remove <Spielername>"));
            player.sendMessage(ChatAction.of("/Whitelist list"));
            return;
        }
        if (args[0].equalsIgnoreCase("on")) {
            SkyBlockPlugin.instance().getWhitelistManager().status(true);
            player.sendMessage(ChatAction.of("Die Whitelist wurde aktiviert."));
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                if (!SkyBlockPlugin.instance().getWhitelistManager().isWhitelisted(onlinePlayer.getUniqueId())) {
                    onlinePlayer.kick(SkyBlockPlugin.instance().getWhitelistManager().getKickMessage(), PlayerKickEvent.Cause.WHITELIST);
                }
            });
            return;
        }
        if (args[0].equalsIgnoreCase("off")) {
            SkyBlockPlugin.instance().getWhitelistManager().status(false);
            player.sendMessage(ChatAction.of("Die Whitelist wurde deaktiviert."));
            return;
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length == 1) {
                player.sendMessage(ChatAction.of("/Whitelist add <Spielername>"));
                return;
            }
            if (SkyBlockPlugin.instance().getWhitelistManager().isWhitelisted(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
                player.sendMessage(ChatAction.failure("Der Spieler " + args[1] + " ist bereits auf der Whitelist."));
                return;
            }
            SkyBlockPlugin.instance().getWhitelistManager().whitelist(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
            player.sendMessage(ChatAction.of("Der Spieler " + args[1] + " wurde hinzugefügt."));
            return;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 1) {
                player.sendMessage(ChatAction.of("/Whitelist remove <Spielername>"));
                return;
            }
            if (!SkyBlockPlugin.instance().getWhitelistManager().isWhitelisted(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
                player.sendMessage(ChatAction.failure("Der Spieler " + args[1] + " ist nicht auf der Whitelist."));
                return;
            }
            SkyBlockPlugin.instance().getWhitelistManager().unwhitelist(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
            player.sendMessage(ChatAction.of("Der Spieler " + args[1] + " wurde entfernt."));
            Player onlinePlayer = Bukkit.getPlayer(args[1]);
            if (onlinePlayer != null) {
                onlinePlayer.kick(SkyBlockPlugin.instance().getWhitelistManager().getKickMessage(), PlayerKickEvent.Cause.WHITELIST);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("list")) {
            player.sendMessage(ChatAction.of("Whitelist:"));
            for (UUID uuid : SkyBlockPlugin.instance().getWhitelistManager().whitelistedPlayers()) {
                player.sendMessage(ChatAction.of(" - " + Bukkit.getOfflinePlayer(uuid).getName()));
            }
            return;
        }
        player.sendMessage(ChatAction.of("/Whitelist <on/off>"));
        player.sendMessage(ChatAction.of("/Whitelist add <Spielername>"));
        player.sendMessage(ChatAction.of("/Whitelist remove <Spielername>"));
        player.sendMessage(ChatAction.of("/Whitelist list"));
    }
}
