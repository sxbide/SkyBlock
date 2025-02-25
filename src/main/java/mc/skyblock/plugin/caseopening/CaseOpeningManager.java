package mc.skyblock.plugin.caseopening;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.caseopening.configuration.CaseConfiguration;
import mc.skyblock.plugin.caseopening.model.CaseItem;
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

}
