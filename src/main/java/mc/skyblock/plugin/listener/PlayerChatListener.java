package mc.skyblock.plugin.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.guild.model.SkyBlockGuild;
import mc.skyblock.plugin.punish.model.mute.Mute;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.ResourceIcons;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;

import java.time.Duration;
import java.util.Comparator;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        TextComponent rawChat = ((TextComponent) event.message());
        Player player = event.getPlayer();
        Team playerTeam = SkyBlockPlugin.instance().getTablistManager().getTeamByPlayer(player);

        for (ResourceIcons resourceIcon : ResourceIcons.values()) {
            if (event.message().contains(Component.text(resourceIcon.unicode()))) {
                event.setCancelled(true);
            }
        }

        if (SkyBlockPlugin.instance().getPunishManager().hasActiveMutes(player.getUniqueId())) {
            Mute latestMute = SkyBlockPlugin.instance().getPunishManager().getMutes(player.getUniqueId()).stream().max(Comparator.comparing(Mute::getMutedAt)).orElse(null);
            if (latestMute != null) {
                player.sendMessage(SkyBlockPlugin.instance().getPunishManager().getMuteScreen(latestMute));
                event.setCancelled(true);
                return;
            }
        }

        event.renderer((source, component, message, viewer) -> {
            Component chatMessage = message;
            String raw = rawChat.content();
            for (Player onlinePlayer : SkyBlockPlugin.instance().getServer().getOnlinePlayers()) {
                if (!raw.contains(onlinePlayer.getName())) {
                    continue;
                }
                if (onlinePlayer.getUniqueId().equals(source.getUniqueId())) continue;
                raw = raw.replace(onlinePlayer.getName(), "<yellow>" + onlinePlayer.getName() + "<white>");
                Title title = Title.title(Component.text("Du wurdest in einer Nachricht erwähnt", NamedTextColor.YELLOW),
                        Component.text("von " + source.getName(), NamedTextColor.GRAY),
                        Title.Times.times(Duration.ofMillis(100), Duration.ofSeconds(1), Duration.ofMillis(500)));
                onlinePlayer.showTitle(title);
                onlinePlayer.playSound(onlinePlayer.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                if (PlayerAfkListener.afkPlayers.contains(onlinePlayer)) {
                    player.sendMessage(ChatAction.info(onlinePlayer.getName() + " ist AFK. Er wird möglicherweise nicht antworten."));
                }
            }

            SkyBlockGuild skyBlockGuild = SkyBlockPlugin.instance().getGuildManager().getGuild(player);

            message = playerTeam.prefix()
                    .append(Component.text(player.getName(), NamedTextColor.GRAY))
                    .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                    .append((player.isOp() ? MiniMessage.miniMessage().deserialize(raw) : chatMessage));

            return message;
        });
    }
}
