package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.cosmetic.menu.CosmeticMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CosmeticCommand extends AbstractCommand {

    public CosmeticCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "cosmetic", null, "kosmetika");
    }

    @Override
    public void run(Player player, String[] args) {
        new CosmeticMenu().getRyseInventory().open(player);
    }
}
