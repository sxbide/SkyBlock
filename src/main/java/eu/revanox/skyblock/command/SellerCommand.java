package eu.revanox.skyblock.command;

import eu.revanox.skyblock.seller.menu.SellerMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SellerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (sender instanceof Player player) {
            new SellerMenu().getRyseInventory().open(player);
        }
        return false;
    }
}
