package mc.skyblock.plugin.punish.model.ban;

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
public class Ban {

    @Id
    UUID banId;

    UUID bannedPlayer;
    UUID bannedBy;

    PunishReason reason;

    LocalDateTime bannedAt;
    LocalDateTime expireAt;

    @Transient
    public boolean isActive() {
        return expireAt == null || expireAt.isAfter(LocalDateTime.now());
    }

    public boolean isPermanent() {
        return expireAt == null;
    }

}
