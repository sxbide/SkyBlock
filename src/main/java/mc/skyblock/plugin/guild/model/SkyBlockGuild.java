package mc.skyblock.plugin.guild.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SkyBlockGuild {

    @Id
    UUID uniqueId;

    String guildName;

    UUID leaderUniqueId;
    List<UUID> guildMembers;

}
