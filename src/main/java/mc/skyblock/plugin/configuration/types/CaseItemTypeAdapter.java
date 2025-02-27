package mc.skyblock.plugin.configuration.types;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import mc.skyblock.plugin.caseopening.mongo.model.item.CaseItem;
import mc.skyblock.plugin.configuration.annotation.TypeAdapterType;
import mc.skyblock.plugin.util.ItemStackConverter;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

@TypeAdapterType(CaseItem.class)
public class CaseItemTypeAdapter extends TypeAdapter<CaseItem> {


    @Override
    public void write(JsonWriter jsonWriter, CaseItem caseItem) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("item").value(ItemStackConverter.encode(caseItem.getItemStack()));
        jsonWriter.name("chance").value(caseItem.getChance());
        jsonWriter.endObject();
    }

    @Override
    public CaseItem read(JsonReader jsonReader) throws IOException {
        jsonReader.beginObject();
        jsonReader.nextName();
        ItemStack itemStack = ItemStackConverter.decode(jsonReader.nextString());
        jsonReader.nextName();
        double chance = jsonReader.nextDouble();
        jsonReader.endObject();
        CaseItem caseItem = new CaseItem(itemStack, chance);
        return caseItem;
    }
}
