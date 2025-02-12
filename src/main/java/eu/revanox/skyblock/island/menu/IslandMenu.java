package eu.revanox.skyblock.island.menu;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.util.Util;
import io.github.rysefoxx.inventory.plugin.content.InventoryContents;
import io.github.rysefoxx.inventory.plugin.content.InventoryProvider;
import io.github.rysefoxx.inventory.plugin.pagination.RyseInventory;
import org.bukkit.entity.Player;

public class IslandMenu implements InventoryProvider {

    RyseInventory ryseInventory;

    public IslandMenu() {

        this.ryseInventory = RyseInventory.builder()
                .title("Insel")
                .rows(6)
                .provider(this)
                .disableUpdateTask()
                .build(SkyBlockPlugin.instance());
    }


    @Override
    public void init(Player player, InventoryContents contents) {
        Util.defaultInventory(contents);


    }
}
