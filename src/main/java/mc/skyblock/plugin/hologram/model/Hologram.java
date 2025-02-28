package mc.skyblock.plugin.hologram.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Hologram {

    @Id
    String name;

    Location location;

    Color backgroundColor;
    Display.Billboard billboard;
    List<String> lines; //MiniMessage format

    boolean persistent;

}
