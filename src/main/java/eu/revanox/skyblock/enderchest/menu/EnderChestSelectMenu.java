package eu.revanox.skyblock.enderchest.menu;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.ItemBuilder;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.InventoryOpenerType;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnderChestSelectMenu implements InventoryProvider {

    SkyBlockPlugin plugin;
    RyseInventory ryseInventory;

    @NonFinal
    SkyBlockUser skyBlockUser;

    public EnderChestSelectMenu() {
        this.plugin = SkyBlockPlugin.instance();
        this.ryseInventory = RyseInventory.builder().title("Enderchest: Auswahl").type(InventoryOpenerType.HOPPER).provider(this).disableUpdateTask().build(this.plugin);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        this.skyBlockUser = this.plugin.getUserManager().getUser(player.getUniqueId());
        LuckPerms luckPerms = LuckPermsProvider.get();

        contents.set(0, ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).displayName("§r").build());
        contents.set(4, ItemBuilder.of(Material.GRAY_STAINED_GLASS_PANE).displayName("§r").build());

        contents.set(1, IntelligentItem.of(ItemBuilder.of(Material.CHEST_MINECART).displayName("§7Enderkiste §8#§e1")
                        .lore(
                                Component.empty(),
                                Component.text("§7Klicke, um die Enderkiste §8#§e1 §7zu öffnen."),
                                Component.empty(),
                                Component.text("§7<Klicke zum Öffnen>")).build(),
                event -> {
                    player.closeInventory();
                    new EnderChestMenu(this.skyBlockUser, 1).open(player);
                }));

        //TODO: Change those boolean checks to a more efficient way, e.g. by using ranks instead of permissions
        boolean hasEnderchest2 = luckPerms.getUserManager().getUser(player.getUniqueId()).getCachedData().getPermissionData().checkPermission("skyblock.enderchest.2").asBoolean();
        boolean hasEnderchest3 = luckPerms.getUserManager().getUser(player.getUniqueId()).getCachedData().getPermissionData().checkPermission("skyblock.enderchest.3").asBoolean();

        if (hasEnderchest2) {
            contents.set(2, IntelligentItem.of(ItemBuilder.of(Material.CHEST_MINECART).displayName("§7Enderkiste §8#§e2")
                    .lore(
                            Component.empty(),
                            Component.text("§7Klicke, um die Enderkiste §8#§e2 §7zu öffnen."),
                            Component.empty(),
                            Component.text("§7<Linksklicke zum Öffnen>")).build(), event -> {
                player.closeInventory();
                new EnderChestMenu(this.skyBlockUser, 2).open(player);
            }));
        } else {
            contents.set(2, IntelligentItem.of(ItemBuilder.of(Material.MINECART).displayName("§7Enderkiste §8#§e2")
                    .lore(
                            Component.empty(),
                            Component.text("§cDu hast keine Berechtigung für diese Enderkiste.")).build(), event -> {
                player.sendMessage(ChatAction.failure("Du hast keine Berechtigung für diese Enderkiste."));
            }));
        }

        if (hasEnderchest3) {
            contents.set(3, IntelligentItem.of(ItemBuilder.of(Material.CHEST_MINECART).displayName("§7Enderkiste §8#§e3")
                    .lore(
                            Component.empty(),
                            Component.text("§7Klicke, um die Enderkiste §8#§e3 §7zu öffnen."),
                            Component.empty(),
                            Component.text("§7<Linksklicke zum Öffnen>")).build(), event -> {
                player.closeInventory();
                new EnderChestMenu(this.skyBlockUser, 3).open(player);
            }));
        } else {
            contents.set(3, IntelligentItem.of(ItemBuilder.of(Material.MINECART).displayName("§7Enderkiste §8#§e3")
                    .lore(
                            Component.empty(),
                            Component.text("§cDu hast keine Berechtigung für diese Enderkiste.")
                    ).build(), event -> {
                player.sendMessage(ChatAction.failure("Du hast keine Berechtigung für diese Enderkiste."));
            }));
        }
    }
}
