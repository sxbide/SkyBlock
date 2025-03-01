package mc.skyblock.plugin.codec;

import mc.skyblock.plugin.cosmetic.model.Cosmetic;
import mc.skyblock.plugin.cosmetic.model.CosmeticType;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class CosmeticCodec implements Codec<Cosmetic> {

    @Override
    public Cosmetic decode(BsonReader bsonReader, DecoderContext decoderContext) {
        String name;
        int customModelData;
        CosmeticType type;
        boolean holdable;
        bsonReader.readStartDocument();
        name = bsonReader.readString("name");
        customModelData = bsonReader.readInt32("customModelData");
        type = CosmeticType.valueOf(bsonReader.readString("type"));
        holdable = bsonReader.readBoolean("holdable");
        bsonReader.readEndDocument();
        return new Cosmetic(name, customModelData, type, holdable);
    }

    @Override
    public void encode(BsonWriter bsonWriter, Cosmetic cosmetic, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("name", cosmetic.getName());
        bsonWriter.writeInt32("customModelData", cosmetic.getCustomModelData());
        bsonWriter.writeString("type", cosmetic.getType().name());
        bsonWriter.writeBoolean("holdable", cosmetic.isHoldable());
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Cosmetic> getEncoderClass() {
        return Cosmetic.class;
    }
}
