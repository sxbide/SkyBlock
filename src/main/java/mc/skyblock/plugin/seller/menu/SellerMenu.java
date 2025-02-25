package mc.skyblock.plugin.seller.menu;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.Getter;
import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.seller.SellerItems;
import mc.skyblock.plugin.user.model.SkyBlockUser;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.ItemBuilder;
import mc.skyblock.plugin.util.NumberUtil;
import mc.skyblock.plugin.util.SoundAction;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Getter
public class SellerMenu implements InventoryProvider {

    RyseInventory ryseInventory;
    List<ItemStack> itemsToSell;

    public SellerMenu() {

        this.ryseInventory = RyseInventory.builder()
                .title("Was möchtest du verkaufen?")
                .rows(6)
                .provider(this)
                .ignoredSlots(IntStream.range(0, 44).toArray())
                .ignoreClickEvent(DisabledInventoryClick.BOTTOM)
                .enableAction(Action.MOVE_TO_OTHER_INVENTORY, Action.DOUBLE_CLICK)
                .build(SkyBlockPlugin.instance());

        this.itemsToSell = new ArrayList<>();
    }

    @Override
    public void close(Player player, RyseInventory inventory) {
        if(inventory.getInventory() != null) {
            if (inventory.getInventory().getItem(0) != null) {
                for (int i = 0; i < 44; i++) {
                    ItemStack itemStack = inventory.getInventory().getItem(i);
                    if (itemStack != null) {
                        player.getInventory().addItem(itemStack);
                        inventory.getInventory().setItem(i, ItemStack.empty());
                    }
                }
            }
        }
    }

    @Override
    public void init(Player player, InventoryContents contents) {

        contents.fillArea(45, 53, IntelligentItem.of(ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                .displayName("§7-/-").build(), event -> {
            event.setCancelled(true);
        }));


        contents.set(47, IntelligentItem.of(ItemBuilder.of(Material.EMERALD)
                .displayName("§aVerkauf bestätigen")
                .lore(
                        Component.empty(),
                        Component.text("§7Alle sich im Inventar befindenen Items werden"),
                        Component.text("§7daraufhin für den Verkaufspreis verkauft."),
                        Component.empty(),
                        Component.text("§7<Linksklicke zum verkaufen>")
                ).build(), event -> {
            event.setCancelled(true);

            if (event.getInventory().getItem(0) == null) {
                player.sendMessage(ChatAction.failure("§cBitte lege die Items rein, die du verkaufen möchtest."));
                return;
            }

            SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());
            Map<SellerItems, Integer> soldItems = new HashMap<>();
            boolean anySold = false, anyFailed = false;

            for (int i = 0; i < 44; i++) {
                ItemStack itemStack = event.getInventory().getItem(i);
                if (itemStack == null || itemStack.getType() == Material.AIR) continue;

                boolean sold = false;
                for (SellerItems value : SellerItems.values()) {
                    if (itemStack.getType().equals(value.getMaterial())) {
                        soldItems.put(value, soldItems.getOrDefault(value, 0) + itemStack.getAmount());
                        skyBlockUser.addBalance(value.getPrice() * itemStack.getAmount());
                        event.getInventory().setItem(i, null);
                        anySold = true;
                        sold = true;
                        break;
                    }
                }

                if (!sold) {
                    player.getInventory().addItem(itemStack);
                    event.getInventory().setItem(i, null);
                    anyFailed = true;
                }
            }

            for (Map.Entry<SellerItems, Integer> entry : soldItems.entrySet()) {
                SellerItems item = entry.getKey();
                int totalAmount = entry.getValue();
                double totalPrice = item.getPrice() * totalAmount;

                int stacks = totalAmount / 64;
                int remainder = totalAmount % 64;
                String amountDisplay = (stacks > 0 ? stacks + "x64" : "") + (remainder > 0 ? (stacks > 0 ? " + " : "") + remainder : "");

                player.sendMessage(ChatAction.of("§a" + amountDisplay + " " + item.getDisplayName() + " wurde für " + NumberUtil.formatBalance(totalPrice) + " ⛃ verkauft."));
            }

            if (anySold) SoundAction.playTaskComplete(player);
            if (anyFailed) {

                if (event.getInventory().getContents().length > 1) {
                    player.sendMessage(ChatAction.failure("§cEinige Items konnten nicht verkauft werden."));
                }
                player.sendMessage(ChatAction.failure("§cSchau in die Verkaufsliste, ob dein Item verkauft werden kann."));
                for (int i = 0; i < 44; i++) {
                    contents.set(i, IntelligentItem.empty(new ItemStack(Material.AIR)));
                }
            }

        }));

        contents.set(51, IntelligentItem.of(ItemBuilder.of(Material.GUSTER_BANNER_PATTERN)
                .itemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)
                .displayName("§6Preisübersicht öffnen")
                .lore(
                        Component.empty(),
                        Component.text("§7<Linksklicke zum öffnen>")
                ).build(), event -> {

            event.setCancelled(true);
            new SellerListMenu().getRyseInventory().open(player);
        }));

        SoundAction.playInventoryOpen(player);
    }
}
