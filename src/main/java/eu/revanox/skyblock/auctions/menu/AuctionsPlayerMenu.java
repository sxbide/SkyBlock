package eu.revanox.skyblock.auctions.menu;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.auctions.model.AuctionItem;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.ItemBuilder;
import eu.revanox.skyblock.util.NumberUtil;
import eu.revanox.skyblock.util.Util;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class AuctionsPlayerMenu implements InventoryProvider {

    RyseInventory ryseInventory;

    public AuctionsPlayerMenu() {
        this.ryseInventory = RyseInventory.builder()
                .title("Deine Auktionen")
                .rows(6)
                .provider(this)
                .build(SkyBlockPlugin.instance());
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Util.defaultInventory(contents);

        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(21);
        pagination.iterator(SlotIterator.builder()
                .startPosition(10)
                .blackList(17, 18, 26,27)
                .override()
                .type(SlotIterator.SlotIteratorType.HORIZONTAL)
                .build());

        List<Map.Entry<Integer, AuctionItem>> auctionItems = SkyBlockPlugin.instance().getAuctionsManager().getAuctionItems().entrySet().stream()
                .filter(integerAuctionItemEntry -> integerAuctionItemEntry.getValue().getSellerUniqueId().equals(player.getUniqueId())).toList();

        contents.set(49, IntelligentItem.of(ItemBuilder.of(Material.BARRIER)
                .displayName("§cZurück zum Startseite")
                .lore(
                        Component.empty(),
                        Component.text("§7<Linksklicke zum zurückgehen>")
                )
                .build(), event -> {

            event.setCancelled(true);
            new AuctionsMenu().getRyseInventory().open(player);
        }));

        if (auctionItems.isEmpty()) {
            contents.set(0, ItemBuilder.of(Material.PAPER).displayName("§cDu hast keine Auktionen :(").build());
            return;
        }

        auctionItems.forEach(integerAuctionItemEntry -> {

            ItemBuilder auctionItemStack = ItemBuilder.of(integerAuctionItemEntry.getValue().getItemStack().clone());

            auctionItemStack.getLore().add(Component.empty());
            auctionItemStack.getLore().add(Component.text("§7Preis: §e" + NumberUtil.formatBalance(integerAuctionItemEntry.getValue().getPrice()) + " ⛃"));
            auctionItemStack.getLore().add(Component.empty());
            auctionItemStack.getLore().add(Component.text("§c<Shift+Linksklicke zum entfernen>"));

            pagination.addItem(IntelligentItem.of(auctionItemStack.build(), event -> {

                if (event.getClick() == ClickType.SHIFT_LEFT) {
                    if(Objects.equals(event.getInventory().getItem(event.getSlot()), Util.placeholderItem())) {
                        return;
                    }

                    if (!SkyBlockPlugin.instance().getAuctionsManager().isItemAvailable(integerAuctionItemEntry.getKey())) {
                        player.sendMessage(ChatAction.failure("§cDieses Item wurde bereits verkauft."));
                        return;
                    }

                    event.getInventory().setItem(event.getSlot(), Util.placeholderItem());
                    player.sendMessage(ChatAction.of("§aDeine Auktion wurde erfolgreich entfernt."));

                    player.getInventory().addItem(integerAuctionItemEntry.getValue().getItemStack());
                    //skyBlockUser.addBalance(integerAuctionItemEntry.getValue().getPrice());

                    SkyBlockPlugin.instance().getAuctionsManager().deleteAuction(integerAuctionItemEntry.getKey());
                }
            }));

        });

        contents.set(53, Util.nextButton(pagination));
        contents.set(45, Util.backButton(pagination));
    }
}
