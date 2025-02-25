package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.enderchest.menu.EnderChestSelectMenu;
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
