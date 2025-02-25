package mc.skyblock.plugin.perks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mc.skyblock.plugin.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public enum Perks {

    FAST_BREAK("§aFlinke Finger", ItemBuilder.of(Material.AXOLOTL_BUCKET)
            .displayName("§aFlinke Finger - Vorteil")
            .lore(
                    Component.empty(),
                    Component.text("§7Halte dieses Vorteilsitem in deiner"),
                    Component.text("§7Zweithand (Off-Hand [F]) um schneller"),
                    Component.text("§7Blöcke abbauen zu können!")
            ).build(), 1000.0);


    String displayName;
    ItemStack itemStack;
    double price;


    public static Perks isPerkItem(ItemStack itemStack) {
        for (Perks value : values()) {
            if (itemStack.equals(value.getItemStack())) {
                return value;
            }
        }
        return null;
    }


}
