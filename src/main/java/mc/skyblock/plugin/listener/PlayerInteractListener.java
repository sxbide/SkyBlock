package mc.skyblock.plugin.listener;

import mc.skyblock.plugin.enderchest.menu.EnderChestSelectMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();

        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
                event.setCancelled(true);
                new EnderChestSelectMenu().getRyseInventory().open(player);
            }
        }
    }
}
