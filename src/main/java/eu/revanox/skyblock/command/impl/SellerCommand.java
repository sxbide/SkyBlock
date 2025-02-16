package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.perks.menu.PerkMenu;
import eu.revanox.skyblock.seller.menu.SellerMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SellerCommand extends AbstractCommand {


    public SellerCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "seller", null, "verkäufer", "verkaufen", "sell");
    }

    @Override
    public void run(Player player, String[] args) {
        new SellerMenu().getRyseInventory().open(player);
    }
}
