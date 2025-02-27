package mc.skyblock.plugin.caseopening;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import mc.skyblock.plugin.caseopening.animation.CaseOpeningAnimation;
import mc.skyblock.plugin.caseopening.configuration.CaseConfiguration;
import mc.skyblock.plugin.caseopening.model.CaseItem;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.Rarity;
import mc.skyblock.plugin.util.SoundAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CaseOpeningManager {

    CaseConfiguration caseConfiguration;
    ItemStack caseKeyItem;
    List<CaseItem> caseItems;

    @NonFinal
    boolean opening;

    public CaseOpeningManager(CaseConfiguration caseConfiguration) {
        this.caseConfiguration = caseConfiguration;
        caseKeyItem = caseConfiguration.getCaseKeyItem();
        caseItems = new ArrayList<>(caseConfiguration.getCaseItems());
    }

    public void open(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (!caseKeyItem.isSimilar(itemInHand)) {
            player.sendMessage(ChatAction.failure("Du benötigst einen Schlüssel, um die Kiste zu öffnen."));
            SoundAction.playTaskFailed(player);
            return;
        }
        if (opening) {
            player.sendMessage(ChatAction.failure("Es wird bereits eine Kiste geöffnet."));
            SoundAction.playTaskFailed(player);
            return;
        }
        if (itemInHand.getAmount() > 1) {
            itemInHand.setAmount(itemInHand.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(ItemStack.empty());
        }
        opening = true;
        new CaseOpeningAnimation(player).start(() -> opening = false);
    }

    public CaseItem getCaseItem(ItemStack itemStack) {
        for (CaseItem caseItem : caseItems) {
            if (caseItem.getItemStack().isSimilar(itemStack)) {
                return caseItem;
            }
        }
        return null;
    }

    public CaseItem getRandomCaseItem() {
        return new ArrayList<>(caseItems).get((int) (Math.random() * caseItems.size()));
    }

    public CaseItem getRandomCaseItem(Rarity... excludedRarities) {
        List<CaseItem> filteredItems = new ArrayList<>(caseItems);
        for (Rarity rarity : excludedRarities) {
            filteredItems.removeIf(caseItem -> caseItem.getRarity() == rarity);
        }
        return filteredItems.get((int) (Math.random() * filteredItems.size()));
    }

}
