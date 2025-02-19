package eu.revanox.skyblock.tag.menu;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.tag.model.Tags;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.*;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

@Getter
public class TagMenu implements InventoryProvider {

    RyseInventory ryseInventory;

    public TagMenu() {

        this.ryseInventory = RyseInventory.builder()
                .title("Wähle deinen Titel aus")
                .rows(6)
                .provider(this)
                .disableUpdateTask()
                .build(SkyBlockPlugin.instance());
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        Util.borderInventory(contents);

        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(10);
        pagination.iterator(SlotIterator.builder()
                .startPosition(20)
                .blackList(25, 26, 27, 28)
                .override()
                .type(SlotIterator.SlotIteratorType.HORIZONTAL)
                .build());

        SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());

        for (Tags tag : Tags.sortByRarity()) {
            ItemBuilder itemBuilder = ItemBuilder.of(Material.NAME_TAG)
                    .displayName(tag.getTagText().append(Component.text(" §8› §7Titel")));


            if (skyBlockUser.getSelectedTag() != null && skyBlockUser.getSelectedTag().equals(tag)) {
                itemBuilder.enchantment(Enchantment.SHARPNESS, 1);
            }
            itemBuilder.itemFlags(ItemFlag.HIDE_ENCHANTS);

            itemBuilder.getLore().add(Component.empty());
            itemBuilder.getLore().add(Component.text("§7Seltenheit: ").append(tag.getRarity().getDisplayName()));


            if (skyBlockUser.hasTag(tag)) {
                itemBuilder.getLore().add(Component.empty());

                if (skyBlockUser.getSelectedTag() != null && skyBlockUser.getSelectedTag().equals(tag)) {
                    itemBuilder.getLore().add(Component.text("§aAktuell ausgewählter Titel"));
                    itemBuilder.getLore().add(Component.text("§7<Linksklicke zum nicht mehr auswählen>"));
                } else {
                    itemBuilder.getLore().add(Component.text("§7<Linksklicke zum auswählen>"));
                }

            } else if (tag.getPrice() == -1) {
                itemBuilder.getLore().add(Component.empty());
                itemBuilder.getLore().add(Component.text("§cDieser Titel kann nicht gekauft werden."));
            } else {
                itemBuilder.getLore().add(Component.text("§7Kosten: §e" + NumberUtil.formatBalance(tag.getPrice()) + " ⛃"));
                itemBuilder.getLore().add(Component.empty());
                itemBuilder.getLore().add(Component.text("§7<Linksklicke zum kaufen>"));
            }

            pagination.addItem(IntelligentItem.of(itemBuilder
                    .build(), event -> {

                if (tag.getPrice() == -1 && !skyBlockUser.hasTag(tag)) {
                    player.sendMessage(ChatAction.failure("§cDieser Titel kann nicht gekauft werden."));
                    return;
                }

                if (!skyBlockUser.hasTag(tag)) {

                    if (skyBlockUser.getBalance() < tag.getPrice()) {
                        player.sendMessage(ChatAction.failure("§cDazu ist dein Kontostand zu niedrig."));
                        return;
                    }

                    skyBlockUser.removeBalance(tag.getPrice());
                    skyBlockUser.addTag(tag);
                    SoundAction.playTaskComplete(player);
                    player.sendMessage(ChatAction.of("§aDu hast den Titel erfolgreich gekauft."));
                    player.closeInventory();

                } else {

                    if (skyBlockUser.getSelectedTag() != null && skyBlockUser.getSelectedTag().equals(tag)) {
                        skyBlockUser.setSelectedTag(null);
                        itemBuilder.getEnchantments().clear();
                        player.sendMessage(ChatAction.failure("§cDu hast nun keinen Titel mehr ausgewählt."));
                    } else {
                        skyBlockUser.setSelectedTag(tag);
                        itemBuilder.enchantment(Enchantment.SHARPNESS, 1);
                        player.sendMessage(ChatAction.of("§aDu hast den Titel erfolgreich ausgewählt."));
                    }
                    SkyBlockPlugin.instance().getTagManager().updateTag(player);
                    player.closeInventory();
                }
            }));
        }

        contents.set(53, Util.nextButton(pagination));

        contents.set(48, IntelligentItem.empty(ItemBuilder.of(Material.KNOWLEDGE_BOOK)
                .displayName(MiniMessage.miniMessage().deserialize("<#00ff00>Information"))
                .lore(
                        Component.empty(),
                        Component.text("§7Hier kannst du dir deinen Titel aussuchen."),
                        Component.text("§7Dieser wird dann über deinem Namen angezeigt."),
                        Component.empty(),
                        Component.text("§7Du hast §e" + skyBlockUser.getTags().values().stream().filter(tag -> tag).count() + " §7von §e" + Tags.values().length + " §7Titel freigeschaltet."),
                        Component.text("§7Du kannst nur einen Titel gleichzeitig auswählen.")
                )
                .build()
        ));

        contents.set(50, IntelligentItem.of(ItemBuilder.of(Material.BARRIER)
                        .displayName(MiniMessage.miniMessage().deserialize("<#ff0000>Titel entfernen"))
                        .lore(
                                Component.empty(),
                                Component.text("§7Hiermit entfernst du deinen ausgewählten Titel."),
                                Component.empty(),
                                Component.text("§7<Linksklicke zum entfernen>")
                        ).build(), event -> {
                    skyBlockUser.setSelectedTag(null);
                    player.sendMessage(ChatAction.of("§aDu hast deinen Titel erfolgreich entfernt."));
                    SkyBlockPlugin.instance().getTagManager().updateTag(player);
                    player.closeInventory();
                }
        ));

        contents.set(45, Util.backButton(pagination));
    }
}
