package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.perks.menu.PerkMenu;
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
