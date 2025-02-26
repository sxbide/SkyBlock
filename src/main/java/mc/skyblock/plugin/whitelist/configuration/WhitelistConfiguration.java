package mc.skyblock.plugin.whitelist.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.configuration.Configuration;
import mc.skyblock.plugin.configuration.annotation.ConfigPath;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class WhitelistConfiguration extends Configuration {

    @ConfigPath("whitelist.enabled")
    boolean enabled = false;

    @ConfigPath("whitelist.kick-message")
    List<String> whiteListKickMessage = List.of(
            "<red><bold>Sorry, but you are not whitelisted on this server.",
            "<reset>",
            "<gray>Want to join the whitelist?",
            "<gray>Visit our website at <aqua>example.com"
    );

    @ConfigPath("whitelist.players")
    List<UUID> whitelistedPlayers = new ArrayList<>();

}
