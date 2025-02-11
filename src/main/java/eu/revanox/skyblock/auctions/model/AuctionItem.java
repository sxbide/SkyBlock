package eu.revanox.skyblock.auctions.model;

import eu.koboo.en2do.repository.entity.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionItem {

    @Id
    int id;

    double price;

    ItemStack itemStack;
    UUID sellerUniqueId;
}
