package mc.skyblock.plugin.shop.menu;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.shop.model.Shop;
import mc.skyblock.plugin.shop.model.item.ShopItem;
import mc.skyblock.plugin.user.model.SkyBlockUser;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.ItemBuilder;
import mc.skyblock.plugin.util.NumberUtil;
import mc.skyblock.plugin.util.Util;
import mc.skyblock.plugin.util.custom.CustomSounds;
import mc.skyblock.plugin.util.data.BlockData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ShopListMenu implements InventoryProvider {

    RyseInventory ryseInventory;
    Shop shop;

    public ShopListMenu(Shop shop) {
        this.shop = shop;
        this.ryseInventory = RyseInventory.builder()
                .provider(this)
                .rows(5)
                .title(MiniMessage.miniMessage().deserialize("Shop: <#ffaa00>" + shop.getName()))
                .build(SkyBlockPlugin.instance());
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        Pagination pagination = contents.pagination();
        SlotIterator iterator = SlotIterator.builder()
                .startPosition(10)
                .endPosition(35)
                .blackList(17, 18, 26, 27)
                .build();
        pagination.iterator(iterator);

        Util.borderInventory(contents);

        if (shop.getItems().isEmpty()) {
            contents.set(22, ItemBuilder.of(Material.BARRIER).displayName("§cKeine Items vorhanden").build());

            contents.set(40, Util.exitButton());
            return;
        }

        for (ShopItem item : shop.getItems()) {
            ItemBuilder shopItem = ItemBuilder.of(item.getItemStack().clone());
            shopItem.appendLore(
                    Component.text("§8§m----------------------§r"),
                    Component.empty(),
                    MiniMessage.miniMessage().deserialize("<gray>Kosten: <yellow>" + NumberUtil.formatBalance(item.getPrice()) + " " + (shop.getCurrencyFormat().getIcon() == null ? shop.getCurrencyFormat().getDisplayName() : shop.getCurrencyFormat().getIcon())),
                    Component.empty(),
                    Component.text("§7<Linksklicke zum kaufen>")
            );
            pagination.addItem(IntelligentItem.of(shopItem.build(), event -> {
                event.setCancelled(true);
                SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());
                Number balance = skyBlockUser.getBalance(shop.getCurrencyFormat());
                if (balance.doubleValue() < item.getPrice()) {
                    player.sendMessage(ChatAction.failure("Du hast nicht genügend " + shop.getCurrencyFormat().getDisplayName() + " um dieses Item zu kaufen."));
                    return;
                }
                skyBlockUser.removeBalance(shop.getCurrencyFormat(), item.getPrice());
                player.getInventory().addItem(item.getItemStack());
                Component itemDisplayName = item.getItemStack().getItemMeta().displayName() == null ? Component.text(BlockData.findBlockByMaterial(item.getItemStack().getType()).getName()) : item.getItemStack().getItemMeta().displayName();
                player.sendMessage(ChatAction.of("Du hast erfolgreich ").append(itemDisplayName).append(MiniMessage.miniMessage().deserialize("<#6cd414> für " + NumberUtil.formatBalance(item.getPrice()) + " " + shop.getCurrencyFormat().getDisplayName() + " gekauft.")));
                CustomSounds.CASHIER.playSound(player, 0.6F, 1.0F, player.getLocation());
            }));
        }

        contents.set(43, Util.nextButton(pagination));
        contents.set(40, Util.exitButton());
        contents.set(37, Util.backButton(pagination));

    }
}
