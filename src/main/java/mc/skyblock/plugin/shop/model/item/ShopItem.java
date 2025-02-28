package mc.skyblock.plugin.shop.model.item;

import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ShopItem {

    @Id
    Integer id; //Starts at 0, increments by 1 for each new item

    ItemStack itemStack;

    double price;
    int amount;

    boolean discount;
    double discountPercentage;

}
