package eu.revanox.skyblock.listener;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.guild.model.SkyBlockGuild;
import eu.revanox.skyblock.util.ResourceIcons;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scoreboard.Team;

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

            SkyBlockGuild skyBlockGuild = SkyBlockPlugin.instance().getGuildManager().getGuild(player);

            message = playerTeam.prefix()
                    .append(Component.text(player.getName(), NamedTextColor.GRAY))
                    .append(Component.text(": ", NamedTextColor.DARK_GRAY))
                    .append((player.isOp() ? MiniMessage.miniMessage().deserialize(rawChat.content()) : chatMessage));

            return message;
        });
    }
}
