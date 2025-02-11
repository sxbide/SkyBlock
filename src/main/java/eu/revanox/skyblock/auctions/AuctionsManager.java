package eu.revanox.skyblock.auctions;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.auctions.model.AuctionItem;
import eu.revanox.skyblock.auctions.repository.AuctionsRepository;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.NumberUtil;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class AuctionsManager {

    @Getter
    Map<Integer, AuctionItem> auctionItems;
    AuctionsRepository repository;

    public AuctionsManager() {
        this.repository = SkyBlockPlugin.instance().getMongoManager().create(AuctionsRepository.class);

        this.auctionItems = new HashMap<>();

        for (AuctionItem auctionItem : this.repository.findAll()) {
            this.auctionItems.put(auctionItem.getId(), auctionItem);
        }
    }

    public void createAuction(UUID seller, ItemStack itemStack, double price) {
        int auctionId = NumberUtil.randomInt(100000, 999999);
        AuctionItem auctionItem = new AuctionItem();
        auctionItem.setId(auctionId);
        auctionItem.setPrice(price);
        auctionItem.setItemStack(itemStack.clone());
        auctionItem.setSellerUniqueId(seller);

        this.auctionItems.put(auctionId, auctionItem);
        this.repository.save(auctionItem);
    }

    public void deleteAuction(int auctionId) {
        AuctionItem auctionItem = this.auctionItems.get(auctionId);

        if(auctionItem != null) {
            this.auctionItems.remove(auctionId);
            this.repository.delete(auctionItem);
        }
    }

    public List<AuctionItem> getAuctionsByPlayer(UUID uuid) {
        List<AuctionItem> auctionsItems = new ArrayList<>();
        List<Map.Entry<Integer, AuctionItem>> auctionItemsMap = this.auctionItems.entrySet().stream().filter(integerAuctionItemEntry -> integerAuctionItemEntry.getValue().getSellerUniqueId().equals(uuid)).toList();

        for (Map.Entry<Integer, AuctionItem> integerAuctionItemEntry : auctionItemsMap) {
            auctionsItems.add(integerAuctionItemEntry.getValue());
        }
        return auctionsItems;
    }

    public boolean isItemAvailable(int auctionId) {
        return this.auctionItems.get(auctionId) != null;
    }
}
