package mc.skyblock.plugin.codec;

import mc.skyblock.plugin.shop.model.Shop;
import mc.skyblock.plugin.shop.model.currency.ShopCurrencyFormat;
import mc.skyblock.plugin.shop.model.item.ShopItem;
import mc.skyblock.plugin.util.ItemStackConverter;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.ArrayList;
import java.util.List;

public class ShopCodec implements Codec<Shop> {

    @Override
    public Shop decode(BsonReader reader, DecoderContext context) {
        Shop shop = new Shop();
        reader.readStartDocument();
        shop.setName(reader.readString("name"));
        shop.setNpcId(reader.readString("npcId"));
        shop.setCurrencyFormat(ShopCurrencyFormat.valueOf(reader.readString("currencyFormat")));
        shop.setDiscountPercentage(reader.readDouble("discountPercentage"));
        shop.setItems(readItems(reader));
        reader.readEndDocument();
        return shop;
    }

    private List<ShopItem> readItems(BsonReader reader) {
        List<ShopItem> items = new ArrayList<>();
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            items.add(readItem(reader));
        }
        reader.readEndArray();
        return items;
    }

    private ShopItem readItem(BsonReader reader) {
        ShopItem item = new ShopItem();
        reader.readStartDocument();
        item.setId(reader.readInt32("id"));
        item.setItemStack(ItemStackConverter.decode(reader.readString("itemStack")));
        item.setPrice(reader.readDouble("price"));
        item.setAmount(reader.readInt32("amount"));
        item.setDiscount(reader.readBoolean("discount"));
        item.setDiscountPercentage(reader.readDouble("discountPercentage"));
        reader.readEndDocument();
        return item;
    }

    @Override
    public void encode(BsonWriter writer, Shop shop, EncoderContext context) {
        writer.writeStartDocument();
        writer.writeString("name", shop.getName());
        writer.writeString("npcId", shop.getNpcId());
        writer.writeString("currencyFormat", shop.getCurrencyFormat().name());
        writer.writeBoolean("discount", shop.isDiscount());
        writer.writeDouble("discountPercentage", shop.getDiscountPercentage());
        writeItems(writer, shop.getItems());
        writer.writeEndDocument();
    }

    private void writeItems(BsonWriter writer, List<ShopItem> items) {
        writer.writeStartArray("items");
        for (ShopItem item : items) {
            writeItem(writer, item);
        }
        writer.writeEndArray();
    }

    private void writeItem(BsonWriter writer, ShopItem item) {
        writer.writeStartDocument();
        writer.writeInt32("id", item.getId());
        writer.writeString("itemStack", ItemStackConverter.encode(item.getItemStack()));
        writer.writeDouble("price", item.getPrice());
        writer.writeInt32("amount", item.getAmount());
        writer.writeBoolean("discount", item.isDiscount());
        writer.writeDouble("discountPercentage", item.getDiscountPercentage());
        writer.writeEndDocument();
    }

    @Override
    public Class<Shop> getEncoderClass() {
        return Shop.class;
    }
}