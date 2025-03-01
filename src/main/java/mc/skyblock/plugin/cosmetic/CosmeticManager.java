package mc.skyblock.plugin.cosmetic;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.cosmetic.model.Cosmetic;
import mc.skyblock.plugin.cosmetic.model.CosmeticType;
import mc.skyblock.plugin.user.model.SkyBlockUser;
import mc.skyblock.plugin.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import javax.lang.model.element.ElementVisitor;
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
                if (Cosmetics.isCosmeticItem(event.getCurrentItem())) {
                    System.out.println("Cosmetic Item clicked!");
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        if(Cosmetics.isCosmeticItem(event.getMainHandItem())) {
            event.setCancelled(true);
        }
    }

    public boolean hasSelectedCosmetics(Player player) {
        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());
        List<Cosmetics> selectedCosmetics = skyBlockUser.getSelectedCosmetic();

        return !selectedCosmetics.isEmpty();
    }

    public void updateCosmetics(Player player) {
        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());
        Map<Cosmetics, Boolean> cosmetics = skyBlockUser.getCosmetics();
        List<Cosmetics> selectedCosmetics = skyBlockUser.getSelectedCosmetic();

        ItemStack currentHelmet = player.getInventory().getHelmet();
        ItemStack currentOffhand = player.getInventory().getItemInOffHand();

        if (currentHelmet != null && Cosmetics.isCosmeticItem(currentHelmet)) {
            player.getInventory().setHelmet(null);
        }
        if (Cosmetics.isCosmeticItem(currentOffhand)) {
            player.getInventory().setItemInOffHand(null);
        }

        for (Cosmetics selectedCosmetic : selectedCosmetics) {
            if (!cosmetics.getOrDefault(selectedCosmetic, false)) {
                continue;
            }

            switch (selectedCosmetic.getCosmetic().getType()) {
                case HEAD -> {
                    if (currentHelmet != null && !Cosmetics.isCosmeticItem(currentHelmet)) {
                        player.getInventory().addItem(currentHelmet);
                    }
                    player.getInventory().setHelmet(ItemBuilder.of(Material.PAPER)
                            .displayName(selectedCosmetic.getCosmetic().getName())
                            .customModelData(selectedCosmetic.getCosmetic().getCustomModelData())
                            .build());
                }

                case HAND -> {
                    if (currentOffhand.getType() != Material.AIR && !Cosmetics.isCosmeticItem(currentOffhand)) {
                        player.getInventory().addItem(currentOffhand);
                    }
                    player.getInventory().setItemInOffHand(ItemBuilder.of(Material.PAPER)
                            .displayName(selectedCosmetic.getCosmetic().getName())
                            .customModelData(selectedCosmetic.getCosmetic().getCustomModelData())
                            .build());
                }
            }
        }
    }

}
