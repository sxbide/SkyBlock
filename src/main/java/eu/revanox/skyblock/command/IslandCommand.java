package eu.revanox.skyblock.command;

import eu.revanox.skyblock.SkyBlockPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class IslandCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {
            //player.openInventory(SkyBlockPlugin.instance().getIslandInventory().inventory(player));
        }
        return false;
    }
}
