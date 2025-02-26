package mc.skyblock.plugin.configuration.types;

import com.google.gson.TypeAdapter;
import mc.skyblock.plugin.configuration.annotation.TypeAdapterType;

import java.util.UUID;

@TypeAdapterType(UUID.class)
public class UUIDTypeAdapter extends TypeAdapter<UUID> {

    @Override
    public void write(com.google.gson.stream.JsonWriter jsonWriter, UUID uuid) throws java.io.IOException {
        jsonWriter.value(uuid.toString());
    }

    @Override
    public UUID read(com.google.gson.stream.JsonReader jsonReader) throws java.io.IOException {
        return UUID.fromString(jsonReader.nextString());
    }
}
