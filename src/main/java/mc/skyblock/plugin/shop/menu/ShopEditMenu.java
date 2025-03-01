package mc.skyblock.plugin.shop.menu;

import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.conversation.ShopDeletionPrompt;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Conversation;
import mc.skyblock.plugin.util.ItemBuilder;
import mc.skyblock.plugin.shop.model.Shop;
import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.Material;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ShopEditMenu implements InventoryProvider {

    RyseInventory ryseInventory;
    Shop shop;

    public ShopEditMenu(Shop shop) {
        this.shop = shop;
        this.ryseInventory = RyseInventory.builder()
                .title("Shop bearbeiten: " + shop.getName())
                .rows(3)
                .provider(this)
                .build(SkyBlockPlugin.instance());
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Util.defaultInventory(contents);

        contents.set(10, Util.exitButton());

        contents.set(12, IntelligentItem.of(ItemBuilder.of(Material.CHEST).displayName("§7Items").appendLore("§8§m-------------", "§7Hier kannst du die Items", "§7deines Shops bearbeiten.").build(), event -> {
            //TODO: Open ShopItemsMenu
        }));

        contents.set(14, IntelligentItem.of(ItemBuilder.of(Material.NAME_TAG).displayName("§7Rabatt").appendLore("§8§m-------------", "§7Hier kannst du den Rabatt", "§7deines Shops bearbeiten.").build(), event -> {
            //TODO: Open ShopDiscountMenu
        }));

        contents.set(16, IntelligentItem.of(ItemBuilder.of(Material.EMERALD).displayName("§cShop löschen").appendLore("§8§m-------------", "§7Hier kannst du deinen Shop", "§7löschen.").build(), event -> {
            player.closeInventory();
            deletionConversation(player);
        }));
    }

    private void deletionConversation(Player player) {
        ConversationFactory factory = new ConversationFactory(SkyBlockPlugin.instance())
                .withFirstPrompt(new ShopDeletionPrompt(shop))
                .withLocalEcho(false)
                .withEscapeSequence("cancel");
        Conversation conversation = factory.buildConversation(player);
        conversation.begin();
    }
}
