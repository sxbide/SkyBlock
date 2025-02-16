package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.perks.menu.PerkMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PerkCommand extends AbstractCommand {


    public PerkCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "perk", null, "perks", "vorteile", "vorteil");
    }

    @Override
    public void run(Player player, String[] args) {
        new PerkMenu().getRyseInventory().open(player);
    }
}
