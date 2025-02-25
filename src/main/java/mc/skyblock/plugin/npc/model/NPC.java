package mc.skyblock.plugin.npc.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.npc.model.player.Skin;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder(toBuilder = true)
public class NPC {

    @Id
    int id;

    Location location;
    String displayName;
    EntityType entityType;
    Skin skin;
    List<String> hologramLines;

}
