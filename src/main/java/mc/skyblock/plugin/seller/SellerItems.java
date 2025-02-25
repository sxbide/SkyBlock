package mc.skyblock.plugin.seller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum SellerItems {

    GOLDEN_CARROT(0.20, Material.GOLDEN_CARROT, "Goldene Karotte"),
    COBBLESTONE(0.10, Material.COBBLESTONE, "Bruchstein"),
    DIAMOND(0.50, Material.DIAMOND, "Diamant"),
    EMERALD(0.40, Material.EMERALD, "Smaragd"),
    IRON_INGOT(0.30, Material.IRON_INGOT, "Eisenbarren"),
    GOLD_INGOT(0.40, Material.GOLD_INGOT, "Goldbarren"),

    ;

    double price;
    Material material;
    String displayName;


}
