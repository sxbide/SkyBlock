package mc.skyblock.plugin.cosmetic;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.cosmetic.model.Cosmetic;
import mc.skyblock.plugin.cosmetic.model.CosmeticType;
import org.bukkit.inventory.ItemStack;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Getter
public enum Cosmetics {

    DEBUG_HAT(new Cosmetic("§cDebug Hat", 101, CosmeticType.HEAD, false)),
    DEBUG_ITEM(new Cosmetic("§cDebug Item", 102, CosmeticType.HAND, true));

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
