package mc.skyblock.plugin.util.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Accessors(fluent = true)
public enum CustomItems {

    ANTIQUE_KEY(ItemBuilder.of(Material.PAPER).customModelData(3)
            .displayName("§7Antiker Schlüssel")
            .build());


    ItemStack itemStack;


}
