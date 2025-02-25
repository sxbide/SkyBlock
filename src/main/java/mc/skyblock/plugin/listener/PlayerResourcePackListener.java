package mc.skyblock.plugin.listener;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.util.builder.ResourcePackRequestBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerResourcePackListener implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ResourcePackRequestBuilder.create()
                .player(player)
                .url(SkyBlockPlugin.instance().getResourcePackConfiguration().getUrl())
                .kickMessage(
                        MiniMessage.miniMessage().deserialize("<gradient:#E2A574:#E0EF50>Blockarion</gradient>"),
                        Component.text("Bitte akzeptiere unser Texturenpaket, um spielen zu können!", NamedTextColor.RED)
                )
                .send();
    }
}
