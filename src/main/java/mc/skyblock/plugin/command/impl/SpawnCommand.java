package mc.skyblock.plugin.command.impl;

import mc.skyblock.plugin.SkyBlockPlugin;
import mc.skyblock.plugin.command.model.AbstractCommand;
import mc.skyblock.plugin.util.ChatAction;
import mc.skyblock.plugin.util.SoundAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand extends AbstractCommand {


    public SpawnCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "spawn", null, "hub", "lobby", "leave");
    }

    @Override
    public void run(Player player, String[] args) {

        SkyBlockPlugin.instance().getTagManager().teleportPlayer(player, SkyBlockPlugin.instance().getLocationManager().getPosition("spawn").getLocation());
        player.sendMessage(ChatAction.of("§7Du wurdest zum Spawnpunkt teleportiert."));
        SoundAction.playGoodWork(player);
    }
}
