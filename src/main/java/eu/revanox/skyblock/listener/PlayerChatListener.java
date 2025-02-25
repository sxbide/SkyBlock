package eu.revanox.skyblock.listener;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.guild.model.SkyBlockGuild;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.ResourceIcons;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scoreboard.Team;

import java.time.Duration;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncChatEvent event) {

        TextComponent rawChat = ((TextComponent)event.message());
        Player player = event.getPlayer();
        Team playerTeam = SkyBlockPlugin.instance().getTablistManager().getTeamByPlayer(player);

        for (ResourceIcons resourceIcon : ResourceIcons.values()) {
            if(event.message().contains(Component.text(resourceIcon.unicode()))) {
                event.setCancelled(true);
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
