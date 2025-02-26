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

    @ConfigPath("punish.ban.screen")
    List<String> banScreen = List.of(
            "{prefix}<red>You have been banned from the server!",
            "<red>Reason: <gray>{reason}",
            "<red>Banned by: <gray>{bannedBy}",
            "<red>Banned at: <gray>{bannedAt}",
            "<red>Expires at: <gray>{expireAt}",
            "<red>Appeal at: <gray>discord.gg/skyblock"
    );

    @ConfigPath("punish.mute.screen")
    List<String> muteScreen = List.of(
            "{prefix}<red>You have been muted from the server!",
            "<red>Reason: <gray>{reason}",
            "<red>Muted by: <gray>{mutedBy}",
            "<red>Expire at: <gray>{expireAt}",
            "<red>Appeal at: <gray>discord.gg/skyblock"
    );

    @ConfigPath("punish.mute.blocked-commands")
    List<String> blockedCommands = List.of(
            "tell",
            "msg",
            "whisper",
            "w",
            "r",
            "reply",
            "me",
            "em",
            "emote",
            "minecraft:me",
            "minecraft:em",
            "minecraft:emote",
            "minecraft:tell",
            "minecraft:msg",
            "minecraft:whisper",
            "minecraft:w",
            "minecraft:r",
            "minecraft:reply",
            "skyblock:msg",
            "skyblock:whisper",
            "skyblock:w",
            "skyblock:r",
            "skyblock:reply"
    );

    @ConfigPath("punish.warn.screen")
    List<String> warnScreen = List.of(
            "{prefix}<red>You have been warned on the server!",
            "<red>Reason: <gray>{reason}",
            "<red>Warned by: <gray>{warnedBy}",
            "<red>Expire at: <gray>{expireAt}"
    );

    @ConfigPath("punish.kick.screen")
    List<String> kickScreen = List.of(
            "{prefix}<red>You have been kicked from the server!",
            "<red>Reason: <gray>{reason}",
            "<red>Kicked by: <gray>{kickedBy}"
    );

}
