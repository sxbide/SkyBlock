package mc.skyblock.plugin.punish.model.mute;

import eu.koboo.en2do.repository.entity.Id;
import eu.koboo.en2do.repository.entity.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.punish.model.reason.PunishReason;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Mute {

    @Id
    UUID muteId;

    UUID mutedPlayer;
    UUID mutedBy;

    PunishReason reason;

    LocalDateTime mutedAt;
    LocalDateTime expireAt;

    @Transient
    public boolean isActive() {
        return expireAt == null || expireAt.isAfter(LocalDateTime.now());
    }

    public boolean isPermanent() {
        return expireAt == null;
    }

}
