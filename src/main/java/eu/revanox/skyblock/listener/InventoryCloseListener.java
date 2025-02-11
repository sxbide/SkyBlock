package eu.revanox.skyblock.listener;

import eu.revanox.skyblock.util.SoundAction;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {

        if (event.getPlayer() instanceof Player player) {
            if (event.getView().title().equals(Component.text("Was möchtest du verkaufen?"))) {
                if (event.getInventory().getItem(0) != null) {
                    for (int i = 0; i < 44; i++) {
                        ItemStack itemStack = event.getInventory().getItem(i);


                        if (itemStack != null) {
                            player.getInventory().addItem(itemStack);
                            event.getInventory().setItem(i, ItemStack.empty());
                        }
                    }
                }
            }
        }
    }
}
