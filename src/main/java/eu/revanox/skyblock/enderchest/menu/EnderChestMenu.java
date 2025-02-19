package eu.revanox.skyblock.enderchest.menu;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.ItemStackConverter;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnderChestMenu implements Listener {

    SkyBlockPlugin plugin;
    SkyBlockUser skyBlockUser;
    Inventory inventory;
    int enderChestIndex;

    public EnderChestMenu(SkyBlockUser skyBlockUser, int enderChestIndex) {
        this.plugin = SkyBlockPlugin.instance();
        this.skyBlockUser = skyBlockUser;
        this.enderChestIndex = enderChestIndex;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.inventory = Bukkit.createInventory(null, 27, Component.text("Enderkiste #" + enderChestIndex));

        StringBuilder data;
        while (skyBlockUser.getEnderChests().size() < enderChestIndex) {
            data = new StringBuilder();
            data.append("0".repeat(27));
            skyBlockUser.getEnderChests().add(data.toString());
        }

        data = new StringBuilder(skyBlockUser.getEnderChests().get(enderChestIndex - 1));

        if (!data.toString().equals("0".repeat(27))) {
            List<ItemStack> enderChestItems = ItemStackConverter.decodeList(skyBlockUser.getEnderChests().get((enderChestIndex - 1)));

            for (ItemStack itemStack : enderChestItems) {
                if (itemStack == null) continue;
                if (itemStack.getType().isAir()) continue;
                inventory.addItem(itemStack);
            }
        }

    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event) {
        if (event.getInventory().equals(inventory)) {
            List<ItemStack> list = new ArrayList<>();
            Arrays.stream(inventory.getContents()).forEach(itemStack -> {
                if (itemStack != null && !itemStack.getType().isAir()) {
                    list.add(itemStack);
                }
            });
            skyBlockUser.getEnderChests().set(enderChestIndex - 1, ItemStackConverter.encodeList(list));
            plugin.getUserManager().saveUser(skyBlockUser);
            event.getPlayer().sendMessage(ChatAction.of("Deine Enderkiste #" + enderChestIndex + " wurde gespeichert."));
        }
    }



}
