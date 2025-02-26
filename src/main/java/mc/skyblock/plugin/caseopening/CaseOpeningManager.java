package mc.skyblock.plugin.caseopening;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.caseopening.configuration.CaseConfiguration;
import mc.skyblock.plugin.caseopening.model.CaseItem;
import mc.skyblock.plugin.util.Rarity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CaseOpeningManager {

    CaseConfiguration caseConfiguration;
    ItemStack caseKeyItem;
    List<CaseItem> caseItems;

    public CaseOpeningManager(CaseConfiguration caseConfiguration) {
        this.caseConfiguration = caseConfiguration;
        caseKeyItem = caseConfiguration.getCaseKeyItem();
        caseItems = new ArrayList<>(caseConfiguration.getCaseItems());
    }

    //TODO: Implement case opening logic
    //TODO: Implement animation for case opening (e.g. spinning items, particles, block breaking, sounds, special effects, etc.)

    public CaseItem getCaseItem(ItemStack itemStack) {
        for (CaseItem caseItem : caseItems) {
            if (caseItem.getItemStack().isSimilar(itemStack)) {
                return caseItem;
            }
        }
        return null;
    }

    public CaseItem getRandomCaseItem() {
        return caseItems.get((int) (Math.random() * caseItems.size()));
    }

    public CaseItem getRandomCaseItem(Rarity... excludedRarities) {
        List<CaseItem> filteredItems = new ArrayList<>(caseItems);
        for (Rarity rarity : excludedRarities) {
            filteredItems.removeIf(caseItem -> caseItem.getRarity() == rarity);
        }
        return filteredItems.get((int) (Math.random() * filteredItems.size()));
    }

}
