package mc.skyblock.plugin.caseopening.mongo.model;

import eu.koboo.en2do.repository.entity.Id;
import eu.koboo.en2do.repository.entity.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.caseopening.mongo.model.item.CaseItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Case {

    @Id
    private int version = 1;

    Material caseBlockMaterial;

    ItemStack keyItem;

    List<CaseItem> items;

    @Transient
    public void addCaseItem(@NotNull ItemStack item, double chance) {
        items.add(new CaseItem(item, chance));
    }

    @Transient
    public void removeCaseItem(@NotNull ItemStack item) {
        items.removeIf(caseItem -> caseItem.getItemStack().equals(item));
    }

}
