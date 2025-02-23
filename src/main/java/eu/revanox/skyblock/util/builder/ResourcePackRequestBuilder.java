package eu.revanox.skyblock.util.builder;

import net.kyori.adventure.resource.ResourcePackCallback;
import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;

import java.net.URI;
import java.util.Arrays;

public class ResourcePackRequestBuilder {

    String url;
    Component kickMessage;
    Player player;

    private ResourcePackRequestBuilder() {

    }

    public static ResourcePackRequestBuilder create() {
        return new ResourcePackRequestBuilder();
    }

    public ResourcePackRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public ResourcePackRequestBuilder kickMessage(Component... kickMessage) {
        this.kickMessage = Component.text().append(Arrays.asList(kickMessage)).build();
        return this;
    }

    public ResourcePackRequestBuilder player(Player player) {
        this.player = player;
        return this;
    }

    public void send() {
        ResourcePackRequest request = ResourcePackRequest.resourcePackRequest()
                .required(true)
                .packs(
                        ResourcePackInfo.resourcePackInfo(player.getUniqueId(), URI.create(this.url), getHashFromUrl(this.url))
                ).callback(ResourcePackCallback.onTerminal(((uuid, audience) -> {
            //say thanks (edit: thanks)
        }), ((uuid, audience) -> {
            this.player.kick(this.kickMessage, PlayerKickEvent.Cause.RESOURCE_PACK_REJECTION);
        }))).build();
        this.player.sendResourcePacks(request);
    }

    private String getHashFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1).replaceAll(".zip", "");
    }

}
