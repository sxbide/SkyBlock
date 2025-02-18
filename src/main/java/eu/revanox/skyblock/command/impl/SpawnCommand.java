package eu.revanox.skyblock.command.impl;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.command.model.AbstractCommand;
import eu.revanox.skyblock.perks.menu.PerkMenu;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.SoundAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand extends AbstractCommand {


    public SpawnCommand(@NotNull SkyBlockPlugin plugin) {
        super(plugin, "spawn", null, "hub", "lobby", "leave");
    }

    @Override
    public void run(Player player, String[] args) {
        player.teleportAsync(SkyBlockPlugin.instance().getLocationManager().getPosition("spawn").getLocation()).thenAccept(_ -> {
            player.sendMessage(ChatAction.of("§7Du wurdest zum Spawnpunkt teleportiert."));
        });
        //SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId()); - TODO: Remove this line
        //skyBlockUser.addBalance(0.10); - TODO: Remove this line

        SoundAction.playGoodWork(player);
    }
}
