package eu.revanox.skyblock.auctions.repository;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import eu.revanox.skyblock.auctions.model.AuctionItem;

@Collection("auctions")
public interface AuctionsRepository extends Repository<AuctionItem, Integer> {

}
