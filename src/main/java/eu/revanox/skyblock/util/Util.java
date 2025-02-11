package eu.revanox.skyblock.util;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class Util {

    public void defaultInventory(InventoryContents contents) {
        ItemBuilder placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                .displayName("§7-/-");
        contents.fill(placeholder.build());
    }

    public ItemStack placeholderItem() {
        ItemBuilder placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                .displayName("§7-/-");
        return placeholder.build();
    }
}
