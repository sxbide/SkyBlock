package mc.skyblock.plugin.punish.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.configuration.Configuration;
import mc.skyblock.plugin.configuration.annotation.ConfigPath;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PunishConfiguration extends Configuration {

    @ConfigPath("punish.screens.ban")
    List<String> banScreen = List.of(
            "{prefix}<red>You have been banned from the server!",
            "<red>Reason: <gray>{reason}",
            "<red>Banned by: <gray>{bannedBy}",
            "<red>Banned at: <gray>{bannedAt}",
            "<red>Expires at: <gray>{expireAt}",
            "<red>Appeal at: <gray>discord.gg/skyblock"
    );

    @ConfigPath("punish.screens.mute")
    List<String> muteScreen = List.of(
            "{prefix}<red>You have been muted from the server!",
            "<red>Reason: <gray>{reason}",
            "<red>Muted by: <gray>{mutedBy}",
            "<red>Expire at: <gray>{expireAt}",
            "<red>Appeal at: <gray>discord.gg/skyblock"
    );

    @ConfigPath("punish.screens.warn")
    List<String> warnScreen = List.of(
            "{prefix}<red>You have been warned on the server!",
            "<red>Reason: <gray>{reason}",
            "<red>Warned by: <gray>{warnedBy}",
            "<red>Expire at: <gray>{expireAt}"
    );

    @ConfigPath("punish.screens.kick")
    List<String> kickScreen = List.of(
            "{prefix}<red>You have been kicked from the server!",
            "<red>Reason: <gray>{reason}",
            "<red>Kicked by: <gray>{kickedBy}"
    );

}
