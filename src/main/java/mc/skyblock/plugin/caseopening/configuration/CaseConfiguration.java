package mc.skyblock.plugin.caseopening.configuration;

import com.google.gson.reflect.TypeToken;
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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CaseConfiguration extends Configuration {

    @ConfigPath("case.block.material")
    Material caseBlockMaterial = Material.DROPPER;

    @ConfigPath("case.key")
    ItemStack caseKeyItem = ItemBuilder.of(Material.NAME_TAG).displayName(MiniMessage.miniMessage().deserialize("<rainbow>Case Key")).build();

    @ConfigPath("case.items")
    List<CaseItem> caseItems = List.of(
            new CaseItem(
                    ItemBuilder.of(Material.DIAMOND).displayName(MiniMessage.miniMessage().deserialize("<gradient:aqua:blue>Diamond")).build(),
                    30.0
            ),
            new CaseItem(
                    ItemBuilder.of(Material.EMERALD).displayName(MiniMessage.miniMessage().deserialize("<gradient:green:dark_green>Emerald")).build(),
                    15.0
            ),
            new CaseItem(
                    ItemBuilder.of(Material.GOLD_INGOT).displayName(MiniMessage.miniMessage().deserialize("<gradient:yellow:gold>Gold Ingot")).build(),
                    10.0
            ),
            new CaseItem(
                    ItemBuilder.of(Material.IRON_INGOT).displayName(MiniMessage.miniMessage().deserialize("<gradient:white:gray>Iron Ingot")).build(),
                    5.0
            ),
            new CaseItem(
                    ItemBuilder.of(Material.COAL).displayName(MiniMessage.miniMessage().deserialize("<gradient:black:gray>Coal")).build(),
                    0.9
            ),
            new CaseItem(
                    ItemBuilder.of(Material.NETHERITE_HELMET).displayName(MiniMessage.miniMessage().deserialize("<gradient:#ffffff:#000000>nigga helmet")).build(),
                    0.1
            )
    );

    public List<CaseItem> getCaseItems() {
        Type listType = new TypeToken<List<CaseItem>>() {}.getType();
        return GSON.fromJson(GSON.toJson(caseItems), listType);
    }

    public void addCaseItem(@NotNull ItemStack item, double chance) {
        caseItems.add(new CaseItem(item.clone(), chance));
    }

    public void removeCaseItem(@NotNull ItemStack itemStack) {
        caseItems.removeIf(caseItem -> caseItem.getItemStack().isSimilar(itemStack));
    }
}
