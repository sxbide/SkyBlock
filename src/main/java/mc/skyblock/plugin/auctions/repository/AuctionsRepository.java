package mc.skyblock.plugin.auctions.repository;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.auctions.model.AuctionItem;

@Collection("auctions")
public interface AuctionsRepository extends Repository<AuctionItem, Integer> {

}
