package mc.skyblock.plugin.caseopening.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.caseopening.model.CaseItem;
import mc.skyblock.plugin.configuration.Configuration;
import mc.skyblock.plugin.configuration.annotation.ConfigPath;
import mc.skyblock.plugin.util.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CaseConfiguration extends Configuration {

    @ConfigPath("case.key")
    ItemStack caseKeyItem = ItemBuilder.of(Material.NAME_TAG).displayName(MiniMessage.miniMessage().deserialize("<rainbow>Case Key")).build();

    @ConfigPath("case.items")
    List<CaseItem> caseItems = List.of(
            new CaseItem(
                    ItemBuilder.of(Material.DIAMOND).displayName(MiniMessage.miniMessage().deserialize("<gradient:aqua:blue>Diamond")).build(),
                    0.1
            ),
            new CaseItem(
                    ItemBuilder.of(Material.EMERALD).displayName(MiniMessage.miniMessage().deserialize("<gradient:green:dark_green>Emerald")).build(),
                    0.2
            ),
            new CaseItem(
                    ItemBuilder.of(Material.GOLD_INGOT).displayName(MiniMessage.miniMessage().deserialize("<gradient:yellow:orange>Gold Ingot")).build(),
                    99.7
            )
    );

}
