package eu.revanox.skyblock.perks.menu;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.perks.Perks;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.*;
import io.github.rysefoxx.inventory.plugin.content.IntelligentItem;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.enums.Action;
import io.github.rysefoxx.inventory.plugin.enums.DisabledInventoryClick;
import io.github.rysefoxx.inventory.plugin.pagination.Pagination;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import io.github.rysefoxx.inventory.plugin.pagination.SlotIterator;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter
public class PerkMenu implements InventoryProvider {

    RyseInventory ryseInventory;

    public PerkMenu() {

        this.ryseInventory = RyseInventory.builder()
                .title("Vorteile")
                .rows(5)
                .provider(this)
                .build(SkyBlockPlugin.instance());
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Util.defaultInventory(contents);

        Pagination pagination = contents.pagination();
        pagination.setItemsPerPage(14);
        pagination.iterator(SlotIterator.builder()
                .startPosition(10)
                .blackList(17, 18)
                .override()
                .type(SlotIterator.SlotIteratorType.HORIZONTAL)
                .build());

        for (Perks perk : Perks.values()) {

            ItemBuilder perkItemStack = ItemBuilder.of(perk.getItemStack().clone());
            perkItemStack.getLore().add(Component.empty());
            perkItemStack.getLore().add(Component.text("§7Kosten: §e" + NumberUtil.formatBalance(perk.getPrice()) + " ⛃"));

            pagination.addItem(IntelligentItem.of(perkItemStack.build(), event -> {

                SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());

                if (skyBlockUser.getBalance() < perk.getPrice()) {
                    player.sendMessage(ChatAction.failure("§cDazu ist dein Kontostand zu niedrig."));
                    return;
                }

                skyBlockUser.removeBalance(perk.getPrice());
                ItemBuilder perkBoughtItemStack = ItemBuilder.of(perk.getItemStack().clone());
                perkBoughtItemStack.getLore().add(Component.empty());
                perkBoughtItemStack.getLore().add(Component.text("§7Gekauft von §e" + player.getName()));
                player.getInventory().addItem(perkBoughtItemStack.enchantment(Enchantment.EFFICIENCY, 10).build());
                SoundAction.playTaskComplete(player);
                player.sendMessage(ChatAction.of("§aDu hast den Vorteil §f§o" + perk.getDisplayName() + " §aerfolgreich gekauft."));
            }));
        }

        contents.set(44, Util.nextButton(pagination));
        contents.set(36, Util.backButton(pagination));
    }
}
