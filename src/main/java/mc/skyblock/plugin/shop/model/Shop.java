package mc.skyblock.plugin.shop.model;

import eu.koboo.en2do.repository.entity.Id;
import eu.koboo.en2do.repository.entity.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import mc.skyblock.plugin.shop.model.item.ShopItem;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Shop {

    @Id
    String name;
    int npcId;

    List<ShopItem> items;

    boolean discount;
    double discountPercentage;

    @Transient
    public ShopItem getShopItem(int id) {
        return items.stream().filter(shopItem -> shopItem.getId() == id).findFirst().orElse(null);
    }

    @Transient
    public void getShopItem(ItemStack similarItem) {
        items.stream().filter(shopItem -> shopItem.getItemStack().isSimilar(similarItem)).findFirst().orElse(null);
    }

}
