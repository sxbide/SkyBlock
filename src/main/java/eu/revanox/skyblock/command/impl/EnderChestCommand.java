package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.enderchest.menu.EnderChestSelectMenu;
import eu.revanox.skyblock.perks.menu.PerkMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EnderChestCommand extends AbstractCommand {


    public EnderChestCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "enderchest", null, "ec", "lager", "enderkiste");
    }

    @Override
    public void run(Player player, String[] args) {
        new EnderChestSelectMenu().getRyseInventory().open(player);
    }
}
