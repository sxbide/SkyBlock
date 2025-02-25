package mc.skyblock.plugin.island.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mc.skyblock.plugin.SkyBlockPlugin;
import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SkyBlockIsland {

    @Id
    UUID uniqueId;

    UUID ownerUniqueId;
    long level;
    Location warpLocation;
    Map<IslandSetting, Boolean> islandSettings;


    public void setBoughtWarpLocation(Location location) {
        this.warpLocation = location;
        SkyBlockPlugin.instance().getIslandManager().saveIsland(this);
    }

    public boolean getSetting(IslandSetting islandSetting) {
        return islandSettings.getOrDefault(islandSetting, islandSetting.defaultValue);
    }

    public void setSetting(IslandSetting islandSetting, boolean value) {
        this.islandSettings.put(islandSetting, value);
        SkyBlockPlugin.instance().getIslandManager().saveIsland(this);
    }
}
