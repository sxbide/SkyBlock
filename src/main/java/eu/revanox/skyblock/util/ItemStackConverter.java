package eu.revanox.skyblock.util;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ItemStackConverter {

    public String encode(ItemStack itemStack) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            dataOutput.writeObject(itemStack);
            return new String(Base64Coder.encode(outputStream.toByteArray()));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to save item stack.", exception);
        }
    }

    public ItemStack decode(String base64) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decode(base64)); BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
            return (ItemStack) dataInput.readObject();
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to decode class type.", exception);
        }
    }

    public String encodeList(List<ItemStack> itemStackList) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(itemStackList.size());
            for (ItemStack itemStack : itemStackList) {
                dataOutput.writeObject(itemStack);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public List<ItemStack> decodeList(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            int size = dataInput.readInt();
            List<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                items.add((ItemStack) dataInput.readObject());
            }
            return items;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to decode class type.", e);
        }
    }
}
