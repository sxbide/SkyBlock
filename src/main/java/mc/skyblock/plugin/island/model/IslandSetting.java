package mc.skyblock.plugin.island.model;

import lombok.AllArgsConstructor;
import org.bukkit.Material;

@AllArgsConstructor
public enum IslandSetting {


    PVP(Material.DIAMOND_SWORD, "PvP", false),
    EXPLOSIONS(Material.TNT, "Explosions", false);

    Material displayItem;
    String displayName;
    boolean defaultValue;
}
