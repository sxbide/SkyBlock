package mc.skyblock.plugin.cosmetic.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.util.Rarity;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cosmetic {

    String name;
    int customModelData;
    double price;
    CosmeticType type;
    Rarity rarity;

    boolean holdable;
}
