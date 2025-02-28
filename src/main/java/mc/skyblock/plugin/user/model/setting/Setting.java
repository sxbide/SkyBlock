package mc.skyblock.plugin.user.model.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum Setting {

    SPAWN_BACKGROUND_MUSIC("spawn_background_music", "Spawn: Hintergrundmusik", 0, Map.of(
            0, "<red>Off",
            1, "<green>On"
    )),
    ;

    String name;
    String description; //MiniMessage

    int defaultValue;
    Map<Integer, String> values;

}
