package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.island.menu.IslandMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IslandCommand extends AbstractCommand {


    public IslandCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "insel", null, "is", "island");
    }

    @Override
    public void run(Player player, String[] args) {
        new IslandMenu().getRyseInventory().open(player);
    }
}
