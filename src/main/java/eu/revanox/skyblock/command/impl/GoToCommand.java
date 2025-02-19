package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.util.ChatAction;
import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GoToCommand extends AbstractCommand {

    public GoToCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "goto", "skyblock.goto", "tpWorld");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatAction.failure("Verwende dazu: /goto <world>"));
            return;
        }
        String worldName = args[0];
        World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            player.sendMessage(ChatAction.failure("Die Welt " + worldName + " existiert nicht."));
            return;
        }
        player.teleport(world.getSpawnLocation(), TeleportFlag.EntityState.RETAIN_PASSENGERS);
        player.sendMessage(ChatAction.of("Du wurdest zur Welt " + worldName + " teleportiert."));
    }
}
