package mc.skyblock.plugin.caseopening.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.util.Rarity;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CaseItem {

    ItemStack itemStack;
    double chance;

    public Rarity getRarity() {
        double maxExclusiveChance = 0.2;
        if (chance < maxExclusiveChance) {
            return Rarity.EXCLUSIVE;
        }
        double maxLegendaryChance = 5.0;
        if (chance < maxLegendaryChance) {
            return Rarity.LEGENDARY;
        }
        double maxMythicChance = 10.0;
        if (chance < maxMythicChance) {
            return Rarity.MYTHIC;
        }
        double maxEpicChance = 15.0;
        if (chance < maxEpicChance) {
            return Rarity.EPIC;
        }
        double maxRareChance = 20.0;
        if (chance < maxRareChance) {
            return Rarity.RARE;
        }
        double maxUncommonChance = 30.0;
        if (chance < maxUncommonChance) {
            return Rarity.UNCOMMON;
        }
        return Rarity.COMMON;
    }


}
