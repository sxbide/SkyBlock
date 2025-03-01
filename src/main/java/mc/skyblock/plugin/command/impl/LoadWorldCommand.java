package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LoadWorldCommand extends AbstractCommand {

    public LoadWorldCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "loadworld", "skyblock.loadworld", "lw");
    }

    @Override
    public void run(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatAction.failure("/loadworld <world>"));
            return;
        }
        String worldName = args[0];
        World world = plugin.getServer().getWorld(worldName);
        if (world != null) {
            player.sendMessage(ChatAction.failure("Die Welt " + worldName + " ist bereits geladen."));
            return;
        }
        WorldCreator creator = new WorldCreator(worldName);
        world = creator.createWorld();
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        world.setGameRule(GameRule.DO_FIRE_TICK, false);
        world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        SkyBlockPlugin.instance().getTagManager().teleportPlayer(player, world.getSpawnLocation());
        player.sendMessage(ChatAction.of("Du wurdest zur Welt " + worldName + " teleportiert."));
    }
}
