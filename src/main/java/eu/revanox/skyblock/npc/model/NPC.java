package eu.revanox.skyblock.npc.model;

import eu.koboo.en2do.repository.entity.Id;
import eu.revanox.skyblock.npc.model.player.Skin;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
