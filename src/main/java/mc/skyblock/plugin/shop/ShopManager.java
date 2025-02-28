package mc.skyblock.plugin.shop;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.shop.model.Shop;
import mc.skyblock.plugin.shop.repository.ShopRepository;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShopManager {

    ShopRepository shopRepository;
    Map<String, Shop> shopMap;

    public ShopManager() {
        this.shopRepository = SkyBlockPlugin.instance().getMongoManager().create(ShopRepository.class);
        this.shopMap = shopRepository.findAll().stream().collect(Collectors.toMap(Shop::getName, Function.identity()));
    }

    public Shop createShop(String name) {
        Shop shop = new Shop();
        shop.setName(name);
        shop.setNpcId(0);
        shop.setItems(new ArrayList<>());
        shop.setDiscount(false);
        shop.setDiscountPercentage(0);
        shopMap.put(name, shop);
        shopRepository.save(shop);
        return shop;
    }

    public void editShop(String name, Consumer<Shop> consumer) {
        Shop shop = shopMap.get(name);
        consumer.accept(shop);
        shopMap.put(name, shop);
        shopRepository.save(shop);
    }

    public void deleteShop(String name) {
        shopMap.remove(name);
        shopRepository.deleteById(name);
    }

    public Shop getShop(String name) {
        return shopMap.get(name);
    }

    public void saveAll() {
        shopRepository.saveAll(shopMap.values());
    }
}
