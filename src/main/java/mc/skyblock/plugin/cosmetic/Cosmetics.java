package mc.skyblock.plugin.cosmetic;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.cosmetic.model.Cosmetic;
import mc.skyblock.plugin.cosmetic.model.CosmeticType;
import mc.skyblock.plugin.util.Rarity;
import org.bukkit.inventory.ItemStack;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public enum Cosmetics {

    HEADSET(new Cosmetic("§7Kopfhörer", 101,100, CosmeticType.HEAD, Rarity.COMMON, false)),
    BANANA(new Cosmetic("§eKrumme Banane", 103,100, CosmeticType.HAND, Rarity.COMMON, true)),
    LEVIATHAN_AXE(new Cosmetic("§bAxt des Leviathans", 104,100, CosmeticType.HAND, Rarity.RARE, true))
    ;

    Cosmetic cosmetic;


    public static boolean isCosmeticItem(ItemStack itemStack) {
        for (Cosmetics value : values()) {
            if(itemStack.getItemMeta() != null && itemStack.getItemMeta().hasCustomModelData() && itemStack.getItemMeta().getCustomModelData() == value.cosmetic.getCustomModelData()) {
                return true;
            }
        }
        return false;
    }
}
