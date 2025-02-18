package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.tag.menu.TagMenu;
import eu.revanox.skyblock.util.ChatAction;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TagCommand extends AbstractCommand {

    public TagCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "tag", null, "titel");
    }

    @Override
    public void run(Player player, String[] args) {

        if(args.length == 0) {
            new TagMenu().getRyseInventory().open(player);
        }
    }
}
