package eu.revanox.skyblock.command;

import eu.revanox.skyblock.SkyBlockPlugin;
import eu.revanox.skyblock.user.model.SkyBlockUser;
import eu.revanox.skyblock.util.ChatAction;
import eu.revanox.skyblock.util.SoundAction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {

            player.teleportAsync(SkyBlockPlugin.instance().getLocationManager().getPosition("spawn").getLocation()).thenAccept(aBoolean -> {
                player.sendMessage(ChatAction.of("§7Du wurdest zum Spawnpunkt teleportiert."));
            });
            SkyBlockUser skyBlockUser = SkyBlockPlugin.instance().getUserManager().getUser(player.getUniqueId());
            skyBlockUser.setBalance((skyBlockUser.getBalance())+0.10);

            SoundAction.playGoodWork(player);
        }
        return false;
    }
}
