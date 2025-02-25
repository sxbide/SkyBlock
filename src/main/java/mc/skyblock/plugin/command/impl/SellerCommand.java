package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.seller.menu.SellerMenu;
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
