package eu.revanox.skyblock.inventory.seller;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.seller.SellerItems;
import eu.revanox.skyblock.util.ItemBuilder;
import eu.revanox.skyblock.util.NumberUtil;
import eu.revanox.skyblock.util.SoundAction;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.concurrent.atomic.AtomicInteger;

public class SellerListInventory implements Listener {

    public SellerListInventory() {
        Bukkit.getPluginManager().registerEvents(this, SkyBlockPlugin.instance());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getWhoClicked() instanceof Player player) {
            if (event.getView().title().equals(Component.text("Verkäufer - Liste"))) {
                event.setCancelled(true);
            }
        }
    }

    public Inventory inventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, Component.text("Verkäufer - Liste"));

        AtomicInteger slot = new AtomicInteger(0);
        for (SellerItems value : SellerItems.values()) {

            ItemBuilder itemStack = ItemBuilder.of(value.getMaterial())
                    .displayName("§e" + value.getDisplayName())
                    .lore(
                            Component.text("§7Verkaufspreis: §e" + NumberUtil.formatBalance(value.getPrice()) + " ⛃")
                    );

            inventory.setItem(slot.getAndIncrement(), itemStack.build());
        }

        SoundAction.playInventoryOpen(player);
        return inventory;
    }
}
