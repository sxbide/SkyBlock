package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.perks.menu.PerkMenu;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.SoundAction;
import io.papermc.paper.entity.TeleportFlag;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand extends AbstractCommand {


    public SpawnCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "spawn", null, "hub", "lobby", "leave");
    }

    @Override
    public void run(Player player, String[] args) {

        player.teleport(SkyBlockPlugin.instance().getLocationManager().getPosition("spawn").getLocation(), TeleportFlag.EntityState.RETAIN_PASSENGERS);
        player.sendMessage(ChatAction.of("§7Du wurdest zum Spawnpunkt teleportiert."));

        SoundAction.playGoodWork(player);
    }
}
