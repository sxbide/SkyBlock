package mc.skyblock.plugin.npc.model.player;

import com.destroystokyo.paper.profile.PlayerProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Skin {

    String url;

    public static Skin of(@NotNull PlayerProfile profile) {
        return new Skin(profile.getTextures().getSkin().toString());
    }

    public static Skin of(@NotNull String url) {
        return new Skin(url);
    }

    public boolean isDefault() {
        return url == null || url.isEmpty();
    }

}
