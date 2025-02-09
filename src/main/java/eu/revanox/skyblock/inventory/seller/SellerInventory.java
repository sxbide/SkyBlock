package eu.revanox.skyblock.inventory.seller;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.seller.SellerItems;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.ItemBuilder;
import eu.revanox.skyblock.util.NumberUtil;
import eu.revanox.skyblock.util.SoundAction;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SellerInventory implements Listener {

    public SellerInventory() {
        Bukkit.getPluginManager().registerEvents(this, SkyBlockPlugin.instance());
    }

    public Inventory inventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, Component.text("Verkäufer"));

        ItemBuilder placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                .displayName("§a-/-");

        ItemBuilder sellItems = ItemBuilder.of(Material.EMERALD)
                .displayName("§aAlle Items verkaufen")
                .lore(
                        Component.empty(),
                        Component.text("§7Alle sich im Inventar befindenen Items werden"),
                        Component.text("§7daraufhin für den Verkaufspreis verkauft."),
                        Component.empty(),
                        Component.text("§7<Linksklicke zum verkaufen>")
                );

        ItemBuilder listItems = ItemBuilder.of(Material.WRITABLE_BOOK)
                .displayName("§aVerkaufsliste öffnen")
                .lore(
                        Component.empty(),
                        Component.text("§7<Linksklicke zum öffnen>")
                );

        for (int i = 45; i < 54; i++) {
            inventory.setItem(i, placeholder.build());
        }

        inventory.setItem(47, sellItems.build());
        inventory.setItem(51, listItems.build());

        SoundAction.playInventoryOpen(player);
        return inventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (event.getView().title().equals(Component.text("Verkäufer"))) {


                if (event.getRawSlot() >= 45 && event.getRawSlot() <= 54 && event.getRawSlot() != 47 && event.getRawSlot() != 51) {
                    event.setCancelled(true);
                    return;
                }

                if (event.getRawSlot() == 47) {
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
                        SoundAction.playTaskFailed(player);
                        player.sendMessage(ChatAction.failure("§cEinige Items konnten nicht verkauft werden."));
                        player.sendMessage(ChatAction.failure("§cSchau in die Verkaufsliste, ob dein Item verkauft werden kann."));
                    }

                    player.closeInventory();
                    return;
                }

                if (event.getRawSlot() == 51) {
                    player.openInventory(SkyBlockPlugin.instance().getSellerListInventory().inventory(player));
                }
            }
        }
    }
}
