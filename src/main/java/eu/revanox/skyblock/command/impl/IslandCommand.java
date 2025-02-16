package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.island.menu.IslandMenu;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
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
