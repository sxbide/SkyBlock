package eu.revanox.skyblock.seller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

@Getter
@AllArgsConstructor
public enum SellerItems {

    GOLDEN_CARROT(0.20, Material.GOLDEN_CARROT, "Goldene Karotte"),
    COBBLESTONE(0.10, Material.COBBLESTONE, "Bruchstein");

    double price;
    Material material;
    String displayName;


}
