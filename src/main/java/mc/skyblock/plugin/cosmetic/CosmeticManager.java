package mc.skyblock.plugin.cosmetic;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.cosmetic.model.Cosmetic;
import mc.skyblock.plugin.user.model.SkyBlockUser;
import mc.skyblock.plugin.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CosmeticManager implements Listener {

    public CosmeticManager() {
        Bukkit.getPluginManager().registerEvents(this, SkyBlockPlugin.instance());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (Objects.equals(event.getClickedInventory(), player.getInventory())) {
                if(Cosmetics.isCosmeticItem(event.getCurrentItem())) {
                    System.out.println("Cosmetic Item clicked!");

                    if (this.hasSelectedCosmetics(player)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    public boolean hasSelectedCosmetics(Player player) {
        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());
        List<Cosmetic> selectedCosmetics = skyBlockUser.getSelectedCosmetic();

        return !selectedCosmetics.isEmpty();
    }

    public void updateCosmetics(Player player) {
        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());
        Map<Cosmetic, Boolean> cosmetics = skyBlockUser.getCosmetics();
        List<Cosmetic> selectedCosmetics = skyBlockUser.getSelectedCosmetic();

        if (cosmetics.isEmpty()) return;
        if (selectedCosmetics.isEmpty()) return;

        for (Cosmetic selectedCosmetic : selectedCosmetics) {
            switch (selectedCosmetic.getType()) {

                case HEAD -> {

                    ItemStack currentHelmet = player.getInventory().getHelmet();

                    if (currentHelmet != null) {
                        player.getInventory().addItem(currentHelmet);
                    }
                    player.getInventory().setHelmet(ItemBuilder.of(Material.PAPER)
                            .displayName(selectedCosmetic.getName())
                            .customModelData(selectedCosmetic.getCustomModelData()).build());
                }

                case HAND -> {

                    ItemStack currentOffhand = player.getInventory().getItemInOffHand();

                    if (currentOffhand.getType() != Material.AIR) {
                        player.getInventory().addItem(currentOffhand);
                    }

                    player.getInventory().setItemInOffHand(ItemBuilder.of(Material.PAPER)
                            .displayName(selectedCosmetic.getName())
                            .customModelData(selectedCosmetic.getCustomModelData()).build());
                }
            }
        }
    }
}
