package eu.revanox.skyblock.log.codec;

import eu.revanox.skyblock.log.AbstractLog;
import eu.revanox.skyblock.log.LogMessage;
import eu.revanox.skyblock.log.privatemessage.PrivateMessageLog;
import eu.revanox.skyblock.log.privatemessage.model.PrivateMessage;
import org.bson.BsonBinary;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

public class LogCodec implements Codec<AbstractLog> {
    @Override
    public AbstractLog decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        String logName = bsonReader.readString("logName");
        UUID logOwner = bsonReader.readBinaryData("logOwner").asUuid();
        AbstractLog abstractLog = null;
        switch (logName) {
            case "privateMessages":
                abstractLog = new PrivateMessageLog(logOwner);
                break;
        }
        bsonReader.readStartArray();
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            bsonReader.readStartDocument();
            LocalDateTime timestamp = LocalDateTime.ofInstant(new Date(bsonReader.readDateTime("timestamp")).toInstant(), ZoneOffset.UTC);
            String entry = bsonReader.readString("entry");
            abstractLog.addEntry(timestamp, PrivateMessage.fromBase64(entry));
            bsonReader.readEndDocument();
        }
        bsonReader.readEndArray();
        bsonReader.readEndDocument();
        return abstractLog;
    }

    @Override
    public void encode(BsonWriter bsonWriter, AbstractLog abstractLog, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("logName", abstractLog.getLogName());
        bsonWriter.writeBinaryData("logOwner", new BsonBinary(abstractLog.getLogOwner()));
        bsonWriter.writeStartArray("logEntries");
        for (Object timestamp : abstractLog.getLogEntries().keySet()) {
            bsonWriter.writeStartDocument();
            bsonWriter.writeDateTime("timestamp", ((LocalDateTime) timestamp).toInstant(ZoneOffset.UTC).toEpochMilli());
            bsonWriter.writeString("entry", ((LogMessage<?>) abstractLog.getLogEntries().get(timestamp)).toBase64());
            bsonWriter.writeEndDocument();
        }
        bsonWriter.writeEndArray();
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<AbstractLog> getEncoderClass() {
        return AbstractLog.class;
    }
}
