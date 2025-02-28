package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
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
            player.sendMessage(ChatAction.failure("/goto <world>"));
            return;
        }
        String worldName = args[0];
        World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            player.sendMessage(ChatAction.failure("Die Welt " + worldName + " existiert nicht."));
            return;
        }
        SkyBlockPlugin.instance().getTagManager().teleportPlayer(player, world.getSpawnLocation());
        player.sendMessage(ChatAction.of("Du wurdest zur Welt " + worldName + " teleportiert."));
    }
}
