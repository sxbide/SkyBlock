package eu.revanox.skyblock.seller.menu;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.seller.SellerItems;
import eu.revanox.skyblock.util.ItemBuilder;
import eu.revanox.skyblock.util.NumberUtil;
import eu.revanox.skyblock.util.Util;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class SellerListMenu implements InventoryProvider {

    RyseInventory ryseInventory;
    Pagination pagination;
    int page;

    public SellerListMenu() {

        this.ryseInventory = RyseInventory.builder()
                .title("Preisübersicht")
                .rows(6)
                .provider(this)
                .disableUpdateTask()
                .build(SkyBlockPlugin.instance());

        this.page = 0;
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        Util.borderInventory(contents);

        pagination = contents.pagination();
        SlotIterator slotIterator = SlotIterator.builder()
                .override()
                .startPosition(10)
                .endPosition(43)
                .blackList(17, 18, 26, 27, 35, 36)
                .build();
        pagination.iterator(slotIterator);

        contents.fillArea(0, 44, IntelligentItem.of(ItemStack.empty(), event -> {
            event.setCancelled(true);
        }));

        contents.fillArea(45, 53, IntelligentItem.of(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                .displayName("§r").build(), event -> {
            event.setCancelled(true);
        }));

        contents.set(49, IntelligentItem.of(ItemBuilder.of(Material.BARRIER)
                .displayName(MiniMessage.miniMessage().deserialize("<#ff0000>Zurück zum Startseite"))
                .lore(
                        Component.empty(),
                        Component.text("§7<Linksklicke zum zurückgehen>")
                )
                .build(), event -> {

            event.setCancelled(true);
            new SellerMenu().getRyseInventory().open(player);

        }));

        contents.set(53, Util.nextButton(pagination));
        contents.set(45, Util.backButton(pagination));

        for (SellerItems value : SellerItems.values()) {

            ItemBuilder itemStack = ItemBuilder.of(value.getMaterial())
                    .displayName("§e" + value.getDisplayName())
                    .lore(
                            Component.text("§7Verkaufspreis: §e" + NumberUtil.formatBalance(value.getPrice()) + " ⛃")
                    );

            pagination.addItem(IntelligentItem.of(itemStack.build(), event -> {
                event.setCancelled(true);
            }));
        }

        update(0, contents);
    }

    public void update(int newPage, InventoryContents contents) {
        //TODO: Implement update method
    }
}
