package eu.revanox.skyblock.auctions.menu;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.*;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

@Getter
public class AuctionsMenu implements InventoryProvider {

    RyseInventory ryseInventory;


    public AuctionsMenu() {
        this.ryseInventory = RyseInventory.builder()
                .title("Spielerauktionen")
                .rows(6)
                .provider(this)
                .build(SkyBlockPlugin.instance());
    }


    @Override
    public void init(Player player, InventoryContents contents) {

        Util.borderInventory(contents);

        for (int i = 37; i < 44; i++) {
            contents.set(i, Util.placeholderItem());
        }

        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(21);
        pagination.iterator(SlotIterator.builder()
                .startPosition(10)
                .blackList(17, 18, 26, 27)
                .override()
                .type(SlotIterator.SlotIteratorType.HORIZONTAL)
                .build());


        SkyBlockPlugin.instance().getAuctionsManager().getAuctionItems().forEach((id, auctionItem) -> {

            ItemBuilder auctionItemStack = ItemBuilder.of(auctionItem.getItemStack().clone());

            auctionItemStack.getLore().add(Component.text("§8§m-------------"));
            auctionItemStack.getLore().add(Component.empty());
            auctionItemStack.getLore().add(Component.text("§7Auktion von §e" + Bukkit.getOfflinePlayer(auctionItem.getSellerUniqueId()).getName()));
            auctionItemStack.getLore().add(Component.text("§7Kosten: §e" + (auctionItem.getPrice() > 0 ? NumberUtil.formatBalance(auctionItem.getPrice()) + " ⛃" : "§aGRATIS")));
            auctionItemStack.getLore().add(Component.empty());
            auctionItemStack.getLore().add(Component.text("§7<Linksklicke zum kaufen>"));

            pagination.addItem(IntelligentItem.of(auctionItemStack.build(), event -> {

                if (auctionItem.getSellerUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(ChatAction.failure("§cDu kannst deine eigene Auktion nicht kaufen."));
                    return;
                }

                SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());

                if (!SkyBlockPlugin.instance().getAuctionsManager().isItemAvailable(id)) {
                    player.sendMessage(ChatAction.failure("§cDieses Item wurde bereits verkauft."));
                    return;
                }

                if (skyBlockUser.getBalance() < auctionItem.getPrice()) {
                    player.sendMessage(ChatAction.failure("§cDazu ist dein Kontostand zu niedrig."));
                    return;
                }

                skyBlockUser.removeBalance(auctionItem.getPrice());
                player.getInventory().addItem(auctionItem.getItemStack());
                SoundAction.playTaskComplete(player);
                player.sendMessage(ChatAction.of("§aDu hast das Auktions Item erfolgreich gekauft."));

                SkyBlockUser sellerUser = SkyBlockPlugin.instance().getUserManager().getUser(auctionItem.getSellerUniqueId());
                Player sellerPlayer = Bukkit.getPlayer(auctionItem.getSellerUniqueId());

                sellerUser.addBalance(auctionItem.getPrice());

                if (sellerPlayer != null) {
                    SoundAction.playTaskComplete(sellerPlayer);
                    sellerPlayer.sendMessage(ChatAction.of("§aDeine Auktion wurde von " + player.getName() + " gekauft. (+" + NumberUtil.formatBalance(auctionItem.getPrice()) + " ⛃)"));
                }

                SkyBlockPlugin.instance().getAuctionsManager().deleteAuction(id);
                event.getInventory().setItem(event.getSlot(), Util.placeholderItemEdit().material(Material.GREEN_STAINED_GLASS_PANE)
                        .displayName("§aGekauft von dir").build());
            }));
        });


        contents.set(53, Util.nextButton(pagination));
        contents.set(45, Util.backButton(pagination));

        contents.set(47, IntelligentItem.of(ItemBuilder.of(Material.KNOWLEDGE_BOOK)
                .displayName("§aDeine Auktionen")
                .lore(
                        Component.empty(),
                        Component.text("§7<Linksklicke zum öffnen>")
                ).build(), event -> {
            new AuctionsPlayerMenu().getRyseInventory().open(player);
        }));

        contents.set(51, IntelligentItem.of(ItemBuilder.of(Material.GUSTER_BANNER_PATTERN)
                .itemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .displayName("§aAuktion erstellen?")
                .lore(
                        Component.empty(),
                        Component.text("§7Halte das gewünschte Item in deiner Hand"),
                        Component.text("§7und benutze /Auktion sell <Preis z.B 12.50>")
                ).build(), event -> {
            event.setCancelled(true);
        }));


        SoundAction.playInventoryOpen(player);


    }
}
