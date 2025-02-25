package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.tag.menu.TagMenu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TagCommand extends AbstractCommand {

    public TagCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "tag", null, "titel");
    }

    @Override
    public void run(Player player, String[] args) {

        if (args.length == 0) {
            new TagMenu().getRyseInventory().open(player);
        }
    }
}
