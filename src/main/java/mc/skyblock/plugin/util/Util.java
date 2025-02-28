package mc.skyblock.plugin.util;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import mc.skyblock.plugin.util.custom.CustomSounds;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class Util {

    public void defaultInventory(InventoryContents contents) {
        ItemBuilder placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                .displayName("§7-/-");
        contents.fill(placeholder.build());
    }

    public void borderInventory(InventoryContents contents) {
        ItemBuilder placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                .displayName("§7-/-");
        contents.fillBorders(placeholder.build());
    }

    public ItemStack placeholderItem() {
        ItemBuilder placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                .displayName("§7-/-");
        return placeholder.build();
    }

    public ItemBuilder placeholderItemEdit() {
        ItemBuilder placeholder = ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE)
                .displayName("§7-/-");
        return placeholder;
    }

    public @NotNull IntelligentItem nextButton(@NonNull Pagination pagination) {

        Material material;

        if (pagination.isLast()) {
            material = Material.GRAY_STAINED_GLASS_PANE;
        } else {
            material = Material.GREEN_STAINED_GLASS_PANE;
        }

        return IntelligentItem.of(ItemBuilder.of(material).displayName("§aNächste Seite »")
                .lore(
                        Component.empty(),
                        Component.text("§7<Linksklicke zum vor gehen>")
                )
                .build(), event -> {
            if (!(event.getWhoClicked() instanceof Player player)) {
                return;
            }

            if (pagination.isLast()) {
                player.sendMessage(ChatAction.failure("Du befindest dich bereits auf der letzten Seite"));
                player.playSound(player.getLocation(), Sound.BLOCK_LANTERN_STEP, 100, 1);
                return;
            }

            RyseInventory currentInventory = pagination.inventory();
            currentInventory.open(player, pagination.next().page());
            CustomSounds.NOTIFICATION.playSound(player, 100, 2, player.getLocation());
        });
    }

    public @NotNull IntelligentItem backButton(@NonNull Pagination pagination) {
        Material material;

        if (pagination.isFirst()) {
            material = Material.GRAY_STAINED_GLASS_PANE;
        } else {
            material = Material.RED_STAINED_GLASS_PANE;
        }

        return IntelligentItem.of(ItemBuilder.of(material)
                .displayName("§c« Zurück")
                .lore(
                        Component.empty(),
                        Component.text("§7<Linksklicke zum zurück gehen>")
                ).build(), event -> {

            if (!(event.getWhoClicked() instanceof Player player)) {
                return;
            }

            if (pagination.isFirst()) {
                player.sendMessage(ChatAction.failure("Du befindest dich bereits auf der ersten Seite"));
                player.playSound(player.getLocation(), Sound.BLOCK_LANTERN_STEP, 100, 1);
                return;
            }

            RyseInventory currentInventory = pagination.inventory();
            currentInventory.open(player, pagination.previous().page());
            CustomSounds.NOTIFICATION.playSound(player, 100, 2, player.getLocation());
        });
    }

    public @NotNull IntelligentItem exitButton(@NonNull RyseInventory inventory) {
        return IntelligentItem.of(ItemBuilder.of(Material.BARRIER)
                .displayName("§cZurück")
                .lore("§7Klicke, um dieses Menü zu schließen.").build(), event -> inventory.close((Player) event.getWhoClicked()));
    }
}
