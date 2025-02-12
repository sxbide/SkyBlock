package eu.revanox.skyblock.seller.menu;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.seller.SellerItems;
import eu.revanox.skyblock.util.ItemBuilder;
import eu.revanox.skyblock.util.NumberUtil;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class SellerListMenu implements InventoryProvider {

    RyseInventory ryseInventory;

    public SellerListMenu() {

        this.ryseInventory = RyseInventory.builder()
                .title("Verkäufer - Liste")
                .rows(6)
                .provider(this)
                .disableUpdateTask()
                .build(SkyBlockPlugin.instance());
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        contents.fillArea(0, 44, IntelligentItem.of(ItemStack.empty(), event -> {
            event.setCancelled(true);
        }));

        contents.fillArea(45, 53, IntelligentItem.of(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                .displayName("§7-/-").build(), event -> {
            event.setCancelled(true);
        }));

        contents.set(49, IntelligentItem.of(ItemBuilder.of(Material.BARRIER)
                .displayName("§cZurück zum Startseite")
                .lore(
                        Component.empty(),
                        Component.text("§7<Linksklicke zum zurückgehen>")
                )
                .build(), event -> {

            event.setCancelled(true);
            new SellerMenu().getRyseInventory().open(player);

        }));

        AtomicInteger slot = new AtomicInteger(0);
        for (SellerItems value : SellerItems.values()) {

            ItemBuilder itemStack = ItemBuilder.of(value.getMaterial())
                    .displayName("§e" + value.getDisplayName())
                    .lore(
                            Component.text("§7Verkaufspreis: §e" + NumberUtil.formatBalance(value.getPrice()) + " ⛃")
                    );

            contents.set(slot.getAndIncrement(), IntelligentItem.of(itemStack.build(), event -> {
                event.setCancelled(true);
            }));
        }
    }
}
