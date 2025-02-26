package mc.skyblock.plugin.punish.model.reason;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.punish.model.reason.type.PunishType;

import java.time.Duration;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum PunishReason {

    CHEATING(1, "Cheating", Duration.ofDays(7), "punish.cheating", false, PunishType.BAN),
    HACKING(2, "Hacking", Duration.ofDays(30), "punish.hacking", false, PunishType.BAN),
    SPAMMING(3, "Spamming", Duration.ofHours(1), "punish.spamming", false, PunishType.MUTE),
    ADVERTISING(4, "Advertising", Duration.ofDays(1), "punish.advertising", false, PunishType.MUTE),
    INAPPROPRIATE_LANGUAGE(5, "Inappropriate Language", Duration.ofHours(1), "punish.inappropriate_language", false, PunishType.MUTE),
    INAPPROPRIATE_SKIN(6, "Inappropriate Skin", Duration.ofDays(1), "punish.inappropriate_skin", false, PunishType.BAN),
    ;

    int id;
    String name;
    Duration duration;
    String neededPermission;
    boolean isPermanent;
    PunishType punishType;

}
