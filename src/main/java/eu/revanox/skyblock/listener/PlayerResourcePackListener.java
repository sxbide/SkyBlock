package eu.revanox.skyblock.listener;

import eu.revanox.skyblock.util.builder.ResourcePackRequestBuilder;
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

//        String resourcePack = "https://download.mc-packs.net/pack/ab9654d76796ed80d403b0eafe81e501cf615d9c.zip";
//        String sha1ResourcePack = "ab9654d76796ed80d403b0eafe81e501cf615d9c";
//        player.setResourcePack(player.getUniqueId(), resourcePack, sha1ResourcePack,
//                MiniMessage.miniMessage().deserialize("<gradient:#E2A574:#E0EF50>Blockarion</gradient>")
//                        .appendNewline().append(Component.text("Bitte akzeptiere unser Texturenpack, um spielen zu können!", NamedTextColor.RED)), true);

        ResourcePackRequestBuilder.create()
                .player(player)
                .url("https://download.mc-packs.net/pack/074269c23c83c6e5f9e23d512196a7d9b5539849.zip")
                .kickMessage(
                        MiniMessage.miniMessage().deserialize("<gradient:#E2A574:#E0EF50>Blockarion</gradient>"),
                        Component.text("Bitte akzeptiere unser Texturenpack, um spielen zu können!", NamedTextColor.RED)
                )
                .send();
    }
}
