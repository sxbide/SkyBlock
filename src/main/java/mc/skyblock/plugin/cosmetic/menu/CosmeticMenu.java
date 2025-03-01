package mc.skyblock.plugin.cosmetic.menu;

import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import lombok.Getter;
import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.cosmetic.Cosmetics;
import mc.skyblock.plugin.user.model.SkyBlockUser;
import mc.skyblock.plugin.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.stream.IntStream;

@Getter
public class CosmeticMenu implements InventoryProvider {

    private RyseInventory ryseInventory;

    public CosmeticMenu() {

        this.ryseInventory = RyseInventory.builder()
                .title("Kosmetische Gegenstände")
                .rows(6)
                .provider(this)
                .build(SkyBlockPlugin.instance());
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        Util.borderInventory(contents);

        for (int i = 37; i < 44; i++) {
            contents.set(i, Util.placeholderItem());
        }

        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(21);
        pagination.iterator(SlotIterator.builder()
                .startPosition(10)
                .blackList(17, 18, 26, 27)
                .override()
                .type(SlotIterator.SlotIteratorType.HORIZONTAL)
                .build());

        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());

        for (Cosmetics cosmetic : Cosmetics.values()) {
            ItemBuilder cosmeticItem = ItemBuilder.of(Material.PAPER)
                    .customModelData(cosmetic.getCosmetic().getCustomModelData())
                    .displayName(Component.text(cosmetic.getCosmetic().getName()))
                    .lore(
                            Component.empty(),
                            Component.text("§7Seltenheit: ").append(cosmetic.getCosmetic().getRarity().getDisplayName()),
                            Component.text("§7Kosmetik Typ: ").append((cosmetic.getCosmetic().isHoldable() ? Component.text("§eHand") : Component.text("§eAndere")))
                    );

            if (skyBlockUser.hasCosmeticSelected(cosmetic)) {
                cosmeticItem.enchantment(Enchantment.SHARPNESS, 1);
            }
            cosmeticItem.itemFlags(ItemFlag.HIDE_ENCHANTS);

            if (!skyBlockUser.hasCosmetic(cosmetic)) {
                cosmeticItem.getLore().add(Component.text("§7Kosten: §e" + NumberUtil.formatBalance(cosmetic.getCosmetic().getPrice()) + " ⛃"));
            }

            cosmeticItem.getLore().add(Component.empty());
            cosmeticItem.getLore().add(Component.text("§7<Linksklicke zum kaufen>"));

            pagination.addItem(IntelligentItem.of(cosmeticItem.build(), event -> {

                if (skyBlockUser.hasCosmetic(cosmetic)) {

                    if(skyBlockUser.hasCosmeticWithTypeSelected(cosmetic.getCosmetic().getType())) {
                        skyBlockUser.toggleSelectedCosmetic(skyBlockUser.getSelectedCosmetic().stream().filter(cosmetics -> cosmetics.getCosmetic().getType().equals(cosmetic.getCosmetic().getType()))
                                .findAny().get());
                    }

                    skyBlockUser.toggleSelectedCosmetic(cosmetic);
                    SkyBlockPlugin.instance().getCosmeticManager().updateCosmetics(player);
                    player.sendMessage(ChatAction.of("§aDu hast den Kosmetik Gegenstand erfolgreich ausgewählt."));
                    player.closeInventory();
                } else {

                    if (skyBlockUser.getBalance() < cosmetic.getCosmetic().getPrice()) {
                        player.sendMessage(ChatAction.failure("§cDazu ist dein Kontostand zu niedrig."));
                        return;
                    }

                    skyBlockUser.removeBalance(cosmetic.getCosmetic().getPrice());
                    skyBlockUser.addCosmetic(cosmetic);
                    SoundAction.playTaskComplete(player);
                    player.sendMessage(ChatAction.of("§aDu hast den Kosmetik Gegenstand erfolgreich erworben."));
                    player.closeInventory();
                }
            }));

        }

        contents.set(53, Util.nextButton(pagination));
        contents.set(45, Util.backButton(pagination));

        contents.set(49, IntelligentItem.of(ItemBuilder.of(Material.BARRIER)
                        .displayName(MiniMessage.miniMessage().deserialize("<#ff0000>Kosmetike Gegenstände entfernen"))
                        .lore(
                                Component.empty(),
                                Component.text("§7Hiermit entfernst du all deine ausgewählten Kosmetika."),
                                Component.empty(),
                                Component.text("§7<Linksklicke zum entfernen>")
                        ).build(), event -> {
                    skyBlockUser.clearSelectedCosmetics();
                    player.sendMessage(ChatAction.of("§aDu hast all deine Kosmetika erfolgreich entfernt."));
                    SkyBlockPlugin.instance().getCosmeticManager().updateCosmetics(player);
                    player.closeInventory();
                }
        ));
    }
}
