package mc.skyblock.plugin.shop.repository;

import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;
import mc.skyblock.plugin.shop.model.Shop;

@Collection("shops")
public interface ShopRepository extends Repository<Shop, String> {
}
