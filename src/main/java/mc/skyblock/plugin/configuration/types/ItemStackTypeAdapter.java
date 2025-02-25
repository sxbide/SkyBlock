package mc.skyblock.plugin.configuration.types;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import mc.skyblock.plugin.configuration.annotation.TypeAdapterType;
import mc.skyblock.plugin.util.ItemStackConverter;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

@TypeAdapterType(ItemStack.class)
public class ItemStackTypeAdapter extends TypeAdapter<ItemStack> {


    @Override
    public void write(JsonWriter jsonWriter, ItemStack itemStack) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("item").value(ItemStackConverter.encode(itemStack));
        jsonWriter.endObject();
    }

    @Override
    public ItemStack read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        jsonReader.nextName();
        ItemStack itemStack = ItemStackConverter.decode(jsonReader.nextString());
        jsonReader.endObject();
        return itemStack;
    }
}
