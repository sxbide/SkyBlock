package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.util.ChatAction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends AbstractCommand {

    public PingCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "ping", null, "pong", "latency", "latenz");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatAction.info("Dein Ping beträgt ").append(formatPing(player.getPing())).append(MiniMessage.miniMessage().deserialize("<#c0f0fb>.")));
            return;
        }
        String targetName = args[0];
        Player target = plugin.getServer().getPlayer(targetName);
        if (target == null) {
            player.sendMessage(ChatAction.getOffline());
            return;
        }
        player.sendMessage(ChatAction.info("Der Ping von ").append(Component.text("§e" + target.getName())).append(MiniMessage.miniMessage().deserialize("<#c0f0fb> beträgt ")).append(formatPing(target.getPing())).append(MiniMessage.miniMessage().deserialize("<#c0f0fb>.")));
    }


    private Component formatPing(int ping) {
        if (ping < 30) {
            return MiniMessage.miniMessage().deserialize("<#00ff00>" + ping + "ms<reset>");
        }
        if (ping < 60) {
            return MiniMessage.miniMessage().deserialize("<#ffff00>" + ping + "ms<reset>");
        }
        if (ping < 100) {
            return MiniMessage.miniMessage().deserialize("<red>" + ping + "ms<reset>");
        }
        if (ping <= 130) {
            return MiniMessage.miniMessage().deserialize("<#ff0000>" + ping + "ms<reset>");
        }
        return MiniMessage.miniMessage().deserialize("<#9D9D9D>" + ping + "ms<reset>");
    }
}
