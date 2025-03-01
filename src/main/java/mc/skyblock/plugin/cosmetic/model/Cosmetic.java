package mc.skyblock.plugin.cosmetic.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cosmetic {

    String name;
    int customModelData;
    CosmeticType type;

    boolean holdable;
}
