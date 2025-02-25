package mc.skyblock.plugin.caseopening.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.caseopening.model.CaseItem;
import mc.skyblock.plugin.configuration.Configuration;
import mc.skyblock.plugin.configuration.annotation.ConfigPath;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CaseConfiguration extends Configuration {

    @ConfigPath("case.key.item")
    ItemStack caseKeyItem;

    @ConfigPath("case.items")
    List<CaseItem> caseItems;

}
