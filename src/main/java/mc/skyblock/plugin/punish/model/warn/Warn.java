package mc.skyblock.plugin.punish.model.warn;

import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Warn {

    @Id
    UUID warnId;

    UUID warnedPlayer;
    UUID warnedBy;

    String reason;

    LocalDateTime warnedAt;
    LocalDateTime expireAt;

    public boolean isActive() {
        return expireAt == null || expireAt.isAfter(LocalDateTime.now());
    }

    public boolean isPermanent() {
        return expireAt == null;
    }

}
